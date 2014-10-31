package org.wso2.carbon.rssmanager.core.workflow;

import org.wso2.carbon.registry.core.utils.UUIDGenerator;
import org.wso2.carbon.rssmanager.core.RSSTransactionManager;
import org.wso2.carbon.rssmanager.core.config.RSSConfigurationManager;
import org.wso2.carbon.rssmanager.core.config.RSSManagementRepository;
import org.wso2.carbon.rssmanager.core.dao.DatabaseDAO;
import org.wso2.carbon.rssmanager.core.dao.RSSDAO;
import org.wso2.carbon.rssmanager.core.dao.RSSDAOFactory;
import org.wso2.carbon.rssmanager.core.dao.exception.RSSDAOException;
import org.wso2.carbon.rssmanager.core.dao.impl.DatabaseDAOImpl;
import org.wso2.carbon.rssmanager.core.dao.impl.DatabaseUserDAOImpl;
import org.wso2.carbon.rssmanager.core.dao.impl.WorkflowDAOImpl;
import org.wso2.carbon.rssmanager.core.dao.util.EntityManager;
import org.wso2.carbon.rssmanager.core.dto.restricted.Database;
import org.wso2.carbon.rssmanager.core.dto.restricted.RSSInstance;
import org.wso2.carbon.rssmanager.core.dto.restricted.Workflow;
import org.wso2.carbon.rssmanager.core.environment.Environment;
import org.wso2.carbon.rssmanager.core.environment.EnvironmentManager;
import org.wso2.carbon.rssmanager.core.exception.RSSManagerException;
import org.wso2.carbon.rssmanager.core.internal.RSSManagerDataHolder;
import org.wso2.carbon.rssmanager.core.jpa.persistence.internal.JPAManagerUtil;
import org.wso2.carbon.rssmanager.core.jpa.persistence.internal.PersistenceManager;
import org.wso2.carbon.rssmanager.core.manager.AbstractRSSManager;
import org.wso2.carbon.rssmanager.core.manager.RSSManager;
import org.wso2.carbon.rssmanager.core.manager.adaptor.EnvironmentAdaptor;
import org.wso2.carbon.rssmanager.core.manager.adaptor.RSSManagerAdaptor;
import org.wso2.carbon.rssmanager.core.manager.adaptor.RSSManagerAdaptorImpl;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class WorkflowExecutor implements Serializable {
    protected String callbackURL;
    public abstract String getWorkflowType();

    private EnvironmentAdaptor getEnvironmentAdaptor() throws RSSManagerException {
        EnvironmentAdaptor adaptor =
                RSSConfigurationManager.getInstance().getRSSManagerEnvironmentAdaptor();
        if (adaptor == null) {
            throw new IllegalArgumentException("RSS Manager Environment Adaptor is not " +
                    "initialized properly");
        }
        return adaptor;
    }

    public void execute(Workflow workflow, Database database)  throws WorkflowException{

    }

    public void complete(Workflow workflow, Database database) throws WorkflowException{
        String environment=workflow.getEnvironmentName();
        if(WorkflowConstants.WORKFLOW_APPROVED.equals(workflow.getStatus())){
            database=workflow.getDatabaseId();
            database.setStatus("APPROVED");
            try {
                getEnvironmentAdaptor().updateDatabse(environment,database);
            } catch (RSSManagerException e) {
                e.printStackTrace();
            }
        }
        else if(WorkflowConstants.WORKFLOW_REJECT.equals(workflow.getStatus())){
            database=workflow.getDatabaseId();
        }
    }

    public abstract List<Workflow> getWorkflowDetails(String workflowStatus) throws WorkflowException;

    public String generateUUID(){
        String UUID = UUIDGenerator.generateUUID();
        return UUID;
    }

    public String getCallbackURL() {
        return callbackURL;
    }

    public void setCallbackURL(String callbackURL) {
        this.callbackURL = callbackURL;
    }

}

