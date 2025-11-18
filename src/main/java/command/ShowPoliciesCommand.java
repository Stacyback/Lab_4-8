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
            System.out.println("Полісів немає.");
            return;
        }
        System.out.println("Всі поліси:");
        derivative.getPolicies().forEach(System.out::println);
    }
}
