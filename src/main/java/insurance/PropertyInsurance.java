package insurance;

public class PropertyInsurance extends InsurancePolicy {
    private String propertyType;
    private String regionRisk; // "low", "medium", "high"
    private boolean theftProtection;

    public PropertyInsurance(int id, String name, double obligation, double risk, int duration,
                             String propertyType, String regionRisk, boolean theftProtection) {
        super(id, name, obligation, risk, duration);
        this.propertyType = propertyType;
        this.regionRisk = regionRisk;
        this.theftProtection = theftProtection;
    }

    @Override
    public double calculatePremium() {
        double regionFactor = switch (regionRisk.toLowerCase()) {
            case "high" -> 1.3;
            case "medium" -> 1.15;
            default -> 1.0; // "low"
        };

        double theftFactor = theftProtection ? 1.1 : 1.0;

        return obligation * (1 + risk) * regionFactor * theftFactor;
    }

    @Override
    public String toString() {
        return String.format("Майнове страхування [ID=%d, Risk=%.2f, Obligation=%.2f, Type=%s]",
                id, risk, obligation, propertyType);
    }
}