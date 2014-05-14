package com.hyweb.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.el.ExpressionEvaluatorImpl;
import org.apache.commons.lang.StringUtils;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;
import org.springframework.jdbc.core.JdbcTemplate;

import com.hyweb.runner.VariableContainer;
import com.hyweb.runner.util.LogicalException;
import com.hyweb.runner.util.LogicalAction;
import com.hyweb.runner.util.LsUtil;
import com.hyweb.runner.util.RmJDomUtil;
import com.hyweb.runner.util.ActionUtil;

public class ItemCheck extends LogicalAction
{	
	private static String XML_SPEC_NAME = "name";
	private static String INSTANCE_ID = "wpinno";
	private static String RESULT = "result";
	
	@Override
	public void validateParameter() throws LogicalException
	{
		// TODO Auto-generated method stub
		if(_inSpo.get(XML_SPEC_NAME)==null)
		{
			throw new LogicalException("Parameter 'name' should not be empty!");
		}
		if(_inSpo.get(INSTANCE_ID)==null)
		{
			throw new LogicalException("Parameter 'wpinno' should not be empty!");
		}
	}

	@Override
	public void excute() throws LogicalException, Exception
	{
		String xmlSpecName = (String)_inSpo.get(XML_SPEC_NAME);
		String instanceId = (String)_inSpo.get(INSTANCE_ID);
		
		Document doc = this.loadActionSpec(xmlSpecName);
		
		String result = "";
		String path = LsUtil.getSysValue(LsUtil.CONFIG_ACTION_PATH);
		
		Element rootE = doc.getRootElement();
		ActionUtil.includeFormProcess(rootE,_log);
		ActionUtil.includeItemCheckProcess(rootE,_log);		
			
		List savedList = ActionUtil.readCheckDataFromXML(instanceId, this.getActionId());
		if(savedList!=null)
		{
			rootE.removeChildren("checkList");
			for(int i=0;i<savedList.size();i++)
			{
				Element e = (Element)savedList.get(i);
				RmJDomUtil.addJdomElement(rootE,(Element)e.clone());
			}
		}
		else
		{
			VariableContainer _varContainer = ActionUtil.getActionVariableData(instanceId,rootE,this,_inSpo,_log);
			getXmlSpec(instanceId,rootE,_varContainer);
		}
		
		//step2.
		List fileList = XPath.selectNodes(rootE,"//refInfo/fileName");
		if(fileList.size()>0)
		{
			String folderPath = ActionUtil.getPhotoRealPath(instanceId);
			
			Iterator it = fileList.iterator();
			while(it.hasNext())
			{
				Element e = (Element)it.next();
				String refId = e.getAttributeValue("refId");
				String fileFolderPath = folderPath + File.separatorChar + refId;
				
				RmJDomUtil.addJdomCdata(e, "folderXml", ActionUtil.getFileListXmlStr(fileFolderPath,""));
			
				String folderUrl = ActionUtil.getPhotoUrl(instanceId) + "/" + refId;
				//folderUrl = java.net.URLEncoder.encode(folderUrl,"utf8");

				RmJDomUtil.addJdomCdata(e, "folderUrl", folderUrl);
			}
		}
		
		//_log.debug(JDomUtil.jdomToString(doc));
		
		//get xsl setting
		Element xslE = rootE.getChild("xsl");
		result = LsUtil.transform(doc, path+ xslE.getText());
		_outSpo.put(RESULT,result);
		
		this.setSuccessful(true);
	}
	
	public void eventHandler() throws LogicalException, Exception
	{
		String xmlSpecName = (String)_inSpo.get(XML_SPEC_NAME);
		String instanceId = (String)_inSpo.get(INSTANCE_ID);
		
		Document doc = this.loadActionSpec(xmlSpecName);
		if(doc==null) return;
		
		Element rootE = doc.getRootElement();
		
		Map request = this.getRequest();
		String event = LsUtil.getFirstParam("_event", "", request);
		String toUrl = LsUtil.getFirstParam("_toUrl", "", request);
		
		_log.debug("Event="+event);
		if(event.length()==0) return;
		
		VariableContainer _varContainer = ActionUtil.getActionVariableData(instanceId,rootE,this,_inSpo,_log);
		
		//add by Jimmy 2011/10/13
		//process include checkList for save to instance data XML.
		ActionUtil.includeItemCheckProcess(rootE,_log);
		
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
				List fieldList = XPath.selectNodes(rootE, "//field");
				String tableName = processE.getAttributeValue("table")==null? this.getActionId() : processE.getAttributeValue("table");
				
				String action = processE.getAttributeValue("action");
				if(StringUtils.isNotBlank(action))
				{
					if(action.equals("del"))
					{
						ActionUtil.delDataFromXML(instanceId, tableName);
					}
					else if(action.equals("add"))
					{
						ActionUtil.saveDataToXML(instanceId,request,tableName,fieldList,_varContainer);
						doSave(instanceId,rootE,_varContainer);
					}
				}
				else //預設為新增以向下相容
				{
					ActionUtil.saveDataToXML(instanceId,request,tableName,fieldList,_varContainer);
					doSave(instanceId, rootE,_varContainer);
				}
			}
			else if(type.equals("sql"))
			{
				//execSqlList.add(processE);
				ActionUtil.processSQL(processE,this.getJdbcTemplate(),_log,_varContainer,rootE);
			}
			else if(type.equals("upload"))
			{	
				List fileList = processE.getChildren("file");
				ActionUtil.saveUploadFile(instanceId,request,fileList,_log,_varContainer);
			}
			else if(type.equals("flow"))
			{
				//ActionUtil.execFlowService(instanceId, processE, _log, _varContainer);
			}
			else if(type.equals("service"))
			{	
				//_varContainer.setUserId(_inSpo.getUserId());
				ActionUtil.getDataSetFromService(processE, _varContainer, _log);
			}
		}
				
		this.setSuccessful(true);
	}
	
	private void doSave(String instanceId,Element rootE,VariableContainer varContainer) throws Exception
	{
		getXmlSpec(instanceId, rootE, varContainer);
		
		List checkList =XPath.selectNodes(rootE, "//checkList");
		ActionUtil.saveCheckDataToXML(instanceId,varContainer.getRequest(),this.getActionId(),checkList);
	}
	
	private void getXmlSpec(String instanceId, Element rootE, VariableContainer varContainer) throws Exception
	{
		List checkList = XPath.selectNodes(rootE, "//check");
		Iterator it = checkList.iterator();
		while(it.hasNext())
		{
			Element ce = (Element)it.next();	
			Element e = ce.getChild("desc");
			if(e==null) continue;
			
			String desc = e.getText();
			boolean addLabelTag = true;
			if(e.getAttribute("autoSetVarStyle")!=null && e.getAttributeValue("autoSetVarStyle").equalsIgnoreCase("N"))
			{
				addLabelTag = false;
			}
			ce.removeChild("desc");
			
			//進行變數轉換。
			desc = ActionUtil.resolveVariable(desc, varContainer,_log,addLabelTag);
			_log.debug("desc="+desc);
			RmJDomUtil.addJdomCdata(ce, "desc", desc);			
		}
		
		ActionUtil.initFormData(instanceId, this, varContainer, _inSpo, rootE, _log);
	}
}
