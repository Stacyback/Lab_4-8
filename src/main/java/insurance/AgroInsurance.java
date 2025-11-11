package insurance;

public class  AgroInsurance extends InsurancePolicy {
    private String cropType;
    private double area;
    private double weatherRisk;

    public AgroInsurance(int id, String name, double obligation, double risk, int duration,
                         String cropType, double area, double weatherRisk) {
        super(id, name, obligation, risk, duration);
        this.cropType = cropType;
        this.area = area;
        this.weatherRisk = weatherRisk;
    }

    @Override
    public double calculatePremium() {
        return obligation * (1 + risk + weatherRisk) * (area / 10.0);
    }
}
