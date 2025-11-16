package command;
import main.Main;

public class LoadFromFileCommand implements Command {
    private Main receiver;

    public LoadFromFileCommand(Main receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        receiver.loadPoliciesFromFile();
    }
}