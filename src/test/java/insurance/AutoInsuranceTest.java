package insurance;

// Імпортуємо класи з JUnit (які ми додали в pom.xml)
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AutoInsuranceTest {

    // @Test каже, що це — тестовий метод
    @Test
    void testPremiumWithCasco() {
        // 1. Arrange (Підготовка)
        // Беремо ваш клас AutoInsurance
        AutoInsurance policy = new AutoInsurance(
                1, "Test Car", 100000, 0.1, 12, "Sedan", 0, true); // casco = true

        // 2. Act (Дія)
        // Викликаємо метод, який тестуємо
        double premium = policy.calculatePremium();

        // 3. Assert (Перевірка)
        // Ми очікуємо, що 100000 * (1 + 0.1 + 0.1) = 120000.0
        assertEquals(120000.0, premium, 0.001); // 0.001 - похибка для double
    }

    @Test
    void testPremiumWithoutCasco() {
        // 1. Arrange
        AutoInsurance policy = new AutoInsurance(
                2, "Test Car 2", 100000, 0.1, 12, "Sedan", 0, false); // casco = false

        // 2. Act
        double premium = policy.calculatePremium();

        // 3. Assert
        // Ми очікуємо, що 100000 * (1 + 0.1 + 0) = 110000.0
        assertEquals(110000.0, premium, 0.001);
    }
}