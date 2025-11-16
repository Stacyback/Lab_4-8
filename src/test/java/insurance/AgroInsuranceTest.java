package insurance;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AgroInsuranceTest {

    @Test
    void testPremiumCalculation() {
        // Arrange
        // Логіка: obligation * (1 + risk + weatherRisk) * (area / 10.0)
        AgroInsurance policy = new AgroInsurance(
                40, "Wheat Field", 50000, 0.1, 12, "Wheat", 100.0, 0.2);

        // Act
        double premium = policy.calculatePremium();

        // Assert
        // Очікуємо: 50000 * (1 + 0.1 + 0.2) * (100.0 / 10.0)
        // = 50000 * 1.3 * 10.0
        // = 650000.0
        assertEquals(650000.0, premium, 0.001);
    }

    @Test
    void testPremiumWithSmallArea() {
        // Arrange
        // (area / 10.0) -> (5.0 / 10.0) = 0.5
        AgroInsurance policy = new AgroInsurance(
                41, "Small Field", 50000, 0.1, 12, "Corn", 5.0, 0.2);

        // Act
        double premium = policy.calculatePremium();

        // Assert
        // Очікуємо: 50000 * (1 + 0.1 + 0.2) * (5.0 / 10.0)
        // = 50000 * 1.3 * 0.5
        // = 32500.0
        assertEquals(32500.0, premium, 0.001);
    }
}