package command;

public class ExitCommand implements Command {

    private final Runnable onExit;

    public ExitCommand(Runnable onExit) {
        this.onExit = onExit;
    }

    @Override
    public void execute() {
        System.out.println("Вихід з програми.");
        if (onExit != null) onExit.run();
    }
}
