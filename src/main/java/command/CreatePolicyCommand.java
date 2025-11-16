package command;

public class CreatePolicyCommand implements Command {
    private main.Main receiver; // Посилання на 'отримувача' логіки

    public CreatePolicyCommand(main.Main receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        receiver.createPolicy(); // Просто викликаємо метод у main.Main
    }
}