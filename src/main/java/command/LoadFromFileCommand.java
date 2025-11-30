package command;

import insurance.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; // <--- 1. Додано імпорти

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadFromFileCommand implements Command {

    // 2. Створюємо об'єкт логера
    private static final Logger logger = LoggerFactory.getLogger(LoadFromFileCommand.class);

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

        logger.info("Спроба завантажити файл: {}", filename); // Пишемо в лог

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
                    // Можна записати попередження про битий рядок (не відправить лист, просто в файл)
                    logger.warn("Помилка парсингу рядка: {}", line);
                }
            }
            String resultMsg = "Завантаження завершено. Успішно: " + success + ", Помилки: " + errors;
            System.out.println(resultMsg);
            logger.info(resultMsg);

        } catch (IOException e) {
            // 🔥🔥🔥 ГОЛОВНЕ ВИПРАВЛЕННЯ 🔥🔥🔥
            // Раніше тут був тільки System.out.println.
            // Ми додаємо logger.error. Саме цей рядок змушує Logback відправити E-mail!

            System.out.println("Помилка читання файлу: " + e.getMessage());
            logger.error("КРИТИЧНА ПОМИЛКА: Не вдалося відкрити файл " + filename, e);
        }
    }
}