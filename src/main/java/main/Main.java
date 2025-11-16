package main;

import insurance.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// НОВІ ІМПОРТИ ДЛЯ КОМАНД
import command.Command;
import command.CreatePolicyCommand;
import command.SortPoliciesCommand;
import command.GenerateReportCommand;
import command.LoadFromFileCommand;
import command.ShowPoliciesCommand;
import command.FindPoliciesCommand;
import command.ExitCommand;


/**
 * Головний клас програми. Тепер він також виступає в ролі "Отримувача" (Receiver) -
 * він містить всю логіку, яку будуть викликати команди.
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    // Змінюємо все на НЕ-статичне
    private final Scanner scanner = new Scanner(System.in);
    private final InsuranceManager manager = new InsuranceManager();
    private final Derivative derivative = new Derivative();
    private int idCounter = 1;
    private boolean isRunning = true; // Нова змінна для виходу з циклу

    // Карта для зберігання наших команд (замість switch)
    private final Map<String, Command> menuCommands = new HashMap<>();

    //
    // ГОЛОВНИЙ МЕТОД (main) ТЕПЕР ДУЖЕ МАЛЕНЬКИЙ
    //
    public static void main(String[] args) {
        logger.info("===== СИСТЕМУ СТРАХУВАННЯ ЗАПУЩЕНО =====");

        // Ми просто створюємо об'єкт main.Main і запускаємо його
        Main application = new Main();
        application.initializeCommands(); // Створюємо команди
        application.runMenuLoop(); // Запускаємо цикл меню

        logger.info("===== СИСТЕМУ СТРАХУВАННЯ ЗУПИНЕНО =====");
    }

    //
    // 1. ІНІЦІАЛІЗАЦІЯ КОМАНД (Заповнюємо нашу Map)
    //
    private void initializeCommands() {
        // Ми передаємо 'this' (поточний об'єкт main.Main) у кожну команду,
        // щоб вона могла викликати його методи (напр. createPolicy())
        menuCommands.put("1", new CreatePolicyCommand(this));
        menuCommands.put("2", new ShowPoliciesCommand(this));
        menuCommands.put("3", new SortPoliciesCommand(this));
        menuCommands.put("4", new FindPoliciesCommand(this));
        menuCommands.put("5", new GenerateReportCommand(this));
        menuCommands.put("6", new LoadFromFileCommand(this));
        menuCommands.put("0", new ExitCommand(this));
    }

    //
    // 2. ГОЛОВНИЙ ЦИКЛ МЕНЮ (Тепер без switch!)
    //
    private void runMenuLoop() {
        while (isRunning) {
            printMenu();
            String choice = scanner.nextLine();

            Command command = menuCommands.get(choice); // Отримуємо команду з карти

            if (command != null) {
                command.execute(); // Виконуємо її
            } else {
                System.out.println("❌ Невірний вибір. Спробуйте ще раз.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n===== МЕНЮ СТРАХОВОЇ СИСТЕМИ (ПАТЕРН 'КОМАНДА') =====");
        System.out.println("1. Створити страховий поліс");
        System.out.println("2. Переглянути всі поліси");
        System.out.println("3. Сортувати поліси за рівнем ризику");
        System.out.println("4. Знайти поліси за параметрами");
        System.out.println("5. Генерувати звіт про дериватив");
        System.out.println("6. Завантажити поліси з файлу");
        System.out.println("0. Вихід");
        System.out.print("Ваш вибір: ");
    }

    //
    // 3. УСІ МЕТОДИ ЛОГІКИ (тепер вони НЕ static і стали public)
    //

    public void exit() {
        this.isRunning = false;
        System.out.println("До побачення!");
    }

    public void loadPoliciesFromFile() {
        System.out.print("Введіть ім'я файлу (напр. 'policies.txt'): ");
        String filename = scanner.nextLine();

        int successCount = 0;
        int errorCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            logger.info("Починаємо читання файлу: {}", filename);
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }

                try {
                    InsurancePolicy policy = manager.parsePolicyFromLine(line, idCounter);
                    derivative.addPolicy(policy);
                    logger.debug("Успішно розпарсено поліс ID {}: {}", idCounter, policy.getName());
                    idCounter++;
                    successCount++;

                } catch (Exception e) {
                    logger.warn("ПОМИЛКА ПАРСИНГУ: Не вдалося обробити рядок [{}]. Причина: {}", line, e.getMessage());
                    errorCount++;
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("КРИТИЧНА ПОМИЛКА: Файл не знайдено: {}", filename, e);
            System.out.println("❌ Файл не знайдено: " + filename);
            return;
        } catch (IOException e) {
            logger.error("КРИТИЧНА ПОМИЛКА: Помилка читання файлу: {}", filename, e);
            System.out.println("❌ Помилка читання файлу: " + e.getMessage());
            return;
        }

        logger.info("Завантаження файлу {} завершено. Успішно: {}, Помилки: {}", filename, successCount, errorCount);
        System.out.println("✅ Завантаження завершено.");
        System.out.println("Успішно додано: " + successCount + " полісів.");
        if (errorCount > 0) {
            System.out.println("⚠️ Не вдалося обробити: " + errorCount + " рядків. (Див. 'insurance_app.log' для деталей)");
        }
    }

    public void createPolicy() {
        try {
            System.out.println("\nОберіть тип страхування:");
            System.out.println("1. Медичне\n2. Авто\n3. Майнове\n4. Туристичне\n5. Агро\n6. Життя");
            System.out.print("Ваш вибір: ");
            int type = Integer.parseInt(scanner.nextLine());
            System.out.print("Назва полісу: ");
            String name = scanner.nextLine();
            System.out.print("Страхове зобов'язання: ");
            double obligation = Double.parseDouble(scanner.nextLine());
            System.out.print("Рівень ризику (0–1): ");
            double risk = Double.parseDouble(scanner.nextLine());
            System.out.print("Тривалість (місяці): ");
            int duration = Integer.parseInt(scanner.nextLine());

            InsurancePolicy policy = switch (type) {
                case 1 -> {
                    System.out.print("Вікове обмеження: ");
                    int ageLimit = Integer.parseInt(scanner.nextLine());
                    System.out.print("Покриття (напр. 'Standard'): ");
                    String coverage = scanner.nextLine();
                    System.out.print("Тип медичних послуг: ");
                    String serviceType = scanner.nextLine();
                    yield manager.createMedicalPolicy(idCounter, name, obligation, risk, duration,
                            ageLimit, coverage, serviceType);
                }
                case 2 -> {
                    System.out.print("Тип авто: ");
                    String carType = scanner.nextLine();
                    System.out.print("Кількість ДТП: ");
                    int accidents = Integer.parseInt(scanner.nextLine());
                    System.out.print("КАСКО (true/false): ");
                    boolean casco = Boolean.parseBoolean(scanner.nextLine());
                    yield manager.createAutoPolicy(idCounter, name, obligation, risk, duration,
                            carType, accidents, casco);
                }
                case 3 -> {
                    System.out.print("Тип майна: ");
                    String propertyType = scanner.nextLine();
                    System.out.print("Ризиковість регіону (low/medium/high): ");
                    String regionRisk = scanner.nextLine();
                    System.out.print("Захист від крадіжки (true/false): ");
                    boolean theft = Boolean.parseBoolean(scanner.nextLine());
                    yield manager.createPropertyPolicy(idCounter, name, obligation, risk, duration,
                            propertyType, regionRisk, theft);
                }
                case 4 -> {
                    System.out.print("Країна подорожі: ");
                    String country = scanner.nextLine();
                    System.out.print("Тривалість поїздки (днів): ");
                    int tripDays = Integer.parseInt(scanner.nextLine());
                    System.out.print("Ризик нещасного випадку (0–1): ");
                    double accidentRisk = Double.parseDouble(scanner.nextLine());
                    yield manager.createTravelPolicy(idCounter, name, obligation, risk, duration,
                            country, tripDays, accidentRisk);
                }
                case 5 -> {
                    System.out.print("Тип культури: ");
                    String crop = scanner.nextLine();
                    System.out.print("Площа (га): ");
                    double area = Double.parseDouble(scanner.nextLine());
                    System.out.print("Ризик погоди (0–1): ");
                    double weatherRisk = Double.parseDouble(scanner.nextLine());
                    yield manager.createAgroPolicy(idCounter, name, obligation, risk, duration,
                            crop, area, weatherRisk);
                }
                case 6 -> {
                    System.out.print("Вік: ");
                    int age = Integer.parseInt(scanner.nextLine());
                    System.out.print("Сума виплати: ");
                    double payout = Double.parseDouble(scanner.nextLine());
                    System.out.print("Стан здоров'я (good/poor): ");
                    String health = scanner.nextLine();
                    yield manager.createLifePolicy(idCounter, name, obligation, risk, duration,
                            age, payout, health);
                }
                default -> throw new IllegalArgumentException("Невірний тип полісу: " + type);
            };

            derivative.addPolicy(policy);
            logger.info("Успішно створено поліс: {} [ID={}]", policy.getName(), idCounter);
            System.out.println("✅ Поліс " + policy.getName() + " [ID=" + idCounter + "] створено успішно!");
            idCounter++;

        } catch (NumberFormatException e) {
            logger.warn("ПОМИЛКА ВВОДУ: Користувач ввів не число.", e);
            System.out.println("❌ Помилка вводу: Очікувалося число.");
        } catch (IllegalArgumentException e) {
            logger.warn("ПОМИЛКА ЛОГІКИ: {}", e.getMessage());
            System.out.println("❌ Помилка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("НЕОЧІКУВАНА ПОМИЛКА при створенні полісу:", e);
            System.out.println("❌ Сталася неочікувана помилка: " + e.getMessage());
        }
    }

    public void showPolicies() {
        if (derivative.getPolicies().isEmpty()) {
            logger.warn("Користувач спробував переглянути поліси, але дериватив порожній.");
            System.out.println("⚠️ Поліси відсутні.");
            return;
        }
        System.out.println("\n=== Всі поліси ===");
        derivative.getPolicies().forEach(System.out::println);
    }

    public void sortPolicies() {
        if (derivative.getPolicies().isEmpty()) {
            logger.warn("Користувач спробував сортувати поліси, але дериватив порожній.");
            System.out.println("⚠️ Немає що сортувати.");
            return;
        }
        System.out.print("Сортувати за зростанням ризику? (true/false): ");
        boolean asc = Boolean.parseBoolean(scanner.nextLine());
        derivative.sortByRisk(asc);
        logger.info("Поліси відсортовано (ascending={})", asc);
        System.out.println("✅ Поліси відсортовано.");
        showPolicies();
    }

    public void findPolicies() {
        if (derivative.getPolicies().isEmpty()) {
            logger.warn("Користувач спробував знайти поліси, але дериватив порожній.");
            System.out.println("⚠️ Поліси відсутні, пошук неможливий.");
            return;
        }
        try {
            System.out.print("Мінімальний ризик (0–1): ");
            double minR = Double.parseDouble(scanner.nextLine());
            System.out.print("Максимальний ризик (0–1): ");
            double maxR = Double.parseDouble(scanner.nextLine());
            System.out.print("Максимальне зобов'язання: ");
            double maxObl = Double.parseDouble(scanner.nextLine());

            logger.info("Пошук полісів з параметрами: minR={}, maxR={}, maxObl={}", minR, maxR, maxObl);
            var found = derivative.findByParameters(minR, maxR, maxObl);
            if (found.isEmpty()) {
                logger.info("Пошук не дав результатів.");
                System.out.println("⚠️ Поліси за вашими критеріями не знайдено.");
            } else {
                logger.info("Знайдено {} полісів.", found.size());
                System.out.println("\n🔍 Знайдені поліси:");
                found.forEach(System.out::println);
            }
        } catch (NumberFormatException e) {
            logger.warn("ПОМИЛКА ВВОДУ: Користувач ввів не число при пошуку.", e);
            System.out.println("❌ Помилка вводу: Очікувалося число.");
        }
    }

    public void generateReport() {
        manager.generateReport(derivative);
    }
}