package org.wso2.carbon.rssmanager.core.workflow;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.rssmanager.common.RSSManagerConstants;
import org.wso2.carbon.rssmanager.core.internal.RSSManagerDataHolder;
import org.wso2.carbon.rssmanager.core.util.RSSManagerUtil;
import org.wso2.carbon.utils.CarbonUtils;
import org.wso2.securevault.SecretResolver;
import org.wso2.securevault.SecretResolverFactory;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class TenantWorkflowConfigHolder implements Serializable {

    private static final Log log = LogFactory.getLog(TenantWorkflowConfigHolder.class);

    private static final QName PROP_Q = new QName("Property");

    private static final QName ATT_NAME = new QName("name");

    private Map<String, WorkflowExecutor> workflowExecutorMap;

    public TenantWorkflowConfigHolder(){
    }

    public WorkflowExecutor getWorkflowExecutor(String workflowExecutorType){
        return workflowExecutorMap.get(workflowExecutorType);
    }

    public void load() throws WorkflowException{
        workflowExecutorMap = new ConcurrentHashMap<String, WorkflowExecutor>();

        try {
            String workflowConfigXMLPath = CarbonUtils.getCarbonConfigDirPath() +
                    File.separator + "etc" + File.separator +
                    RSSManagerConstants.WORKFLOW_CONFIG_XML_NAME;

            StAXOMBuilder builder = new StAXOMBuilder(workflowConfigXMLPath);

            OMElement workflowExtensionsElem = builder.getDocument().getFirstChildWithName(
                    new QName(WorkflowConstants.WORKFLOW_EXTENSIONS));

            OMElement workflowElem = workflowExtensionsElem.getFirstChildWithName(
                    new QName(WorkflowConstants.DATABASE_CREATION));
            String executorClass = workflowElem.getAttributeValue(new QName("executor"));
            Class clazz = TenantWorkflowConfigHolder.class.getClassLoader().loadClass(executorClass);
            WorkflowExecutor workFlowExecutor = (WorkflowExecutor)clazz.newInstance();
            loadProperties(workflowElem, workFlowExecutor);
            workflowExecutorMap.put(WorkflowConstants.WF_TYPE_SS_DATABASE_CREATION, workFlowExecutor);
        }catch (Exception e){

        }}

    private void loadProperties(OMElement executorElem, Object workflowClass) throws WorkflowException {

        for (Iterator it = executorElem.getChildrenWithName(PROP_Q); it.hasNext(); ) {
            OMElement propertyElem = (OMElement) it.next();
            String propName = propertyElem.getAttribute(ATT_NAME).getAttributeValue();
            if (propName == null) {
                handleException(
                        "An Executor class property must specify the name attribute");
            } else {
                OMNode omElt = propertyElem.getFirstElement();
                if (omElt != null) {
                    setInstanceProperty(propName, omElt, workflowClass);
                } else if (propertyElem.getText() != null) {
                    String value = propertyElem.getText();
                    setInstanceProperty(propName, value, workflowClass);
                } else {

                    handleException("An Executor class property must specify " +
                            "name and text value, or a name and a child XML fragment");
                }
            }
        }
    }

    public void setInstanceProperty(String name, Object val, Object obj) throws WorkflowException {

        String mName = "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
        Method method;

        try {
            Method[] methods = obj.getClass().getMethods();
            boolean invoked = false;

            for (Method method1 : methods) {
                if (mName.equals(method1.getName())) {
                    Class[] params = method1.getParameterTypes();
                    if (params.length != 1) {
                        handleException("Did not find a setter method named : " + mName +
                                "() that takes a single String, int, long, float, double ," +
                                "OMElement or boolean parameter");
                    } else if (val instanceof String) {
                        String value = (String) val;
                        if (String.class.equals(params[0])) {
                            method = obj.getClass().getMethod(mName, String.class);
                            method.invoke(obj, new String[]{value});
                        } else if (int.class.equals(params[0])) {
                            method = obj.getClass().getMethod(mName, int.class);
                            method.invoke(obj, new Integer[]{new Integer(value)});
                        } else if (long.class.equals(params[0])) {
                            method = obj.getClass().getMethod(mName, long.class);
                            method.invoke(obj, new Long[]{new Long(value)});
                        } else if (float.class.equals(params[0])) {
                            method = obj.getClass().getMethod(mName, float.class);
                            method.invoke(obj, new Float[]{new Float(value)});
                        } else if (double.class.equals(params[0])) {
                            method = obj.getClass().getMethod(mName, double.class);
                            method.invoke(obj, new Double[]{new Double(value)});
                        } else if (boolean.class.equals(params[0])) {
                            method = obj.getClass().getMethod(mName, boolean.class);
                            method.invoke(obj, new Boolean[]{Boolean.valueOf(value)});
                        } else {
                            continue;
                        }
                    } else if (val instanceof OMElement && OMElement.class.equals(params[0])) {
                        method = obj.getClass().getMethod(mName, OMElement.class);
                        method.invoke(obj, new OMElement[]{(OMElement) val});
                    } else {
                        continue;
                    }
                    invoked = true;
                    break;
                }
            }

            if (!invoked) {
                handleException("Did not find a setter method named : " + mName +
                        "() that takes a single String, int, long, float, double " +
                        "or boolean parameter");
            }

        } catch (Exception e) {
            handleException("Error invoking setter method named : " + mName +
                    "() that takes a single String, int, long, float, double " +
                    "or boolean parameter", e);
        }
    }

    private static void handleException(String msg) throws WorkflowException{
        log.error(msg);
        throw new WorkflowException(msg);
    }

    private static void handleException(String msg, Exception e) throws WorkflowException{
        log.error(msg, e);
        throw new WorkflowException(msg, e);
    }
}
