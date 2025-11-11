package insurance;

import java.util.List;

public class InsuranceManager {
    public InsurancePolicy createPolicy(String type, int id, String name, double obligation,
                                        double risk, int duration, Object... extraData) {
        return switch (type.toLowerCase()) {
            case "medical" -> new MedicalInsurance(id, name, obligation, risk, duration,
                    (int) extraData[0], (String) extraData[1], (String) extraData[2]);
            case "auto" -> new AutoInsurance(id, name, obligation, risk, duration,
                    (String) extraData[0], (int) extraData[1], (boolean) extraData[2]);
            default -> throw new IllegalArgumentException("Unknown policy type: " + type);
        };
    }

    public void generateReport(Derivative derivative) {
        System.out.println("===== Derivative Report =====");
        for (InsurancePolicy p : derivative.getPolicies()) {
            System.out.println(p);
        }
        System.out.println("Total value: " + derivative.calculateTotalValue());
    }
}
