package lld.vendingmechine.service;

public interface Command {
    void execute();
    void undo();
    String getCommandType();
}
