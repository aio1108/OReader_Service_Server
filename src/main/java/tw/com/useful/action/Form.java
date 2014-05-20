package tw.com.useful.action;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.jdbc.core.JdbcTemplate;

import tw.com.useful.runner.VariableContainer;
import tw.com.useful.runner.util.ActionUtil;
import tw.com.useful.runner.util.LogicalAction;
import tw.com.useful.runner.util.LogicalException;
import tw.com.useful.runner.util.LsUtil;
import tw.com.useful.runner.util.RmJDomUtil;
import tw.com.useful.runner.util.SpringContext;

public class Form extends LogicalAction
{
	private static String XML_SPEC_NAME = "name";
	private static String INSTANCE_ID = "wpinno";
	private static String RESULT = "result";
		
	@Override
	public void validateParameter() throws LogicalException
	{
		// TODO Auto-generated method stub
		System.out.println("validate parameter..."+_inSpo.get(XML_SPEC_NAME));
		if(_inSpo.get(XML_SPEC_NAME)==null || _inSpo.get(XML_SPEC_NAME).toString().equals(""))
		{
			throw new LogicalException("AE0005");
		}
		
	}

	@Override
	public void excute() throws LogicalException, Exception
	{
		String result = "";
		
		String xmlSpecName = (String)_inSpo.get(XML_SPEC_NAME);
		String instanceId = (String)_inSpo.get(INSTANCE_ID);
		
		Document doc = this.loadActionSpec(xmlSpecName);	
		
		String path = LsUtil.getSysValue(LsUtil.CONFIG_ACTION_PATH);
		Element rootE = doc.getRootElement();
		
		ActionUtil.includeFormProcess(rootE,_log);
		
		ActionUtil.initFormData(instanceId, this, null, _inSpo, rootE, _log);
		
		//get xsl settinggenform.xsl
		Element xslE = rootE.getChild("xsl");
		if(xslE!=null)
		{
			String xslPath = LsUtil.getSysValue(LsUtil.CONFIG_ACTIONXSL_PATH);
			System.out.println("XSL Path="+xslPath);
			result = LsUtil.transform(doc, xslPath + xslE.getText());	
		}
		else
		{
			result = RmJDomUtil.jdomToString(doc);
		}	
		//System.out.println( RmJDomUtil.jdomToString(doc));
		_outSpo.put(RESULT,result);

		this.setSuccessful(true);
	}

	public void eventHandler() throws LogicalException,Exception
	{
		try
		{
		String xmlSpecName = (String)_inSpo.get(XML_SPEC_NAME);
		String instanceId = (String)_inSpo.get(INSTANCE_ID);
		
		Document doc = this.loadActionSpec(xmlSpecName);
		
		Element rootE = doc.getRootElement();
		
		Map request = this.getRequest();
		String event = LsUtil.getFirstParam("_event", "", request);
		String toUrl = LsUtil.getFirstParam("_toUrl", "", request);
		
		_log.debug("Event="+event);
		if(event.length()==0) return;
		
		ActionUtil.includeFormProcess(rootE,_log);
		
		VariableContainer _varContainer = ActionUtil.getActionVariableData(instanceId,rootE,this,_inSpo,_log);
		
		
		Element e = (Element)XPath.selectSingleNode(rootE, "//eventHandler/btnEvent[@name='"+event+"']");
		List execList = XPath.selectNodes(e, "execute[@process=true()]");
		
		List execSqlList = new ArrayList();
		for(int i=0;i<execList.size();i++)
		{
			Element execE = (Element)execList.get(i);
			
			String process = execE.getAttributeValue("process");
			Element processE = (Element)XPath.selectSingleNode(rootE, "//process[@id='"+ process +"']");
			
			if(processE==null) continue; 
			
			//check type
			String type = processE.getAttributeValue("type");
			if(type.equals("xml"))
			{
				List fieldList = XPath.selectNodes(rootE, "//fieldList/field");
				String tableName = processE.getAttributeValue("table")==null? this.getActionId() : processE.getAttributeValue("table");
				ActionUtil.saveDataToXML(instanceId,request,tableName,fieldList, _varContainer);
			}
			else if(type.equals("sql"))
			{
				//execSqlList.add(processE);	
				ActionUtil.processSQL(processE,this.getJdbcTemplate(),_log,_varContainer,rootE);
			}
			else if(type.equals("business_logic"))
			{
				//ActionUtil.processBusinessLogic(rootE);
			}
			else if(type.equals("upload"))
			{	
				List fileList = processE.getChildren("file");
				ActionUtil.saveUploadFile(instanceId,request,fileList,_log,_varContainer);
			}
			else if(type.equals("service"))
			{	
				_varContainer.setUserId(this._userId);
				ActionUtil.getDataSetFromService(processE, _varContainer, _log);
			}
			else if(type.equals("flow"))
			{
				
			}
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
			
		this.setSuccessful(true);
	}
}
