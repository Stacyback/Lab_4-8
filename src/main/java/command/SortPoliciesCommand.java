package command;

import insurance.Derivative;
import java.util.Scanner;

public class SortPoliciesCommand implements Command {

    private final Derivative derivative;
    private final Scanner scanner;

    public SortPoliciesCommand(Derivative derivative, Scanner scanner) {
        this.derivative = derivative;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        if (derivative.getPolicies().isEmpty()) {
            System.out.println("Немає що сортувати.");
            return;
        }
        System.out.print("Сортувати за зростанням ризику? (true/false): ");
        boolean asc = Boolean.parseBoolean(scanner.nextLine().trim());
        derivative.sortByRisk(asc);
        System.out.println("Поліси відсортовано.");
    }
}
