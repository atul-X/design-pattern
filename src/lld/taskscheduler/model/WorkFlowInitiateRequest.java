package lld.taskscheduler.model;

import java.util.Map;

public class WorkFlowInitiateRequest {
    private String name;
    private String version;
    public Map<String,String> inputParameters;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, String> getInputParameters() {
        return inputParameters;
    }

    public void setInputParameters(Map<String, String> inputParameters) {
        this.inputParameters = inputParameters;
    }
}
