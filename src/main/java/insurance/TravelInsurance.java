package insurance;

public class TravelInsurance extends InsurancePolicy {
    private String country;
    private int tripDuration;
    private double accidentRisk;

    public TravelInsurance(int id, String name, double obligation, double risk, int duration,
                           String country, int tripDuration, double accidentRisk) {
        super(id, name, obligation, risk, duration);
        this.country = country;
        this.tripDuration = tripDuration;
        this.accidentRisk = accidentRisk;
    }

    @Override
    public double calculatePremium() {
        double durationFactor = tripDuration > 14 ? 1.2 : 1.0;
        return obligation * (1 + risk + accidentRisk) * durationFactor;
    }
}
