<%-- Page ---------------------------------------------------------------------%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%-- Import -------------------------------------------------------------------%>
<%@ page import="java.util.*" %>
<%@ page import="org.jdom.*" %>
<%@ page import="org.jdom.xpath.XPath" %>
<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>
<%@ page import="org.springframework.jdbc.core.JdbcTemplate" %>

<%@ page import="com.hyweb.util.*" %>
<%@ page import="com.hyweb.runner.*" %>
<%@ page import="com.hyweb.runner.util.*" %>

<%
	Map requestMap = request.getParameterMap();
	Document doc = generate(requestMap,session);
	Element rootE = doc.getRootElement();
	
	String showHeaderFootrFlag = CommonUtil.getFirstParam("showHF", "true", requestMap);
	
	JDomUtil.addJdomContent(rootE, "showHF", showHeaderFootrFlag);
	JDomUtil.addJdomContent(rootE, "contextPath", request.getContextPath());
	
	//System.out.println(JDomUtil.jdomToString(doc));
	
	String rootDir = getServletConfig().getServletContext().getRealPath("/");
	String xslPath = rootDir + "/wSite/runAction.xsl";
	
	String result = LsUtil.transform(doc, xslPath);
	out.println(result);
%>

<%!public Document generate(Map request, HttpSession session) 
{
	Log LOGGER = LogFactory.getLog(this.getClass());

	String actionType = CommonUtil.getFirstParam("actionType", "com.hyweb.action.Form", request);
	
	
	Document outDoc = new Document(new Element("hpMain"));
	Element rootE = outDoc.getRootElement();
	
	
	ActionInput input = new ActionInput();
	//必要輸入欄位
	//input.put("name", actXmlName);
	input.setActionType(actionType);	
	input.setUserId("TestUser");
	input.setRequest(request);
	input.setSession(session);
	input.setJdbcTemplate( (JdbcTemplate)SpringContext.getBean("jdbcTemplate"));
	
	
	/*
	Map map = new Hashtable();
	map.put("ReceiveNo1", "123");
	map.put("ReceiveNo2", "4567890");
	map.put("ChineseName", "陳惠惠");
	
	input.put("formData", map);
	*/
	
	try
	{
		ActionRunner actionRunner = new ActionRunner(input);
		ActionOutput output = actionRunner.run();
		
		Document actionInfoDoc = output.getActionInfo();
		rootE.addContent(actionInfoDoc.getRootElement().cloneContent());
		
		Document actionOutputDoc = output.getActionOutputAsXML();
		JDomUtil.addJdomContent(rootE,"actionOutput",actionOutputDoc.getRootElement().cloneContent());
		/*
		String actionHtml = (String)output.get("result");
		if(actionHtml!=null)
		{
			JDomUtil.addJdomCdata(rootE, "actionHTML",actionHtml);	
		}
		
		JDomUtil.addJdomContent(rootE, "actionXml", actXmlName);
		JDomUtil.addJdomContent(rootE, "returnCode", output.getReturnCode());
		*/
		
	} 
	catch (Exception e)
	{
		e.printStackTrace();
		LOGGER.error(e);
	}
	
	
	//JDomUtil.addJdomContent(outDoc.getRootElement(), "msgInfo", msg);
    
	return outDoc;
}%>