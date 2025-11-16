package command;

public class GenerateReportCommand implements Command {
    private main.Main receiver;

    public GenerateReportCommand(main.Main receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        receiver.generateReport();
    }
}