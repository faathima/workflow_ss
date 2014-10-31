package org.wso2.carbon.rssmanager.core.dao.impl;

import org.wso2.carbon.rssmanager.core.dao.WorkflowDAO;
import org.wso2.carbon.rssmanager.core.dao.exception.RSSDAOException;
import org.wso2.carbon.rssmanager.core.dao.util.EntityManager;
import org.wso2.carbon.rssmanager.core.dto.restricted.RSSInstance;
import org.wso2.carbon.rssmanager.core.dto.restricted.Workflow;
import org.wso2.carbon.rssmanager.core.jpa.persistence.dao.AbstractEntityDAO;

/**
 * Created by msffayaza on 10/15/14.
 */
public class WorkflowDAOImpl extends AbstractEntityDAO<Integer,Workflow> implements WorkflowDAO {
    private EntityManager entityManager;

    public WorkflowDAOImpl(EntityManager entityManager) {

        super(entityManager.getJpaUtil().getJPAEntityManager());
        this.entityManager = entityManager;
    }

    public void addWorkflow(String environmentName, RSSInstance rssInstance, Workflow workflow,
                            int tenantId) throws RSSDAOException {
        workflow.setTenantId(tenantId);
        super.insert(workflow);
    }

    @Override
    public void addWorkflow(Workflow workflow, int tenantId) throws RSSDAOException{
        //System.out.println(workflow.getCallbackURL());
     //   System.out.println(workflow.getStatus());
      //  System.out.println(workflow.getCreatedTime());
        System.out.println(workflow.getTenantId());
       // System.out.println(workflow.getWorkflowExternalReference());

        workflow.setTenantId(tenantId);
        super.saveOrUpdate(workflow);
    }

    private EntityManager getEntityManager() {
        return entityManager;
    }

}
