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

    // --- ТЕСТИ ПАРСИНГУ (Happy Path) ---
    @Test
    void testParseMedicalSuccess() throws Exception {
        String line = "medical,TestMed,1000,0.5,12,60,Full,Surgery";
        InsurancePolicy p = manager.parsePolicyFromLine(line, 1);
        assertTrue(p instanceof MedicalInsurance);
        assertEquals("TestMed", p.getName());
    }

    @Test
    void testParseAutoSuccess() throws Exception {
        String line = "auto,TestCar,2000,0.3,24,Sedan,0,true";
        InsurancePolicy p = manager.parsePolicyFromLine(line, 2);
        assertTrue(p instanceof AutoInsurance);
    }

    @Test
    void testParsePropertySuccess() throws Exception {
        String line = "property,House,5000,0.2,12,Home,Low,true";
        InsurancePolicy p = manager.parsePolicyFromLine(line, 3);
        assertTrue(p instanceof PropertyInsurance);
    }

    @Test
    void testParseTravelSuccess() throws Exception {
        String line = "travel,Trip,1000,0.1,10,USA,14,0.2";
        InsurancePolicy p = manager.parsePolicyFromLine(line, 4);
        assertTrue(p instanceof TravelInsurance);
    }

    @Test
    void testParseAgroSuccess() throws Exception {
        String line = "agro,Wheat,10000,0.4,6,Corn,50.5,0.3";
        InsurancePolicy p = manager.parsePolicyFromLine(line, 5);
        assertTrue(p instanceof AgroInsurance);
    }

    @Test
    void testParseLifeSuccess() throws Exception {
        String line = "life,LifeIns,100000,0.1,120,30,50000,Good";
        InsurancePolicy p = manager.parsePolicyFromLine(line, 6);
        assertTrue(p instanceof LifeInsurance);
    }

    // --- ТЕСТИ ПОМИЛОК (Exception Path) ---

    @Test
    void testParseNotEnoughBaseData() {
        // Рядок занадто короткий (менше 5 елементів)
        String line = "auto,ShortLine";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            manager.parsePolicyFromLine(line, 1);
        });
        assertEquals("Недостатньо базових даних", exception.getMessage());
    }

    @Test
    void testParseUnknownType() {
        // Невідомий тип полісу
        String line = "spaceship,AlienShip,1000,0.5,12,Mars,1,false";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            manager.parsePolicyFromLine(line, 1);
        });
        assertTrue(exception.getMessage().contains("Невідомий тип полісу"));
    }

    @Test
    void testParseAutoNotEnoughSpecificData() {
        // Тип auto, але не вистачає полів для авто (менше 8)
        String line = "auto,Car,1000,0.1,12,Sedan,0"; // 7 елементів
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            manager.parsePolicyFromLine(line, 1);
        });
        assertEquals("Недостатньо даних для 'auto'", exception.getMessage());
    }

    @Test
    void testParseMedicalNotEnoughSpecificData() {
        String line = "medical,Med,1000,0.1,12,50";
        assertThrows(IllegalArgumentException.class, () -> manager.parsePolicyFromLine(line, 1));
    }

    // Аналогічні перевірки для інших типів, щоб покрити всі "case"
    @Test
    void testParsePropertyNotEnoughSpecificData() {
        String line = "property,Prop,1000,0.1,12,House,Low";
        assertThrows(IllegalArgumentException.class, () -> manager.parsePolicyFromLine(line, 1));
    }

    @Test
    void testParseInvalidNumberFormat() {
        // Передаємо текст "ABC" замість числа зобов'язання
        String line = "auto,Car,ABC,0.1,12,Sedan,0,true";
        assertThrows(NumberFormatException.class, () -> {
            manager.parsePolicyFromLine(line, 1);
        });
    }

    // --- ТЕСТИ ЗВІТУ ---

    @Test
    void testGenerateReportEmpty() {
        // Перевіряємо гілку if (derivative.getPolicies().isEmpty())
        Derivative emptyDerivative = new Derivative();

        // Метод void, тому просто викликаємо, щоб переконатися, що не падає
        // і проходить по гілці "isEmpty" (це видно в Coverage)
        manager.generateReport(emptyDerivative);
    }

    @Test
    void testGenerateReportWithData() {
        Derivative d = new Derivative();
        d.addPolicy(new AutoInsurance(1, "A", 100, 0.1, 12, "S", 0, false));

        // Викликаємо для покриття циклу for і логування
        manager.generateReport(d);
    }
}