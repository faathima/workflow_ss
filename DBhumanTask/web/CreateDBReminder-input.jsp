<%@ page import="org.apache.axiom.om.OMElement" %>
<%@ page import="javax.xml.namespace.QName" %>
<p>
         <%
        String DBname = "";
        String DBID = "";

        OMElement requestElement = (OMElement) request.getAttribute("taskInput");
        String ns = "http://workflow.createdb.ss.carbon.wso2.org";

        if (requestElement != null) {

            OMElement DBnameElement = requestElement.getFirstChildWithName(new QName(ns, "DatabaseName"));

            if(DBnameElement !=null){
                DBname = DBnameElement.getText();
            }

            OMElement DBSInstanceNameElement = requestElement.getFirstChildWithName(new QName(ns, "DBSInstanceName"));

            if(DBSInstanceName !=null){
                DBSInstance= DBIDElement.getText();
            }
        }
<table border="0">
    <tr>
        <td>DB Name</td>
        <td><%=DBname%>
        </td>
    </tr>
    <tr>
        <td>DB Id</td>
        <td><%=DBSInstance%>
        </td>
    </tr>
    
</table>

</p>
