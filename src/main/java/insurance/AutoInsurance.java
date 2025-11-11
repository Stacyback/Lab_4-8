package insurance;

public class AutoInsurance extends InsurancePolicy {
    private String carType;
    private int accidents;
    private boolean casco;

    public AutoInsurance(int id, String name, double obligation, double risk, int duration,
                         String carType, int accidents, boolean casco) {
        super(id, name, obligation, risk, duration);
        this.carType = carType;
        this.accidents = accidents;
        this.casco = casco;
    }

    @Override
    public double calculatePremium() {
        return obligation * (1 + risk + (casco ? 0.1 : 0));
    }
}
