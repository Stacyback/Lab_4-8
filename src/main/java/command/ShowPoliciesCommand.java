package command;

import insurance.Derivative;

public class ShowPoliciesCommand implements Command {

    private final Derivative derivative;

    public ShowPoliciesCommand(Derivative derivative) {
        this.derivative = derivative;
    }

    @Override
    public void execute() {
        if (derivative.getPolicies().isEmpty()) {
            System.out.println(" Немає полісів.");
            return;
        }

        derivative.getPolicies().forEach(System.out::println);
    }
}
