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
            System.out.println(" Немає полісів.");
            return;
        }

        try {
            System.out.print("Мін. ризик: ");
            double min = Double.parseDouble(scanner.nextLine());

            System.out.print("Макс. ризик: ");
            double max = Double.parseDouble(scanner.nextLine());

            System.out.print("Макс. зобов'язання: ");
            double maxObl = Double.parseDouble(scanner.nextLine());

            List<InsurancePolicy> found = derivative.findByParameters(min, max, maxObl);

            if (found.isEmpty()) {
                System.out.println("Нічого не знайдено.");
            } else {
                found.forEach(System.out::println);
            }

        } catch (Exception e) {
            System.out.println(" Невірний формат.");
        }
    }
}
