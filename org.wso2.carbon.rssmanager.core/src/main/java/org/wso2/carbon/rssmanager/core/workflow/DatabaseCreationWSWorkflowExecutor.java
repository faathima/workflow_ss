package org.wso2.carbon.rssmanager.core.workflow;

import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.rssmanager.core.dto.restricted.Database;
import org.wso2.carbon.rssmanager.core.dto.restricted.Workflow;
import org.wso2.carbon.rssmanager.core.internal.RSSManagerDataHolder;

import javax.xml.stream.XMLStreamException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseCreationWSWorkflowExecutor extends WorkflowExecutor {
    private String serviceEndpoint;

    private String username;

    private String password;

    private String contentType;

    private static final Log log = LogFactory.getLog(DatabaseCreationWSWorkflowExecutor.class);

    @Override
    public String getWorkflowType() {
        return WorkflowConstants.WF_TYPE_SS_DATABASE_CREATION;
    }

    @Override
    public void execute(Workflow workflow , Database database) throws WorkflowException {
        if (log.isDebugEnabled()) {
            log.info("Executing Application creation Workflow..");
        }
        // super.execute(workflow);
            workflow.setStatus("CREATED");
            workflow.setDescribtion("WAITING FOR PRIVILEGE USER APPROVAL");

            try {

           String contentType = "application/soap+xml; charset=UTF-8";
                ServiceClient client = new ServiceClient(RSSManagerDataHolder.getContextService().getClientConfigContext(), null);
                Options options = new Options();
                options.setAction("http://workflow.createdb.ss.carbon.wso2.org/initiate");
                options.setTo(new EndpointReference(serviceEndpoint));

                if (contentType != null) {
                    options.setProperty(Constants.Configuration.MESSAGE_TYPE, contentType);
                } else {
                    options.setProperty(Constants.Configuration.MESSAGE_TYPE,
                            HTTPConstants.MEDIA_TYPE_APPLICATION_XML);
                }

                HttpTransportProperties.Authenticator auth = new HttpTransportProperties.Authenticator();

                if (username != null && password != null) {
                    auth.setUsername(username);
                    auth.setPassword(password);
                    auth.setPreemptiveAuthentication(true);
                    List<String> authSchemes = new ArrayList<String>();
                    authSchemes.add(HttpTransportProperties.Authenticator.BASIC);
                    auth.setAuthSchemes(authSchemes);
                    options.setProperty(org.apache.axis2.transport.http.HTTPConstants.AUTHENTICATE,
                            auth);
                    options.setManageSession(true);
                }
                client.setOptions(options);
     String payload =
                        "<wor:CreateDBApprovalWorkFlowProcessRequest xmlns:wor=\"http://workflow.createdb.ss.carbon.wso2.org\">\n"
                                + "        <wor:DatabaseName>$1</wor:DatabaseName>\n"
                                +"         <wor:DBSInstanceName>$2</wor:DBSInstanceName>\n"
                                +"         <wor:Environment>$3</wor:Environment>\n"
                                + "        <wor:description>$4</wor:description>\n"
                                + "        <wor:workflowExternalRef>$5</wor:workflowExternalRef>\n"
                                + "        <wor:callBackURL>$6</wor:callBackURL>\n"
                                + "</wor:CreateDBApprovalWorkFlowProcessRequest>";


                payload = payload.replace("$1",workflow.getDbName());
                payload = payload.replace("$2", workflow.getRssInstanceName());
                payload=payload.replace("$3", workflow.getEnvironmentName());
                payload=payload.replace("$4", workflow.getDescribtion());
                payload=payload.replace("$5",workflow.getWfRefference());
                payload=payload.replace("$6",workflow.getCallbackURL());

                client.fireAndForget(AXIOMUtil.stringToOM(payload));

            } catch (AxisFault axisFault) {
                axisFault.printStackTrace();

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    @Override
    public void complete(Workflow workflow, Database database) throws WorkflowException {
        super.complete(workflow,database);
    }

    @Override
    public List<Workflow> getWorkflowDetails(String workflowStatus) throws WorkflowException {
        return null;
    }

    public String getServiceEndpoint() {
        return serviceEndpoint;
    }

    public void setServiceEndpoint(String serviceEndpoint) {
        this.serviceEndpoint = serviceEndpoint;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

}


