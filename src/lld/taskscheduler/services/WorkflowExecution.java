package lld.taskscheduler.services;

import lld.taskscheduler.model.WorkFlowInitiateRequest;
import lld.taskscheduler.model.Workflow;

public interface WorkflowExecution {
    boolean createWorkflowDefinition(Workflow workflow);
    String initiateWorkFlow(WorkFlowInitiateRequest workFlowInitiateRequest);
}
