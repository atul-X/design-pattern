package lld.taskscheduler.model;

import java.util.List;
import java.util.Map;

public class Workflow {
    private String name;
    private String id;
    private Map<String,String> inputParameters;
    private Map<String,String> outputParameters;
    private List<Task> taskList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getInputParameters() {
        return inputParameters;
    }

    public void setInputParameters(Map<String, String> inputParameters) {
        this.inputParameters = inputParameters;
    }

    public Map<String, String> getOutputParameters() {
        return outputParameters;
    }

    public void setOutputParameters(Map<String, String> outputParameters) {
        this.outputParameters = outputParameters;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
}
