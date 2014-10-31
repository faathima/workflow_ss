<%@ page import="org.apache.axiom.om.OMElement" %>
<%@ page import="javax.xml.namespace.QName" %>
<p>
        <%
	String description ="";
	String workflowExternalRef="";

        OMElement responseElement = (OMElement) request.getAttribute("taskInput");
	String ns = "http://workflow.createdb.ss.carbon.wso2.org";
        if (responseElement != null) { 

            OMElement desctibionElement = responseElement.getFirstChildWithName(new QName(ns, "description"));

            		if(desctibionElement !=null){
             		   description = desctibionElement.getText();
        		    }
            OMElement workflowExternalRefElement = responseElement.getFirstChildWithName(new QName(ns, "workflowExternalRef"));

            if(workflowExternalRefElement !=null){
                workflowExternalRef = workflowExternalRefElement.getText();
            }
	   
        }


    	%>

<script type="text/javascript">
createTaskOutput = function() {checked="true"
    var workflowExternalRef ="<%=workflowExternalRef%>";
    var description ="<%=description%>";
    var outputVal = getCheckedRadio();
    
		return '<sch:CreateDBApprovalResponse xmlns:sch="http://workflow.createdb.ss.carbon.wso2.org">'
		+'	<sch:status>'+ outputVal +'</sch:status>'
		+'	<sch:description>'+  description+'</sch:description>'
		+'	<sch:workflowExternalRef>' + workflowExternalRef +'</sch:workflowExternalRef>'
		+'	</sch:CreateDBApprovalResponse>';
	
};

getCheckedRadio = function () {
      var radioButtons = document.getElementsByName("responseRadio");
      for (var x = 0; x < radioButtons.length; x ++) {
        if (radioButtons[x].checked) {
          return radioButtons[x].value;
        }
      }
    };
</script>

<p>
<form>
<table border="0">
    <tr>
	<td>
		<input type="radio" name="responseRadio" id="responseRadio1" value="approve"/> Approve
		<input type="radio" name="responseRadio" id="responseRadio2" value="disapprove"/> Disapprove 
    </td>
    </tr>

</table>
</form>

<table border="0">
    <tr>
        <td>description</td>
        <td><%=description%>
        </td>
    </tr>
    <tr>
        <td>workflowExternalRef</td>
        <td><%=workflowExternalRef%>
        </td>
    </tr>
  
    
</table>

</p>
