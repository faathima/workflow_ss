package org.wso2.carbon.rssmanager.core.workflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.rssmanager.core.RSSTransactionManager;
import org.wso2.carbon.rssmanager.core.config.RSSManagementRepository;
import org.wso2.carbon.rssmanager.core.dao.RSSDAOFactory;
import org.wso2.carbon.rssmanager.core.dao.exception.RSSDAOException;
import org.wso2.carbon.rssmanager.core.dao.impl.DatabaseDAOImpl;
import org.wso2.carbon.rssmanager.core.dao.util.EntityManager;
import org.wso2.carbon.rssmanager.core.dto.restricted.Database;
import org.wso2.carbon.rssmanager.core.dto.restricted.Workflow;
import org.wso2.carbon.rssmanager.core.environment.dao.EnvironmentManagementDAOFactory;
import org.wso2.carbon.rssmanager.core.internal.RSSManagerDataHolder;
import org.wso2.carbon.rssmanager.core.jpa.persistence.internal.JPAManagerUtil;
import org.wso2.carbon.rssmanager.core.jpa.persistence.internal.PersistenceManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.List;
import java.util.Set;



public class DatabseCreationSimpleWorkflowExecutor  extends WorkflowExecutor {

    private static final Log log =
            LogFactory.getLog(DatabseCreationSimpleWorkflowExecutor.class);

    @Override
    public String getWorkflowType() {
        return WorkflowConstants.WF_TYPE_SS_DATABASE_CREATION;
    }

    public void execute(Workflow workflow, Database database) throws WorkflowException {
        if (log.isDebugEnabled()) {
            log.info("Executing Application creation Workflow..");
        }
            workflow.setStatus("APPROVED");
            workflow.setDescribtion("NO NEED PRIVILEGE USER APPROVAL");

     complete(workflow, database);

    }

    public void complete(Workflow workflow, Database database) throws WorkflowException {
        super.complete(workflow,database);
    }

    @Override
    public List<Workflow> getWorkflowDetails(String workflowStatus) throws WorkflowException {
        return null;
    }
}
