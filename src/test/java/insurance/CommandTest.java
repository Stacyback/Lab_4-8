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
        // Симулюємо введення: Тип 1 (Medical) -> Назва -> Сума -> Ризик -> Час -> Вік -> Покриття -> Послуга
        String input = "1\nTestMed\n1000\n0.5\n12\n60\nFull\nService\n";
        Scanner scanner = mockScanner(input);

        Command cmd = new CreatePolicyCommand(derivative, manager, scanner, idCounter);
        cmd.execute();

        // Перевіряємо, що поліс додався
        assertEquals(1, derivative.getPolicies().size());
        assertTrue(derivative.getPolicies().get(0) instanceof MedicalInsurance);

        // Перевіряємо виведення
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Поліс створено, ID = 1"));
    }

    @Test
    void testCreatePolicyInvalidInput() {
        // Вводимо букви замість цифри типу
        String input = "abc\n";
        Scanner scanner = mockScanner(input);

        Command cmd = new CreatePolicyCommand(derivative, manager, scanner, idCounter);
        cmd.execute();

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Помилка вводу"));
    }

    @Test
    void testCreatePolicyUnknownType() {
        // Вводимо неіснуючий тип "99"
        // Спочатку введемо коректні загальні дані, а на виборі типу введемо 99
        String input = "99\nName\n100\n0.1\n12\n";
        Scanner scanner = mockScanner(input);

        Command cmd = new CreatePolicyCommand(derivative, manager, scanner, idCounter);
        cmd.execute();

        String output = outputStreamCaptor.toString();
        // У вашому коді default кидає println, але не exception, тому перевіряємо текст
        // Зауваження: ваш код спочатку читає тип, а потім загальні поля.
        // А в тесті input має відповідати порядку: Type -> Name -> Obligation -> Risk -> Duration
        // Виправлений input:
        String correctInputOrder = "99\nName\n100\n0.1\n12\n";

        // Перестворюємо сканер з правильним порядком (якщо код CreatePolicyCommand: спочатку type, потім name...)
        // Дивимось ваш код: так, спочатку type, потім name...
        scanner = mockScanner(correctInputOrder);
        cmd = new CreatePolicyCommand(derivative, manager, scanner, idCounter);
        cmd.execute();

        output = outputStreamCaptor.toString();
        assertTrue(output.contains("Невірний тип полісу"));
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
        // Вводимо "true" (сортувати за зростанням)
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
        // Вводимо діапазон: minRisk=0.4, maxRisk=0.6, maxObl=200
        String input = "0.4\n0.6\n200\n";
        Scanner scanner = mockScanner(input);

        Command cmd = new FindPoliciesCommand(derivative, scanner);
        cmd.execute();

        assertTrue(outputStreamCaptor.toString().contains("Знайдені поліси:"));
    }

    @Test
    void testFindPoliciesNotFound() {
        derivative.addPolicy(new AutoInsurance(1, "Car", 100, 0.5, 12, "S", 0, false));
        // Нереальний діапазон
        String input = "0.1\n0.2\n50\n";
        Scanner scanner = mockScanner(input);

        Command cmd = new FindPoliciesCommand(derivative, scanner);
        cmd.execute();

        assertTrue(outputStreamCaptor.toString().contains("не знайдені"));
    }

    // --- ТЕСТИ GenerateReportCommand ---

    @Test
    void testGenerateReport() {
        derivative.addPolicy(new AutoInsurance(1, "Car", 100, 0.1, 12, "S", 0, false));
        Command cmd = new GenerateReportCommand(manager, derivative);

        // Тут ми просто перевіряємо, що команда викликає метод і не падає
        cmd.execute();

        // InsuranceManager у вас пише в логгер і в System.out, тому перехопимо це
        assertTrue(outputStreamCaptor.toString().contains("Звіт по Деривативу"));
    }

    // --- ТЕСТИ ExitCommand ---

    @Test
    void testExitCommand() {
        // Створюємо змінну-прапорець
        final boolean[] executed = {false};

        Command cmd = new ExitCommand(() -> executed[0] = true);
        cmd.execute();

        assertTrue(outputStreamCaptor.toString().contains("Вихід з програми"));
        assertTrue(executed[0], "ExitCommand має викликати Runnable");
    }

    // --- ТЕСТИ LoadFromFileCommand (Найскладніше: треба створити файл) ---

    @Test
    void testLoadFromFile() throws IOException {
        // 1. Створюємо тимчасовий тестовий файл
        String testFilename = "test_policies_temp.txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(testFilename))) {
            writer.println("auto,TestCar,1000,0.1,12,Sedan,0,true");
            writer.println("# коментар");
            writer.println("bad_line"); // Помилковий рядок
        }

        // 2. Вводимо ім'я цього файлу в сканер
        Scanner scanner = mockScanner(testFilename + "\n");

        Command cmd = new LoadFromFileCommand(derivative, manager, scanner, idCounter);
        cmd.execute();

        // 3. Перевіряємо
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Завантаження завершено"));
        // Має бути 1 успішний (auto) і 1 помилка (bad_line)
        assertTrue(output.contains("Успішно: 1"));

        // Перевіряємо, що в деривативі з'явився поліс
        assertEquals(1, derivative.getPolicies().size());

        // 4. Видаляємо файл
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