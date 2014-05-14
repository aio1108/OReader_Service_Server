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
	
	Log LOGGER = LogFactory.getLog(this.getClass());
	
	String actionType = com.hyweb.util.CommonUtil.getFirstParam("actionType", "com.hyweb.action.Form", requestMap);
		
	ActionInput input = new ActionInput();
	//必要輸入欄位
	//input.put("name", actXmlName);
	input.setActionType(actionType);	
	input.setUserId("TestUser");
	input.setRequest(requestMap);
	input.setSession(session);
	input.setJdbcTemplate( (JdbcTemplate)SpringContext.getBean("jdbcTemplate"));
	
	ActionOutput output = null;
	
	try
	{
		ActionRunner actionRunner = new ActionRunner(input);
		output = actionRunner.run();
	} 
	catch (Exception e)
	{
		e.printStackTrace();
		LOGGER.error(e);
	}
	
	String result = (String) output.getActionOutput().get("result");
	out.println(result);
%>