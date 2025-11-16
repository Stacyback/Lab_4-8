package command;
import main.Main;

public class ShowPoliciesCommand implements Command {
    private Main receiver;

    public ShowPoliciesCommand(Main receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        receiver.showPolicies();
    }
}