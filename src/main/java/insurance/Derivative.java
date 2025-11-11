package insurance;

import java.util.*;

public class Derivative {
    private List<InsurancePolicy> policies = new ArrayList<>();

    public void addPolicy(InsurancePolicy policy) {
        policies.add(policy);
    }

    public double calculateTotalValue() {
        return policies.stream().mapToDouble(InsurancePolicy::calculatePremium).sum();
    }

    public void sortByRisk(boolean ascending) {
        policies.sort(Comparator.comparingDouble(InsurancePolicy::getRisk));
        if (!ascending) Collections.reverse(policies);
    }

    public List<InsurancePolicy> findByParameters(double minRisk, double maxRisk, double maxObligation) {
        List<InsurancePolicy> result = new ArrayList<>();
        for (InsurancePolicy p : policies) {
            if (p.getRisk() >= minRisk && p.getRisk() <= maxRisk && p.getObligation() <= maxObligation) {
                result.add(p);
            }
        }
        return result;
    }

    public List<InsurancePolicy> getPolicies() {
        return policies;
    }
}
