package org.wso2.carbon.rssmanager.core.workflow;

import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.rssmanager.core.dto.restricted.Workflow;

import javax.cache.Cache;
import javax.cache.Caching;

public class WorkflowExecutorFactory {

    private static final Log log = LogFactory.getLog(WorkflowExecutorFactory.class);

    private static WorkflowExecutorFactory instance;

    private WorkflowExecutorFactory() {
    }

    public static WorkflowExecutorFactory getInstance() {
        if (instance == null) {
            instance = new WorkflowExecutorFactory();
        }
        return instance;
    }

    public TenantWorkflowConfigHolder getWorkflowConfigurations() throws WorkflowException{
            TenantWorkflowConfigHolder configHolder = new TenantWorkflowConfigHolder();
                configHolder.load();
                return configHolder;
    }

    private static void handleException(String msg) throws WorkflowException {
        log.error(msg);
        throw new WorkflowException(msg);
    }

    public WorkflowExecutor getWorkflowExecutor(String workflowExecutorType) throws WorkflowException {
        TenantWorkflowConfigHolder holder = null;
        try {
            holder = this.getWorkflowConfigurations();
            if (holder != null) {
                return holder.getWorkflowExecutor(workflowExecutorType);
            }
        } catch (WorkflowException e) {
            handleException("Error while creating WorkFlowDTO for " + workflowExecutorType);
        }
        return null;
    }

   }
