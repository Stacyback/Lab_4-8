import insurance.*;
import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final InsuranceManager manager = new InsuranceManager();
    private static final Derivative derivative = new Derivative();
    private static int idCounter = 1;

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== МЕНЮ СТРАХОВОЇ СИСТЕМИ =====");
            System.out.println("1. Створити страховий поліс");
            System.out.println("2. Переглянути всі поліси");
            System.out.println("3. Сортувати поліси за рівнем ризику");
            System.out.println("4. Знайти поліси за параметрами");
            System.out.println("5. Генерувати звіт про дериватив");
            System.out.println("0. Вихід");
            System.out.print("Ваш вибір: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> createPolicy();
                case "2" -> showPolicies();
                case "3" -> sortPolicies();
                case "4" -> findPolicies();
                case "5" -> manager.generateReport(derivative);
                case "0" -> {
                    System.out.println("До побачення!");
                    return;
                }
                default -> System.out.println("❌ Невірний вибір. Спробуйте ще раз.");
            }
        }
    }

    private static void createPolicy() {
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

        try {
            InsurancePolicy policy = switch (type) {
                case 1 -> {
                    System.out.print("Вікове обмеження: ");
                    int ageLimit = Integer.parseInt(scanner.nextLine());
                    System.out.print("Покриття (напр. 'Standard'): ");
                    String coverage = scanner.nextLine();
                    System.out.print("Тип медичних послуг: ");
                    String serviceType = scanner.nextLine();
                    yield manager.createPolicy("medical", idCounter++, name, obligation, risk, duration,
                            ageLimit, coverage, serviceType);
                }
                case 2 -> {
                    System.out.print("Тип авто: ");
                    String carType = scanner.nextLine();
                    System.out.print("Кількість ДТП: ");
                    int accidents = Integer.parseInt(scanner.nextLine());
                    System.out.print("КАСКО (true/false): ");
                    boolean casco = Boolean.parseBoolean(scanner.nextLine());
                    yield manager.createPolicy("auto", idCounter++, name, obligation, risk, duration,
                            carType, accidents, casco);
                }
                case 3 -> {
                    System.out.print("Тип майна: ");
                    String propertyType = scanner.nextLine();
                    System.out.print("Ризиковість регіону (low/medium/high): ");
                    String regionRisk = scanner.nextLine();
                    System.out.print("Захист від крадіжки (true/false): ");
                    boolean theft = Boolean.parseBoolean(scanner.nextLine());
                    yield manager.createPolicy("property", idCounter++, name, obligation, risk, duration,
                            propertyType, regionRisk, theft);
                }
                case 4 -> {
                    System.out.print("Країна подорожі: ");
                    String country = scanner.nextLine();
                    System.out.print("Тривалість поїздки (днів): ");
                    int tripDays = Integer.parseInt(scanner.nextLine());
                    System.out.print("Ризик нещасного випадку (0–1): ");
                    double accidentRisk = Double.parseDouble(scanner.nextLine());
                    yield manager.createPolicy("travel", idCounter++, name, obligation, risk, duration,
                            country, tripDays, accidentRisk);
                }
                case 5 -> {
                    System.out.print("Тип культури: ");
                    String crop = scanner.nextLine();
                    System.out.print("Площа (га): ");
                    double area = Double.parseDouble(scanner.nextLine());
                    System.out.print("Ризик погоди (0–1): ");
                    double weatherRisk = Double.parseDouble(scanner.nextLine());
                    yield manager.createPolicy("agro", idCounter++, name, obligation, risk, duration,
                            crop, area, weatherRisk);
                }
                case 6 -> {
                    System.out.print("Вік: ");
                    int age = Integer.parseInt(scanner.nextLine());
                    System.out.print("Сума виплати: ");
                    double payout = Double.parseDouble(scanner.nextLine());
                    System.out.print("Стан здоров'я (good/poor): ");
                    String health = scanner.nextLine();
                    yield manager.createPolicy("life", idCounter++, name, obligation, risk, duration,
                            age, payout, health);
                }
                default -> throw new IllegalArgumentException("Невірний тип.");
            };

            derivative.addPolicy(policy);
            System.out.println("✅ Поліс створено успішно!");
        } catch (Exception e) {
            System.out.println("❌ Помилка створення полісу: " + e.getMessage());
        }
    }

    private static void showPolicies() {
        if (derivative.getPolicies().isEmpty()) {
            System.out.println("⚠️ Поліси відсутні.");
            return;
        }
        System.out.println("\n=== Всі поліси ===");
        derivative.getPolicies().forEach(System.out::println);
    }

    private static void sortPolicies() {
        System.out.print("Сортувати за зростанням ризику? (true/false): ");
        boolean asc = Boolean.parseBoolean(scanner.nextLine());
        derivative.sortByRisk(asc);
        System.out.println("✅ Поліси відсортовано.");
    }

    private static void findPolicies() {
        System.out.print("Мінімальний ризик: ");
        double minR = Double.parseDouble(scanner.nextLine());
        System.out.print("Максимальний ризик: ");
        double maxR = Double.parseDouble(scanner.nextLine());
        System.out.print("Максимальне зобов'язання: ");
        double maxObl = Double.parseDouble(scanner.nextLine());

        var found = derivative.findByParameters(minR, maxR, maxObl);
        if (found.isEmpty()) {
            System.out.println("⚠️ Поліси не знайдено.");
        } else {
            System.out.println("\n🔍 Знайдені поліси:");
            found.forEach(System.out::println);
        }
    }
}
