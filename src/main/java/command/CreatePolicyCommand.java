package command;

import insurance.*;
import java.util.Scanner;

public class CreatePolicyCommand implements Command {

    private final Derivative derivative;
    private final InsuranceManager manager;
    private final Scanner scanner;
    private int idCounter;

    public CreatePolicyCommand(Derivative derivative,
                               InsuranceManager manager,
                               Scanner scanner,
                               int idCounter) {

        this.derivative = derivative;
        this.manager = manager;
        this.scanner = scanner;
        this.idCounter = idCounter;
    }

    @Override
    public void execute() {

        try {
            System.out.println("\nОберіть тип страхування:");
            System.out.println("1. Медичне\n2. Авто\n3. Майнове\n4. Туристичне\n5. Агро\n6. Життя");
            System.out.print("Вибір: ");
            int type = Integer.parseInt(scanner.nextLine());

            System.out.print("Назва полісу: ");
            String name = scanner.nextLine();

            System.out.print("Зобов'язання: ");
            double obligation = Double.parseDouble(scanner.nextLine());

            System.out.print("Рівень ризику: ");
            double risk = Double.parseDouble(scanner.nextLine());

            System.out.print("Тривалість (дні): ");
            int duration = Integer.parseInt(scanner.nextLine());

            InsurancePolicy policy = manager.createPolicyByType(
                    type, idCounter, name, obligation, risk, duration, scanner
            );

            derivative.addPolicy(policy);

            System.out.println(" Створено поліс ID=" + idCounter);
            idCounter++;

        } catch (Exception e) {
            System.out.println(" Помилка: " + e.getMessage());
        }
    }
}
