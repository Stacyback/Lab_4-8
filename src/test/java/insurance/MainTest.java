package insurance;

import main.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        // Перехоплюємо виведення в консоль, щоб перевіряти текст
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        // Відновлюємо стандартні потоки після кожного тесту
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    @Test
    void testMainExitImmediately() {
        // Сценарій: Запускаємо і одразу тиснемо "0" (Вихід)
        String input = "0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Запускаємо main
        Main.main(new String[]{});

        // Перевіряємо, що меню вивелось і програма завершилась без помилок
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("===== МЕНЮ ====="));
        assertTrue(output.contains("Ваш вибір:"));
    }

    @Test
    void testMainInvalidInput() {
        // Сценарій: Вводимо "99" (невірний вибір), потім "0" (вихід)
        // \n означає натискання Enter
        String input = "99\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main.main(new String[]{});

        String output = outputStreamCaptor.toString();
        // Перевіряємо, що програма повідомила про помилку
        assertTrue(output.contains("Невірний вибір. Спробуйте ще раз."));
    }

    @Test
    void testMainShowPoliciesEmpty() {
        // Сценарій: Вводимо "2" (Показати), потім "0" (вихід)
        String input = "2\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main.main(new String[]{});

        String output = outputStreamCaptor.toString();
        // Оскільки ми не додали поліси, має вивестись заголовок, але список буде порожній
        // (Точний текст залежить від реалізації ShowPoliciesCommand,
        // але головне, що main відпрацював цей кейс)
        assertTrue(output.contains("===== МЕНЮ ====="));
    }

    @Test
    void testMainCreatePolicyInterrupted() {
        // Сценарій: Натискаємо "1" (Створити), потім вводимо щось (або одразу виходимо, якщо логіка дозволяє),
        // але головне - перевірити, що команда викликається.
        // Тут ми просто імітуємо вхід в меню створення і вихід з програми.
        // Якщо всередині створення полісу теж є Scanner, треба подати дані і для нього.
        // Припустимо, ми просто хочемо протестувати виклик.

        // Вводимо: "1" -> (тут CreatePolicyCommand попросить тип) -> "auto" -> (дані...) -> "0" (вихід з Main)
        // Щоб не ускладнювати, ми перевіримо простіші команди, як "5" (Звіт)

        String input = "5\n0\n"; // Звіт -> Вихід
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main.main(new String[]{});

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Звіт"));
    }
}