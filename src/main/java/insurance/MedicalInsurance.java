package insurance;

public class MedicalInsurance extends InsurancePolicy {
    private int ageLimit;
    private String coverage;
    private String serviceType;

    public MedicalInsurance(int id, String name, double obligation, double risk, int duration,
                            int ageLimit, String coverage, String serviceType) {
        super(id, name, obligation, risk, duration);
        this.ageLimit = ageLimit;
        this.coverage = coverage;
        this.serviceType = serviceType;
    }

    @Override
    public double calculatePremium() {
        return obligation * (1 + risk);
    }
}
