package com.hyweb.action;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSON;

import org.apache.commons.el.ExpressionEvaluatorImpl;
import org.apache.commons.io.FileUtils;
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
import tw.com.useful.runner.util.RmLogger;
import tw.com.useful.runner.util.SpringContext;

import com.hyweb.util.JDomUtil;
import com.hyweb.util.JSONUtil;

public class AppDataProvider extends LogicalAction
{
	private static String XML_SPEC_NAME = "name";
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
		Document doc = this.loadActionSpec(xmlSpecName);	
		
		String path = LsUtil.getSysValue(LsUtil.CONFIG_ACTION_PATH);
		Element rootE = doc.getRootElement();
		
		ActionUtil.initFormData(null, this, null, _inSpo, rootE, _log);
		
		Element outputE = (Element) XPath.selectSingleNode(rootE, "//output");
		if(outputE==null) return;
		
		
		String outputType = outputE.getAttributeValue("type")==null? "json" : outputE.getAttributeValue("type").toLowerCase();
		//outputE.removeAttribute("type");
		
		VariableContainer vc = ActionUtil.getActionVariableData(null,rootE,this,_inSpo,_log);
		
		List outElemList = outputE.getChildren();
		System.out.println("outElemList count="+outElemList.size());
		
		for(int i=0;i<outElemList.size();i++)
		{
			Element e = (Element)outElemList.get(i);
			replaceTextValue(e,vc);
			
			if(e.getQualifiedName().equals("data"))
			{
				//prcoess data from ds.
				String ds = e.getAttributeValue("ds")==null? "" : e.getAttributeValue("ds");
				if(ds.length()==0) continue;
				
				e.removeAttribute("ds");
				
				ExpressionEvaluatorImpl _evImpl = new ExpressionEvaluatorImpl();
				Object itemsObj = _evImpl.evaluate(ds,Object.class,vc,vc);
				if(itemsObj instanceof Document)
				{
					Document dsDoc = (Document)itemsObj;
					e.addContent((Element)dsDoc.getRootElement().clone());
				}			
			}
			else if(e.getQualifiedName().equals("for-each"))
			{
				e.getParent().removeContent(e);
					
				String var = e.getAttributeValue("var")==null? "" : e.getAttributeValue("var");
				String items = e.getAttributeValue("items")==null? "" : e.getAttributeValue("items");
				ExpressionEvaluatorImpl _evImpl = new ExpressionEvaluatorImpl();
				Object itemsObj = _evImpl.evaluate(items,Object.class,vc,vc);
				
				
								
				if(itemsObj instanceof List)
				{
					List itemsList = (List)itemsObj;
					
					Element templateE = (Element)e.getChildren().get(0);
					
					for(int j=0;j<itemsList.size();j++)
					{
						Map data = (Map)itemsList.get(j);
						vc.put(var, data);
						
						Element rowE = (Element)templateE.clone();
						List columnList = rowE.getChildren();
						for(int k=0;k<columnList.size();k++)
						{
							Element cE = (Element)columnList.get(k);
							replaceTextValue(cE,vc);
						}
						outputE.addContent(rowE);
					}
				}
			}
		}
		
		
		Document outDoc = new Document(new Element("hpMain"));
		
		List outputEList = outputE.getChildren();
		for(int i=0;i<outputEList.size();i++)
		{
			Element tempE = (Element)outputEList.get(i);
			
			RmJDomUtil.addJdomElement(outDoc.getRootElement(),(Element)tempE.clone());
		}
		
		result = JDomUtil.jdomToString(outDoc);
		if("json".equalsIgnoreCase(outputType))
		{
			JSONUtil xmlSerializer = new JSONUtil(); 
			xmlSerializer.setSkipNamespaces(true); 
			xmlSerializer.setTypeHintsCompatibility(true);
			xmlSerializer.setTypeHintsEnabled(false);
			JSON json = xmlSerializer.read( result );  
			result = json.toString(2);
		}
		
		_outSpo.put(RESULT,result);
		
		this.setSuccessful(true);
	}
	
	private void replaceTextValue(Element e, VariableContainer vc)
	{
		if(e.getText()==null) return;
		
		String text = e.getTextTrim();
		if(text.length()==0) return;
		
		e.setText( ActionUtil.resolveVariable(text, vc, _log,false) );
	}


	@Override
	public void eventHandler() throws LogicalException, Exception {
		// TODO Auto-generated method stub
		
	}

}
