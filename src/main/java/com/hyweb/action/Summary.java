package com.hyweb.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;
import org.springframework.jdbc.core.JdbcTemplate;

import tw.com.useful.runner.VariableContainer;
import tw.com.useful.runner.util.ActionUtil;
import tw.com.useful.runner.util.LogicalAction;
import tw.com.useful.runner.util.LogicalException;
import tw.com.useful.runner.util.LsUtil;
import tw.com.useful.runner.util.RmJDomUtil;

public class Summary extends LogicalAction
{	
	private static String XML_SPEC_NAME = "name";
	private static String INSTANCE_ID = "wpinno";
	private static String RESULT = "result";
	
	@Override
	public void validateParameter() throws LogicalException
	{
		// TODO Auto-generated method stub

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
		
		ActionUtil.initFormData(instanceId, this, null,_inSpo, rootE, _log);
		
		//process reference check list
		
		Element e = ActionUtil.readSummaryDataFromXML(instanceId, this.getActionId());
		if(e!=null)
		{
			rootE.removeChild("summaryList");
			rootE.addContent((Element)e.clone());
		}
	
		Map refMap = new Hashtable();	
		List summaryList = XPath.selectNodes(rootE, "//summary");
		
		_log.debug("summary list ="+summaryList.size());
		for(int i=0;i<summaryList.size();i++)
		{
			Element sE = (Element)summaryList.get(i);
			List detailList = XPath.selectNodes(sE, "detail");
			for(int j=0;j<detailList.size();j++)
			{
				//<detail refId="a3" checkListId="c1" />
				Element detailE = (Element)detailList.get(j);
				String refId = detailE.getAttributeValue("refId");
				String checkListId = detailE.getAttributeValue("checkListId");
				
				List list = (List)refMap.get(refId);
				//_log.debug("refId="+refId + " list = "+list);
				if(list==null)
				{
					list = ActionUtil.readCheckDataFromXML(instanceId, refId);
					if(list!=null)
					{
						refMap.put(refId, list);
					}
				}
				
				if(list==null) continue;
				
				for(int k=0;k<list.size();k++)
				{
					Element cE = (Element)list.get(k);
					String cId = cE.getAttributeValue("id");
					if(!cId.equals(checkListId)) continue;
					
					sE.addContent((Element)cE.clone());
					break;
				}
			}
		}
	

		//_log.debug("xml ="+RmJDomUtil.jdomToString(doc));
		
		//get xsl setting
		Element xslE = rootE.getChild("xsl");
		if(xslE!=null)
		{
			result = LsUtil.transform(doc, path+ xslE.getText());	
		}
		else
		{
			result = RmJDomUtil.jdomToString(doc);
		}
		
		_outSpo.put(RESULT,result);

		this.setSuccessful(true);
	}
	
	public void eventHandler() throws Exception
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
		
		VariableContainer _varContainer = ActionUtil.getActionVariableData(instanceId,rootE,this,_inSpo,_log);
		
		ActionUtil.includeFormProcess(rootE,_log);
		
		Element e = (Element)XPath.selectSingleNode(rootE, "//eventHandler/btnEvent[@name='"+event+"']");
		List execList = XPath.selectNodes(e, "execute[@process=true()]");
		List execSqlList = new ArrayList();
		_log.debug("exec list ="+execList.size());
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
				ActionUtil.saveDataToXML(instanceId,request,tableName,fieldList,_varContainer);
				
				saveCheckListToXML(instanceId,request,rootE);
			}
			else if(type.equals("sql"))
			{
				//execSqlList.add(processE);
				ActionUtil.processSQL(processE,this.getJdbcTemplate(),_log, _varContainer,rootE);
			}
			else if(type.equals("flow"))
			{
				//ActionUtil.execFlowService(instanceId, processE, _log, _varContainer);
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
		}
		
		this.setSuccessful(true);
	}
	
	private void saveCheckListToXML(String instanceId, Map request,Element rootE) throws LogicalException, JDOMException
	{
		List checkList =XPath.selectNodes(rootE, "//summary");
		ActionUtil.saveSummaryDataToXML(instanceId,request,this.getActionId(),checkList);
	}
}
