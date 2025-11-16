package command;
import main.Main;

public class FindPoliciesCommand implements Command {
    private Main receiver;

    public FindPoliciesCommand(Main receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        receiver.findPolicies();
    }
}