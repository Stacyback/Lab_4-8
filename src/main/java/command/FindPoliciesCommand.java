package command;

import insurance.Derivative;
import insurance.InsurancePolicy;
import java.util.List;
import java.util.Scanner;

public class FindPoliciesCommand implements Command {

    private final Derivative derivative;
    private final Scanner scanner;

    public FindPoliciesCommand(Derivative derivative, Scanner scanner) {
        this.derivative = derivative;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        if (derivative.getPolicies().isEmpty()) {
            System.out.println("Полісів немає.");
            return;
        }
        try {
            System.out.print("Мінімальний ризик (0-1): ");
            double minR = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Максимальний ризик (0-1): ");
            double maxR = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Максимальне зобов'язання: ");
            double maxObl = Double.parseDouble(scanner.nextLine().trim());

            List<InsurancePolicy> found = derivative.findByParameters(minR, maxR, maxObl);
            if (found.isEmpty()) {
                System.out.println("Поліси за заданими параметрами не знайдені.");
            } else {
                System.out.println("Знайдені поліси:");
                found.forEach(System.out::println);
            }
        } catch (NumberFormatException e) {
            System.out.println("Помилка вводу: очікувалося число.");
        }
    }
}
