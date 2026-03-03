package lld.learningmanagementsystem.service;

import java.util.Stack;

public class CommandInvoker {
    private static final int MAX_HISTORY=50;

    Stack<Command> history=new Stack<>();
    public void executeCommand(Command command){

        command.execute();
        if (history.size()>=MAX_HISTORY){
            history.remove(0);
        }
        history.push(command);

    }
    public void undoLastCommand(){
        Command command=history.pop();
        command.undo();
    }
}
