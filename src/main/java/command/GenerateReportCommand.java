package command;

import insurance.Derivative;
import insurance.InsuranceManager;

public class GenerateReportCommand implements Command {

    private final InsuranceManager manager;
    private final Derivative derivative;

    public GenerateReportCommand(InsuranceManager manager, Derivative derivative) {
        this.manager = manager;
        this.derivative = derivative;
    }

    @Override
    public void execute() {
        manager.generateReport(derivative);
    }
}
