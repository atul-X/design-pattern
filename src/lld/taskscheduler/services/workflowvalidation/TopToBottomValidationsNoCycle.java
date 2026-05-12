package lld.taskscheduler.services.workflowvalidation;

import lld.taskscheduler.model.Workflow;

public class TopToBottomValidationsNoCycle implements ValidationsStrategy{
    @Override
    public boolean validateWorkflowDef(Workflow workflow) {
        return false;
    }
}
