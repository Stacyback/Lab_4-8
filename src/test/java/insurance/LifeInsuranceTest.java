package insurance;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LifeInsuranceTest {

    // Тест 1: Молода людина, гарне здоров'я
    @Test
    void testPremiumYoungAndHealthy() {
        // Arrange
        // Вік < 50, здоров'я 'good'. Обидва фактори = 1.0
        LifeInsurance policy = new LifeInsurance(
                10, "Healthy Life", 100000, 0.1, 30, 30, 500000, "good");

        // Act
        double premium = policy.calculatePremium();

        // Assert
        // Очікуємо: 100000 * (1 + 0.1) * 1.0 * 1.0 = 110000.0
        assertEquals(110000.0, premium, 0.001);
    }

    // Тест 2: Людина у віці, гарне здоров'я
    @Test
    void testPremiumOldAndHealthy() {
        // Arrange
        // Вік > 50 (фактор 1.25), здоров'я 'good' (фактор 1.0)
        LifeInsurance policy = new LifeInsurance(
                11, "Senior Life", 100000, 0.1, 30, 60, 500000, "good");

        // Act
        double premium = policy.calculatePremium();

        // Assert
        // Очікуємо: 100000 * (1 + 0.1) * 1.25 * 1.0 = 137500.0
        assertEquals(137500.0, premium, 0.001);
    }

    // Тест 3: Людина у віці, погане здоров'я (найдорожчий випадок)
    @Test
    void testPremiumOldAndPoorHealth() {
        // Arrange
        // Вік > 50 (фактор 1.25), здоров'я 'poor' (фактор 1.4)
        LifeInsurance policy = new LifeInsurance(
                12, "Risky Life", 100000, 0.1, 30, 60, 500000, "poor");

        // Act
        double premium = policy.calculatePremium();

        // Assert
        // Очікуємо: 100000 * (1 + 0.1) * 1.25 * 1.4 = 192500.0
        assertEquals(192500.0, premium, 0.001);
    }
}