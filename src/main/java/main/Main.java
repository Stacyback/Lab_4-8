package main;

import command.*;
import insurance.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Derivative derivative = new Derivative();
        InsuranceManager manager = new InsuranceManager();
        AtomicInteger idCounter = new AtomicInteger(1);

        Map<String, Command> commands = new HashMap<>();
        final boolean[] running = {true};

        commands.put("1", new CreatePolicyCommand(derivative, manager, scanner, idCounter));
        commands.put("2", new ShowPoliciesCommand(derivative));
        commands.put("3", new SortPoliciesCommand(derivative, scanner));
        commands.put("4", new FindPoliciesCommand(derivative, scanner));
        commands.put("5", new GenerateReportCommand(manager, derivative));
        commands.put("6", new LoadFromFileCommand(derivative, manager, scanner, idCounter));
        commands.put("0", new ExitCommand(() -> running[0] = false));

        while (running[0]) {
            printMenu();
            String choice = scanner.nextLine().trim();
            Command cmd = commands.get(choice);
            if (cmd != null) {
                cmd.execute();
            } else {
                System.out.println("Невірний вибір. Спробуйте ще раз.");
            }
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("===== МЕНЮ =====");
        System.out.println("1 - Створити поліс");
        System.out.println("2 - Показати всі поліси");
        System.out.println("3 - Сортувати поліси за ризиком");
        System.out.println("4 - Знайти поліси за параметрами");
        System.out.println("5 - Генерувати звіт");
        System.out.println("6 - Завантажити поліси з файлу");
        System.out.println("0 - Вихід");
        System.out.print("Ваш вибір: ");
    }
}
