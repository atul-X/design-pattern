package lld.learningmanagementsystem.service;

public class CommandImpl implements Command{
    private Runnable executeAction;
    public Runnable undoAction;

    public CommandImpl(Runnable executeAction, Runnable undoAction) {
        this.executeAction = executeAction;
        this.undoAction = undoAction;
    }

    @Override
    public void execute() {
        executeAction.run();
    }

    @Override
    public void undo() {
        undoAction.run();
    }
}
