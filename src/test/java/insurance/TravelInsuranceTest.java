package insurance;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TravelInsuranceTest {

    // Тест 1: Коротка поїздка (фактор = 1.0)
    @Test
    void testPremiumShortTrip() {
        // Arrange
        // Логіка: obligation * (1 + risk + accidentRisk) * durationFactor
        // tripDuration = 10 (менше 14), тому durationFactor = 1.0
        TravelInsurance policy = new TravelInsurance(
                30, "Short Trip", 10000, 0.1, 12, "Poland", 10, 0.3);

        // Act
        double premium = policy.calculatePremium();

        // Assert
        // Очікуємо: 10000 * (1 + 0.1 + 0.3) * 1.0 = 10000 * 1.4 = 14000.0
        assertEquals(14000.0, premium, 0.001);
    }

    // Тест 2: Довга поїздка (фактор = 1.2)
    @Test
    void testPremiumLongTrip() {
        // Arrange
        // tripDuration = 20 (більше 14), тому durationFactor = 1.2
        TravelInsurance policy = new TravelInsurance(
                31, "Long Trip", 10000, 0.1, 12, "USA", 20, 0.3);

        // Act
        double premium = policy.calculatePremium();

        // Assert
        // Очікуємо: 10000 * (1 + 0.1 + 0.3) * 1.2 = 10000 * 1.4 * 1.2 = 16800.0
        assertEquals(16800.0, premium, 0.001);
    }
}