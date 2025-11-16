package command;
import main.Main;

public class SortPoliciesCommand implements Command {
    private Main receiver;

    public SortPoliciesCommand(Main receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        receiver.sortPolicies();
    }
}