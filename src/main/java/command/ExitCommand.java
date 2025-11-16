package command;
import main.Main;

public class ExitCommand implements Command {
    private Main receiver;

    public ExitCommand(main.Main receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        receiver.exit();
    }
}