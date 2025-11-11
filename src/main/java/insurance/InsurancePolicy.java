package insurance;

public abstract class InsurancePolicy {
    protected int id;
    protected String name;
    protected double obligation;
    protected double risk;
    protected int duration;

    public InsurancePolicy(int id, String name, double obligation, double risk, int duration) {
        this.id = id;
        this.name = name;
        this.obligation = obligation;
        this.risk = risk;
        this.duration = duration;
    }

    public abstract double calculatePremium();

    public double getObligation() { return obligation; }
    public double getRisk() { return risk; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return String.format("%s [ID=%d, Risk=%.2f, Obligation=%.2f]", name, id, risk, obligation);
    }
}
