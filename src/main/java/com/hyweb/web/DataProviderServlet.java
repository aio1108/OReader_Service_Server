package com.hyweb.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JSONString;

import org.jdom.Document;
import org.springframework.jdbc.core.JdbcTemplate;
import org.xml.sax.SAXException;

import com.hyweb.runner.ActionRunner;
import com.hyweb.runner.ServiceRunner;
import com.hyweb.runner.util.ActionInput;
import com.hyweb.runner.util.ActionOutput;
import com.hyweb.runner.util.LogicalService;
import com.hyweb.runner.util.OrderMap;
import com.hyweb.runner.util.RmLogger;
import com.hyweb.runner.util.ServiceInput;
import com.hyweb.runner.util.ServiceOutput;
import com.hyweb.runner.util.SpringContext;
import com.hyweb.runner.util.XmlUtil;
import com.hyweb.util.CommonUtil;
import com.hyweb.util.JDomUtil;



/**
 * Servlet implementation class ActionServiceServlet
 */
public class DataProviderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DataProviderServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		RmLogger _log = RmLogger.getRmLogger("DataProviderServlet");
		_log.debug("DataProviderServlet - [start]");
		
		Map requestMap = request.getParameterMap();
         
        String actionType = "com.hyweb.action.AppDataProvider";
        
        
        ActionInput input = new ActionInput();
     	//必要輸入欄位
     	input.setActionType(actionType);	
     	input.setUserId("TestUser");
     	input.setRequest(requestMap);
     	input.setSession(request.getSession());
     	//input.setJdbcTemplate( (JdbcTemplate)SpringContext.getBean("jdbcTemplate"));
     	
     	try
     	{
     		ActionRunner actionRunner = new ActionRunner(input);
     		ActionOutput output = actionRunner.run();
     		
     		Map map = output.getActionOutput();
     		String result = map.get("result")==null? "" : map.get("result").toString();
     		
     		//Document actionInfoDoc = output.getActionInfo();
     		//rootE.addContent(actionInfoDoc.getRootElement().cloneContent());
     		
     		//Document actionOutputDoc = output.getActionOutputAsXML();
     		
     		//System.out.println("*********"+ JDomUtil.jdomToString(actionOutputDoc));
     		//actionOutputDoc.getRootElement().getChild("")
     		//JDomUtil.addJdomContent(rootE,"actionOutput",actionOutputDoc.getRootElement().cloneContent());
     		
     		 boolean isJSonTxt = false;
     		 if(result.startsWith("{")) isJSonTxt = true;
     		 
     		
     		 
     		 
			 if(isJSonTxt)
			 {
				 String callback = request.getParameter("callback")==null? "" : request.getParameter("callback").toString();
	     		 System.out.println("callback =============="+ callback);
				 if(callback.length()>0)  //for JSONP call
				 {
					 response.setContentType("application/javascript;charset=UTF-8");
					 result = callback + "(" + result + ")";
				 }
				 else
				 {
					 response.setContentType("application/json;charset=UTF-8");
				 }
			 } 
			 else
			 {
				 response.setContentType("text/xml;charset=utf-8");
			 }     
			 
			 response.addHeader("Access-Control-Allow-Origin", "*");
			 
			 PrintWriter pw = response.getWriter();
			 pw.write(result);
			 pw.flush();
			 pw.close();
        	 
     	} 
     	catch (Exception e)
     	{
     		e.printStackTrace();
     		_log.error(e);
     	} 
	}
}
