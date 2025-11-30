package insurance;

import command.Command;
import command.SortPoliciesCommand;
import command.CreatePolicyCommand;
import command.LoadFromFileCommand;
import command.FindPoliciesCommand;
import command.ShowPoliciesCommand;
import command.GenerateReportCommand;
import command.ExitCommand;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {

    private Derivative derivative;
    private InsuranceManager manager;
    private AtomicInteger idCounter;

    // Для перехоплення System.out
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        derivative = new Derivative();
        manager = new InsuranceManager();
        idCounter = new AtomicInteger(1);

        // Перехоплюємо консоль, щоб перевіряти, що пишуть команди
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        // Повертаємо консоль назад
        System.setOut(originalOut);
    }

    // Допоміжний метод для створення Scanner з рядка
    private Scanner mockScanner(String input) {
        return new Scanner(new ByteArrayInputStream(input.getBytes()));
    }

    // --- ТЕСТИ CreatePolicyCommand ---
    @Test
    void testCreateMedicalPolicySuccess() {
        // 1 -> Medical
        String input = "1\nTestMed\n1000\n0.5\n12\n60\nFull\nService\n";
        Scanner scanner = mockScanner(input);
        Command cmd = new CreatePolicyCommand(derivative, manager, scanner, idCounter);
        cmd.execute();

        assertEquals(1, derivative.getPolicies().size());
        assertTrue(derivative.getPolicies().get(0) instanceof MedicalInsurance);
        assertTrue(outputStreamCaptor.toString().contains("Поліс створено, ID = 1"));
    }

    @Test
    void testCreateAutoPolicySuccess() {
        // 2 -> Auto
        String input = "2\nCarTest\n2000\n0.2\n24\nSedan\n0\ntrue\n";
        Scanner scanner = mockScanner(input);
        Command cmd = new CreatePolicyCommand(derivative, manager, scanner, idCounter);
        cmd.execute();

        assertEquals(1, derivative.getPolicies().size());
        assertTrue(derivative.getPolicies().get(0) instanceof AutoInsurance);
    }

    @Test
    void testCreatePropertyPolicySuccess() {
        // 3 -> Property
        String input = "3\nHouseTest\n5000\n0.1\n12\nApartment\nHigh\ntrue\n";
        Scanner scanner = mockScanner(input);
        Command cmd = new CreatePolicyCommand(derivative, manager, scanner, idCounter);
        cmd.execute();

        assertEquals(1, derivative.getPolicies().size());
        assertTrue(derivative.getPolicies().get(0) instanceof PropertyInsurance);
    }

    @Test
    void testCreateTravelPolicySuccess() {
        // 4 -> Travel
        String input = "4\nTripTest\n1000\n0.1\n1\nUSA\n10\n0.05\n";
        Scanner scanner = mockScanner(input);
        Command cmd = new CreatePolicyCommand(derivative, manager, scanner, idCounter);
        cmd.execute();

        assertEquals(1, derivative.getPolicies().size());
        assertTrue(derivative.getPolicies().get(0) instanceof TravelInsurance);
    }

    @Test
    void testCreateAgroPolicySuccess() {
        // 5 -> Agro
        String input = "5\nWheatTest\n10000\n0.3\n6\nWheat\n100.5\n0.2\n";
        Scanner scanner = mockScanner(input);
        Command cmd = new CreatePolicyCommand(derivative, manager, scanner, idCounter);
        cmd.execute();

        assertEquals(1, derivative.getPolicies().size());
        assertTrue(derivative.getPolicies().get(0) instanceof AgroInsurance);
    }

    @Test
    void testCreateLifePolicySuccess() {
        // 6 -> Life
        String input = "6\nLifeTest\n50000\n0.1\n120\n30\n50000\nGood\n";
        Scanner scanner = mockScanner(input);
        Command cmd = new CreatePolicyCommand(derivative, manager, scanner, idCounter);
        cmd.execute();

        assertEquals(1, derivative.getPolicies().size());
        assertTrue(derivative.getPolicies().get(0) instanceof LifeInsurance);
    }

    @Test
    void testCreatePolicyInvalidInput() {
        // Вводимо букви замість цифри типу
        String input = "abc\n";
        Scanner scanner = mockScanner(input);
        Command cmd = new CreatePolicyCommand(derivative, manager, scanner, idCounter);
        cmd.execute();

        assertTrue(outputStreamCaptor.toString().contains("Помилка вводу"));
    }

    @Test
    void testCreatePolicyUnknownType() {
        // Вводимо неіснуючий тип "99"
        String input = "99\nName\n100\n0.1\n12\n";
        Scanner scanner = mockScanner(input);
        Command cmd = new CreatePolicyCommand(derivative, manager, scanner, idCounter);
        cmd.execute();

        assertTrue(outputStreamCaptor.toString().contains("Невірний тип полісу"));
    }

    // --- ТЕСТИ ShowPoliciesCommand ---

    @Test
    void testShowPoliciesEmpty() {
        Command cmd = new ShowPoliciesCommand(derivative);
        cmd.execute();
        assertTrue(outputStreamCaptor.toString().contains("Полісів немає"));
    }

    @Test
    void testShowPoliciesNotEmpty() {
        derivative.addPolicy(new AutoInsurance(1, "Car", 100, 0.1, 12, "S", 0, false));
        Command cmd = new ShowPoliciesCommand(derivative);
        cmd.execute();
        assertTrue(outputStreamCaptor.toString().contains("Всі поліси:"));
        assertTrue(outputStreamCaptor.toString().contains("Car"));
    }

    // --- ТЕСТИ SortPoliciesCommand ---

    @Test
    void testSortPoliciesEmpty() {
        Scanner scanner = mockScanner("true\n");
        Command cmd = new SortPoliciesCommand(derivative, scanner);
        cmd.execute();
        assertTrue(outputStreamCaptor.toString().contains("Немає що сортувати"));
    }

    @Test
    void testSortPoliciesSuccess() {
        derivative.addPolicy(new AutoInsurance(1, "Car", 100, 0.1, 12, "S", 0, false));
        Scanner scanner = mockScanner("true\n");
        Command cmd = new SortPoliciesCommand(derivative, scanner);
        cmd.execute();
        assertTrue(outputStreamCaptor.toString().contains("Поліси відсортовано"));
    }

    // --- ТЕСТИ FindPoliciesCommand ---

    @Test
    void testFindPoliciesEmpty() {
        Scanner scanner = mockScanner("");
        Command cmd = new FindPoliciesCommand(derivative, scanner);
        cmd.execute();
        assertTrue(outputStreamCaptor.toString().contains("Полісів немає"));
    }

    @Test
    void testFindPoliciesSuccess() {
        derivative.addPolicy(new AutoInsurance(1, "Car", 100, 0.5, 12, "S", 0, false));
        String input = "0.4\n0.6\n200\n";
        Scanner scanner = mockScanner(input);
        Command cmd = new FindPoliciesCommand(derivative, scanner);
        cmd.execute();

        assertTrue(outputStreamCaptor.toString().contains("Знайдені поліси:"));
    }

    @Test
    void testFindPoliciesNotFound() {
        derivative.addPolicy(new AutoInsurance(1, "Car", 100, 0.5, 12, "S", 0, false));
        String input = "0.1\n0.2\n50\n";
        Scanner scanner = mockScanner(input);
        Command cmd = new FindPoliciesCommand(derivative, scanner);
        cmd.execute();

        assertTrue(outputStreamCaptor.toString().contains("не знайдені"));
    }

    @Test
    void testFindPoliciesInputError() {
        derivative.addPolicy(new AutoInsurance(1, "Car", 100, 0.5, 12, "S", 0, false));
        // Вводимо текст замість числа для ризику
        String input = "abc\n";
        Scanner scanner = mockScanner(input);
        Command cmd = new FindPoliciesCommand(derivative, scanner);
        cmd.execute();

        assertTrue(outputStreamCaptor.toString().contains("Помилка вводу"));
    }

    // --- ТЕСТИ GenerateReportCommand ---

    @Test
    void testGenerateReport() {
        derivative.addPolicy(new AutoInsurance(1, "Car", 100, 0.1, 12, "S", 0, false));
        Command cmd = new GenerateReportCommand(manager, derivative);
        cmd.execute();
        assertTrue(outputStreamCaptor.toString().contains("Звіт по Деривативу"));
    }

    // --- ТЕСТИ ExitCommand ---

    @Test
    void testExitCommand() {
        final boolean[] executed = {false};
        Command cmd = new ExitCommand(() -> executed[0] = true);
        cmd.execute();
        assertTrue(outputStreamCaptor.toString().contains("Вихід з програми"));
        assertTrue(executed[0]);
    }

    // --- ТЕСТИ LoadFromFileCommand ---

    @Test
    void testLoadFromFile() throws IOException {
        String testFilename = "test_policies_temp.txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(testFilename))) {
            writer.println("auto,TestCar,1000,0.1,12,Sedan,0,true");
            writer.println("# коментар");
            writer.println("bad_line");
        }

        Scanner scanner = mockScanner(testFilename + "\n");
        Command cmd = new LoadFromFileCommand(derivative, manager, scanner, idCounter);
        cmd.execute();

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Завантаження завершено"));
        assertTrue(output.contains("Успішно: 1"));
        assertEquals(1, derivative.getPolicies().size());

        new File(testFilename).delete();
    }

    @Test
    void testLoadFromFileNotFound() {
        Scanner scanner = mockScanner("non_existent_file.txt\n");
        Command cmd = new LoadFromFileCommand(derivative, manager, scanner, idCounter);
        cmd.execute();
        assertTrue(outputStreamCaptor.toString().contains("Помилка читання файлу"));
    }
}