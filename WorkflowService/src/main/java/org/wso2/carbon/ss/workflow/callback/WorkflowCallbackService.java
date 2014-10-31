package org.wso2.carbon.ss.workflow.callback;

import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
public class WorkflowCallbackService{
private static final Log log = LogFactory.getLog(WorkflowCallbackService.class);
public void resumeEvent(String workflowReference,String status, String description ){
	String workflowReff =workflowReference;
	String wfStatus =status;
	String wfDescription= description;
	
	System.out.println("workflow Reffernce - "+workflowReff);
	System.out.println("workflow Satatus - "+wfStatus);
	System.out.println("workflow description - "+wfDescription);
	System.out.println("axis2 service for call back");
	String serviceEndpoint = null;
	String contentType=null;
	try{
	ServiceClient client=new ServiceClient();
	
     Options options = new Options();
     options.setAction("");
     options.setTo(new EndpointReference(serviceEndpoint));

     client.setOptions(options);

 } catch (AxisFault axisFault) {
     axisFault.printStackTrace();

 } catch (Exception e) {
     e.printStackTrace();

 }
	}

}