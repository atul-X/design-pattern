package lld.taskscheduler.services;

import lld.taskscheduler.model.WorkFlowInitiateRequest;
import lld.taskscheduler.model.Workflow;
import lld.taskscheduler.services.workflowvalidation.ValidationsStrategy;

import java.util.HashMap;
import java.util.Map;

public class WorkflowExecutionImp implements   WorkflowExecution{

    private ValidationsStrategy validationsStrategy;
    Map<String,Workflow> workflowMap;
    public WorkflowExecutionImp(ValidationsStrategy validationsStrategy) {
        this.validationsStrategy = validationsStrategy;
        workflowMap=new HashMap<>();
    }

    @Override
    public boolean createWorkflowDefinition(Workflow workflow) {
        boolean isValidDefinition =validationsStrategy.validateWorkflowDef(workflow);
        if (!isValidDefinition){
            throw new IllegalStateException("Workflow definition is not correct");
        }
        workflowMap.put(workflow.getName(),workflow);
        return false;
    }

    @Override
    public String initiateWorkFlow(WorkFlowInitiateRequest workFlowInitiateRequest) {

        return "";
    }
}
