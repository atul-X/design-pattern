package lld.taskscheduler.services.workflowvalidation;

import lld.taskscheduler.model.Workflow;

public interface ValidationsStrategy {
    boolean validateWorkflowDef(Workflow workflow);
}
