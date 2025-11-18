package insurance;

import java.util.List;

// НОВІ ІМПОРТИ для логування
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InsuranceManager {

    // 1. СТВОРЮЄМО ЛОГГЕР для цього класу
    private static final Logger logger = LoggerFactory.getLogger(InsuranceManager.class);

    // ... (усі ваші методи create...Policy ... залишаються БЕЗ ЗМІН) ...
    public InsurancePolicy createMedicalPolicy(int id, String name, double obligation, double risk, int duration,
                                               int ageLimit, String coverage, String serviceType) {
        return new MedicalInsurance(id, name, obligation, risk, duration,
                ageLimit, coverage, serviceType);
    }
    public InsurancePolicy createAutoPolicy(int id, String name, double obligation, double risk, int duration,
                                            String carType, int accidents, boolean casco) {
        return new AutoInsurance(id, name, obligation, risk, duration,
                carType, accidents, casco);
    }
    public InsurancePolicy createPropertyPolicy(int id, String name, double obligation, double risk, int duration,
                                                String propertyType, String regionRisk, boolean theft) {
        return new PropertyInsurance(id, name, obligation, risk, duration,
                propertyType, regionRisk, theft);
    }
    public InsurancePolicy createTravelPolicy(int id, String name, double obligation, double risk, int duration,
                                              String country, int tripDays, double accidentRisk) {
        return new TravelInsurance(id, name, obligation, risk, duration,
                country, tripDays, accidentRisk);
    }
    public InsurancePolicy createAgroPolicy(int id, String name, double obligation, double risk, int duration,
                                            String crop, double area, double weatherRisk) {
        return new AgroInsurance(id, name, obligation, risk, duration,
                crop, area, weatherRisk);
    }
    public InsurancePolicy createLifePolicy(int id, String name, double obligation, double risk, int duration,
                                            int age, double payout, String health) {
        return new LifeInsurance(id, name, obligation, risk, duration,
                age, payout, health);
    }

    public void generateReport(Derivative derivative) {
        // 3. ЗАМІНА: Використовуємо logger.info замість System.out.println
        logger.info("===== Початок генерації звіту по деривативу =====");
        System.out.println("\n===== Звіт по Деривативу =====");

        if (derivative.getPolicies().isEmpty()) {
            logger.warn("Звіт не згенеровано: дериватив порожній.");
            System.out.println(" Дериватив порожній.");
            return;
        }

        for (InsurancePolicy p : derivative.getPolicies()) {
            String reportLine = String.format("%s (Премія: %.2f)", p.toString(), p.calculatePremium());
            logger.info("Елемент звіту: {}", reportLine);
            System.out.println(reportLine);
        }

        double totalValue = derivative.calculateTotalValue();
        String totalLine = String.format("Загальна вартість (сума премій): %.2f", totalValue);
        logger.info("===== Кінець звіту. {} =====", totalLine);
        System.out.println("---------------------------------");
        System.out.println(totalLine);
    }

    // ... (ваш метод parsePolicyFromLine ... залишається БЕЗ ЗМІН) ...
    public InsurancePolicy parsePolicyFromLine(String line, int id) throws Exception {
        String[] data = line.split(",");
        if (data.length < 5) {
            throw new IllegalArgumentException("Недостатньо базових даних");
        }
        String type = data[0].trim().toLowerCase();
        String name = data[1].trim();
        double obligation = Double.parseDouble(data[2].trim());
        double risk = Double.parseDouble(data[3].trim());
        int duration = Integer.parseInt(data[4].trim());

        switch (type) {
            case "medical":
                if (data.length < 8) throw new IllegalArgumentException("Недостатньо даних для 'medical'");
                int ageLimit = Integer.parseInt(data[5].trim());
                String coverage = data[6].trim();
                String serviceType = data[7].trim();
                return createMedicalPolicy(id, name, obligation, risk, duration, ageLimit, coverage, serviceType);
            case "auto":
                if (data.length < 8) throw new IllegalArgumentException("Недостатньо даних для 'auto'");
                String carType = data[5].trim();
                int accidents = Integer.parseInt(data[6].trim());
                boolean casco = Boolean.parseBoolean(data[7].trim());
                return createAutoPolicy(id, name, obligation, risk, duration, carType, accidents, casco);
            case "property":
                if (data.length < 8) throw new IllegalArgumentException("Недостатньо даних для 'property'");
                String propertyType = data[5].trim();
                String regionRisk = data[6].trim();
                boolean theft = Boolean.parseBoolean(data[7].trim());
                return createPropertyPolicy(id, name, obligation, risk, duration, propertyType, regionRisk, theft);
            case "travel":
                if (data.length < 8) throw new IllegalArgumentException("Недостатньо даних для 'travel'");
                String country = data[5].trim();
                int tripDays = Integer.parseInt(data[6].trim());
                double accidentRisk = Double.parseDouble(data[7].trim());
                return createTravelPolicy(id, name, obligation, risk, duration, country, tripDays, accidentRisk);
            case "agro":
                if (data.length < 8) throw new IllegalArgumentException("Недостатньо даних для 'agro'");
                String crop = data[5].trim();
                double area = Double.parseDouble(data[6].trim());
                double weatherRisk = Double.parseDouble(data[7].trim());
                return createAgroPolicy(id, name, obligation, risk, duration, crop, area, weatherRisk);
            case "life":
                if (data.length < 8) throw new IllegalArgumentException("Недостатньо даних для 'life'");
                int age = Integer.parseInt(data[5].trim());
                double payout = Double.parseDouble(data[6].trim());
                String health = data[7].trim();
                return createLifePolicy(id, name, obligation, risk, duration, age, payout, health);
            default:
                throw new IllegalArgumentException("Невідомий тип полісу: " + type);
        }
    }
}