package command;

import insurance.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadFromFileCommand implements Command {

    private final Derivative derivative;
    private final InsuranceManager manager;
    private final Scanner scanner;
    private final AtomicInteger idCounter;

    public LoadFromFileCommand(Derivative derivative,
                               InsuranceManager manager,
                               Scanner scanner,
                               AtomicInteger idCounter) {
        this.derivative = derivative;
        this.manager = manager;
        this.scanner = scanner;
        this.idCounter = idCounter;
    }

    @Override
    public void execute() {
        System.out.print("Введіть ім'я файлу: ");
        String filename = scanner.nextLine().trim();

        int success = 0;
        int errors = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                try {
                    int id = idCounter.get();
                    InsurancePolicy p = manager.parsePolicyFromLine(line, id);
                    derivative.addPolicy(p);
                    idCounter.incrementAndGet();
                    success++;
                } catch (Exception e) {
                    errors++;
                }
            }
            System.out.println("Завантаження завершено. Успішно: " + success + ", Помилки: " + errors);
        } catch (IOException e) {
            System.out.println("Помилка читання файлу: " + e.getMessage());
        }
    }
}
