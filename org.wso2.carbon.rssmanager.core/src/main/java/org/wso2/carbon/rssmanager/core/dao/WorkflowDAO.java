package org.wso2.carbon.rssmanager.core.dao;

import org.wso2.carbon.rssmanager.core.dao.exception.RSSDAOException;
import org.wso2.carbon.rssmanager.core.dto.restricted.Workflow;
import org.wso2.carbon.rssmanager.core.jpa.persistence.dao.EntityBaseDAO;

/**
 * Created by msffayaza on 10/15/14.
 */
public interface WorkflowDAO extends EntityBaseDAO<Integer, Workflow> {

    /**
     * Method to add workflow information to the RSS meta data repository.
     */
    void addWorkflow(Workflow workflow, int tenantId) throws RSSDAOException;

    /**
     * Method to remove workflow configuration information from RSS metadata repository.
     */
    // @Deprecated
    //  void removeWorkflow() throws WorkflowException;




}
