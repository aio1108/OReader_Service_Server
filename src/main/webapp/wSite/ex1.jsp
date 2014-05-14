<%-- Page ---------------------------------------------------------------------%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%-- Import -------------------------------------------------------------------%>
<%@ page import="java.util.*" %>
<%@ page import="org.jdom.*" %>
<%@ page import="org.jdom.xpath.XPath" %>
<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>

<%@ page import="com.hyweb.util.CommonUtil" %>
<%@ page import="com.hyweb.util.JDomUtil" %>
<%@ page import="com.hyweb.runner.*" %>
<%@ page import="com.hyweb.runner.util.*" %>

<%
	Map requestMap = request.getParameterMap();
	Document doc = generate(requestMap,session);
	Element rootE = doc.getRootElement();
	
	JDomUtil.addJdomContent(rootE, "showHF", "true");
	JDomUtil.addJdomContent(rootE, "contextPath", request.getContextPath());
	
	//System.out.println(JDomUtil.jdomToString(doc));
	
	String rootDir = getServletConfig().getServletContext().getRealPath("/");
	String xslPath = rootDir + "/wSite/runCase.xsl";
	
	String result = LsUtil.transform(doc, xslPath);
	out.println(result);
%>

<%!
public Document generate(Map request, HttpSession session) 
{
	Log LOGGER = LogFactory.getLog(this.getClass());
	
	String caseno = CommonUtil.getFirstParam("caseno", "", request);
	String caseXmlName = CommonUtil.getFirstParam("casename", "", request);
	
	Document outDoc = new Document(new Element("hpMain"));
	Element rootE = outDoc.getRootElement();
	
	CaseInput input = new CaseInput();
	//必要輸入欄位
	input.setUserId("TestUser");
	input.setCaseName(caseXmlName); //要執行哪個case xml
	input.setInstanceId(caseno);  //必需為唯一值（申辦案件文號）
	
	input.setRequest(request);
	input.setSession(session);
	input.setJdbcTemplate(null);
	
	try
	{
		CaseRunner caseRunner = new CaseRunner(input);
		CaseOutput output = caseRunner.run();
		
		String actionHtml = (String)output.get("actionHTML");
		if(actionHtml!=null)
		{
			JDomUtil.addJdomCdata(rootE, "actionHTML",actionHtml);	
		}
			
		Document caseInfoDoc = output.getCaseInfoDoc();
		if(caseInfoDoc!=null)
		{
			Element actionList = (Element) XPath.selectSingleNode(caseInfoDoc, "//run");
			if(actionList!=null)
			{
				outDoc.getRootElement().addContent((Element)actionList.clone());
			}
		}
		
		JDomUtil.addJdomContent(rootE, "returnCode", output.getReturnCode());
	
		
		LOGGER.debug("Return message="+ output.getReturnMessage());
		Map msgMap = output.getReturnMessage();
		Iterator it = null;
        if (msgMap instanceof OrderMap)
        {
        	it = ((OrderMap)msgMap).getOrderKey().iterator();
        }
        else
        {
        	it = msgMap.keySet().iterator();
        }
        
        Element msgE = JDomUtil.addJdomContent(rootE, "returnMessage");
        while (it.hasNext())
        {
            String key = it.next().toString();
            Object value = msgMap.get(key);
            Element e = JDomUtil.addJdomContent(msgE, "message", value.toString());
            e.setAttribute("name",key);
        }
        
        
        if(!output.isSuccess())
        {
	        JDomUtil.addJdomContent(rootE, "returnCodeDesc", ErrorCodeUtility.getCodeDesc(output.getReturnCode()) );
        }
		
        
        /*
		if(toUrl.length()>0 && !(toUrl.equals("undefined")))
		{
			JDomUtil.addJdomCdata(rootE, "redirect", toUrl);
		}
        */
	} 
	catch (Exception e)
	{
		e.printStackTrace();
		LOGGER.error(e);
	}
	
	
	//JDomUtil.addJdomContent(outDoc.getRootElement(), "msgInfo", msg);
    
	return outDoc;
}
%>