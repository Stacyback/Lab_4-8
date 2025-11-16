package insurance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DerivativeTest {

    private Derivative derivative;

    // Створюємо "піддослідні" поліси
    // Використовуємо MedicalInsurance, бо у нього найпростіший розрахунок
    private InsurancePolicy lowRiskPolicy = new MedicalInsurance(
            1, "Low Risk", 10000, 0.1, 12, 99, "a", "a"); // Risk = 0.1
    private InsurancePolicy mediumRiskPolicy = new MedicalInsurance(
            2, "Medium Risk", 20000, 0.5, 12, 99, "b", "b"); // Risk = 0.5
    private InsurancePolicy highRiskPolicy = new MedicalInsurance(
            3, "High Risk", 30000, 0.9, 12, 99, "c", "c"); // Risk = 0.9

    // Цей метод з @BeforeEach буде запускатися ПЕРЕД кожним тестом,
    // щоб у нас завжди був свіжий, чистий 'derivative'
    @BeforeEach
    void setUp() {
        derivative = new Derivative();
        derivative.addPolicy(mediumRiskPolicy);
        derivative.addPolicy(highRiskPolicy);
        derivative.addPolicy(lowRiskPolicy);
    }

    @Test
    void testSortByRiskAscending() {
        // Act (Дія)
        // Сортуємо за зростанням (ascending = true)
        derivative.sortByRisk(true);

        // Assert (Перевірка)
        // Перевіряємо, що поліси тепер йдуть у правильному порядку
        List<InsurancePolicy> sortedPolicies = derivative.getPolicies();
        assertEquals(lowRiskPolicy, sortedPolicies.get(0), "Першим має бути поліс з найменшим ризиком");
        assertEquals(mediumRiskPolicy, sortedPolicies.get(1), "Другим має бути поліс з середнім ризиком");
        assertEquals(highRiskPolicy, sortedPolicies.get(2), "Третім має бути поліс з найбільшим ризиком");
    }

    @Test
    void testSortByRiskDescending() {
        // Act (Дія)
        // Сортуємо за спаданням (ascending = false)
        derivative.sortByRisk(false);

        // Assert (Перевірка)
        List<InsurancePolicy> sortedPolicies = derivative.getPolicies();
        assertEquals(highRiskPolicy, sortedPolicies.get(0), "Першим має бути поліс з найбільшим ризиком");
        assertEquals(mediumRiskPolicy, sortedPolicies.get(1), "Другим має бути поліс з середнім ризиком");
        assertEquals(lowRiskPolicy, sortedPolicies.get(2), "Третім має бути поліс з найменшим ризиком");
    }

    @Test
    void testFindByParameters() {
        // Act (Дія)
        // Шукаємо поліси з ризиком між 0.4 і 1.0, та зобов'язанням < 40000
        List<InsurancePolicy> found = derivative.findByParameters(0.4, 1.0, 40000);

        // Assert (Перевірка)
        // Має знайти 'mediumRiskPolicy' (risk 0.5) та 'highRiskPolicy' (risk 0.9)
        assertEquals(2, found.size(), "Має знайти 2 поліси");
        assertEquals(true, found.contains(mediumRiskPolicy), "Має містити 'mediumRiskPolicy'");
        assertEquals(true, found.contains(highRiskPolicy), "Має містити 'highRiskPolicy'");
    }

    @Test
    void testFindByParametersNoResults() {
        // Act (Дія)
        // Шукаємо щось, чого точно немає
        List<InsurancePolicy> found = derivative.findByParameters(0.0, 0.05, 100);

        // Assert (Перевірка)
        assertEquals(0, found.size(), "Не має знайти жодного полісу");
    }
}