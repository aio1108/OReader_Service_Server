package com.hyweb.action.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

import tw.com.useful.runner.util.LogicalService;

public class GIPDataConvertService extends LogicalService
{
	public String getNodeValue()
	{
		String result = "";
		Document doc = (Document)this.getInputParameter("doc");
		String xPath = (String)this.getInputParameter("xPath","");
		System.out.println("xPath="+xPath);
		
		try {
			Element e = (Element)XPath.selectSingleNode(doc.getRootElement(),xPath);
			if(e!=null)
			{
				result = e.getText();
			}
			
			System.out.println("Element = "+e);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	public Document getLPNodes() throws JDOMException
	{
		Document result = new Document();
		Document doc = (Document)this.getInputParameter("doc");
		String xPath = (String)this.getInputParameter("xPath","");
		System.out.println("xPath="+xPath);
		try {
			Element e = (Element)XPath.selectSingleNode(doc.getRootElement(), xPath);
			if(e!=null)
			{
				result = new Document((Element) e.detach());
			}
			
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	public Document getCPNodes() throws JDOMException
	{
		Document result = new Document();
		Document doc = (Document)this.getInputParameter("doc");
		String xPath = (String)this.getInputParameter("xPath","");
		System.out.println("xPath="+xPath);
		try {
			Element e = (Element)XPath.selectSingleNode(doc.getRootElement(), xPath);
			if(e!=null)
			{
				result = new Document((Element) e.detach());
			}
			
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	public Document getNPNodes() throws JDOMException
	{
		Document result = new Document();
		Document doc = (Document)this.getInputParameter("doc");
		String xPath = (String)this.getInputParameter("xPath","");
		System.out.println("xPath="+xPath);
		try {
			Element e = (Element)XPath.selectSingleNode(doc.getRootElement(), xPath);
			if(e!=null)
			{
				result = new Document((Element) e.detach());
			}
			
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
}
