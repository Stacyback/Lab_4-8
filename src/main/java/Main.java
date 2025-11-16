import insurance.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

// НОВІ ІМПОРТИ для логування
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    // 1. СТВОРЮЄМО ЛОГГЕР для цього класу
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static final Scanner scanner = new Scanner(System.in);
    private static final InsuranceManager manager = new InsuranceManager();
    private static final Derivative derivative = new Derivative();
    private static int idCounter = 1;

    public static void main(String[] args) {
        // 2. Перше повідомлення в лог-файл!
        logger.info("===== СИСТЕМУ СТРАХУВАННЯ ЗАПУЩЕНО =====");

        while (true) {
            System.out.println("\n===== МЕНЮ СТРАХОВОЇ СИСТЕМИ =====");
            System.out.println("1. Створити страховий поліс");
            System.out.println("2. Переглянути всі поліси");
            System.out.println("3. Сортувати поліси за рівнем ризику");
            System.out.println("4. Знайти поліси за параметрами");
            System.out.println("5. Генерувати звіт про дериватив");
            System.out.println("6. Завантажити поліси з файлу");
            System.out.println("0. Вихід");
            System.out.print("Ваш вибір: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> createPolicy();
                case "2" -> showPolicies();
                case "3" -> sortPolicies();
                case "4" -> findPolicies();
                case "5" -> manager.generateReport(derivative);
                case "6" -> loadPoliciesFromFile();
                case "0" -> {
                    logger.info("===== СИСТЕМУ СТРАХУВАННЯ ЗУПИНЕНО =====");
                    System.out.println("До побачення!");
                    return;
                }
                default -> System.out.println("❌ Невірний вибір. Спробуйте ще раз.");
            }
        }
    }

    private static void loadPoliciesFromFile() {
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
                    // 3. ЗАМІНА: Логуємо помилку парсингу рядка у файл
                    logger.warn("ПОМИЛКА ПАРСИНГУ: Не вдалося обробити рядок [{}]. Причина: {}", line, e.getMessage());
                    errorCount++;
                }
            }
        } catch (FileNotFoundException e) {
            // 3. ЗАМІНА: Логуємо критичну помилку у файл
            logger.error("КРИТИЧНА ПОМИЛКА: Файл не знайдено: {}", filename, e);
            System.out.println("❌ Файл не знайдено: " + filename);
            return;
        } catch (IOException e) {
            // 3. ЗАМІНА: Логуємо критичну помилку у файл
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


    private static void createPolicy() {
        try {
            // ... (увесь ваш код для зчитування даних ... залишається БЕЗ ЗМІН) ...
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
                // ... (усі ваші case 1-6 ... залишаються БЕЗ ЗМІН) ...
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
            // 3. ЗАМІНА: Логуємо помилку вводу у файл
            logger.warn("ПОМИЛКА ВВОДУ: Користувач ввів не число.", e);
            System.out.println("❌ Помилка вводу: Очікувалося число.");
        } catch (IllegalArgumentException e) {
            // 3. ЗАМІНА: Логуємо помилку логіки у файл
            logger.warn("ПОМИЛКА ЛОГІКИ: {}", e.getMessage());
            System.out.println("❌ Помилка: " + e.getMessage());
        } catch (Exception e) {
            // 3. ЗАМІНА: Логуємо будь-яку іншу помилку у файл
            logger.error("НЕОЧІКУВАНА ПОМИЛКА при створенні полісу:", e);
            System.out.println("❌ Сталася неочікувана помилка: " + e.getMessage());
        }
    }

    // ... (решта методів: showPolicies, sortPolicies, findPolicies ... залишаються БЕЗ ЗМІН) ...
    // ... (але ви можете додати logger.warn(...) у їхні `if (derivative.getPolicies().isEmpty())` за бажанням) ...
    private static void showPolicies() {
        if (derivative.getPolicies().isEmpty()) {
            logger.warn("Користувач спробував переглянути поліси, але дериватив порожній.");
            System.out.println("⚠️ Поліси відсутні.");
            return;
        }
        System.out.println("\n=== Всі поліси ===");
        derivative.getPolicies().forEach(System.out::println);
    }

    private static void sortPolicies() {
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

    private static void findPolicies() {
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
}