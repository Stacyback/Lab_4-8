package insurance;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MedicalInsuranceTest {

    @Test
    void testPremiumCalculation() {
        // Arrange
        // Логіка: obligation * (1 + risk)
        MedicalInsurance policy = new MedicalInsurance(
                20, "Basic Health", 50000, 0.2, 12, 70, "Standard", "All");

        // Act
        double premium = policy.calculatePremium();

        // Assert
        // Очікуємо: 50000 * (1 + 0.2) = 60000.0
        assertEquals(60000.0, premium, 0.001);
    }

    @Test
    void testPremiumWithZeroRisk() {
        // Arrange
        MedicalInsurance policy = new MedicalInsurance(
                21, "Zero Risk Health", 50000, 0.0, 12, 70, "Standard", "All");

        // Act
        double premium = policy.calculatePremium();

        // Assert
        // Очікуємо: 50000 * (1 + 0.0) = 50000.0
        assertEquals(50000.0, premium, 0.001);
    }
}