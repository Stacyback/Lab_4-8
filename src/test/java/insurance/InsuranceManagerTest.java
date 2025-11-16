package insurance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InsuranceManagerTest {

    private InsuranceManager manager;

    @BeforeEach
    void setUp() {
        manager = new InsuranceManager();
    }

    // Тест 1: Успішне парсування 'auto'
    @Test
    void testParseAutoPolicySuccess() throws Exception {
        // Arrange
        String line = "auto,Моя Mazda,150000,0.3,12,Легковий,0,true";

        // Act
        InsurancePolicy policy = manager.parsePolicyFromLine(line, 100);

        // Assert
        // Перевіряємо, що це дійсно AutoInsurance
        assertInstanceOf(AutoInsurance.class, policy, "Має бути AutoInsurance");
        // Перевіряємо, що ключові поля збігаються
        assertEquals("Моя Mazda", policy.getName());
        assertEquals(0.3, policy.getRisk());
        assertEquals(150000, policy.getObligation());
        assertEquals(100, policy.id);
    }

    // Тест 2: Успішне парсування 'life'
    @Test
    void testParseLifePolicySuccess() throws Exception {
        // Arrange
        String line = "life,На майбутнє,1000000,0.15,30,30,1000000,good";

        // Act
        InsurancePolicy policy = manager.parsePolicyFromLine(line, 101);

        // Assert
        assertInstanceOf(LifeInsurance.class, policy, "Має бути LifeInsurance");
        assertEquals("На майбутнє", policy.getName());
        assertEquals(0.15, policy.getRisk());
        assertEquals(101, policy.id);
    }

    // Тест 3: Помилка - невідомий тип
    @Test
    void testParseUnknownType() {
        // Arrange
        String line = "boat,Титанік,1000,0.9,1,Ocean,1,false";

        // Act & Assert
        // Ми перевіряємо, що метод КИНЕ виняток IllegalArgumentException
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            manager.parsePolicyFromLine(line, 102);
        });

        // Перевіряємо, що повідомлення про помилку правильне
        assertTrue(exception.getMessage().contains("Невідомий тип полісу"));
    }

    // Тест 4: Помилка - недостатньо даних
    @Test
    void testParseNotEnoughData() {
        // Arrange
        String line = "auto,Тесла,200000,0.5"; // Недостатньо полів

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            manager.parsePolicyFromLine(line, 103);
        });

        assertTrue(exception.getMessage().contains("Недостатньо базових даних"));
    }
}