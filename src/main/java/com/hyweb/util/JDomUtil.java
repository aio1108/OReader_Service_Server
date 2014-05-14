package com.hyweb.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletResponse;

import org.jdom.Attribute;
import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

/**
 * XML DOM 操作類別
 *
 */
public class JDomUtil{

    public static Document buildXml(String path)throws IOException,JDOMException{        
		SAXBuilder builder = new SAXBuilder();
		return builder.build(path);		
    }	
    
    public static Document buildXml(java.io.InputStream in)throws IOException,JDOMException{
        SAXBuilder builder = new SAXBuilder();
        return builder.build(in);
    }	
    
    public static Document buildXml(java.io.Reader characterStream)throws IOException,JDOMException{
        SAXBuilder builder = new SAXBuilder();
        return builder.build(characterStream);
    }	    
    

    //----------------------------//

    public static Element addJdomContent(org.jdom.Element node, String name) {
    	org.jdom.Element subnode = new org.jdom.Element(name);    	
    	node.addContent(subnode);
    	
    	return subnode;
    }    
    
    public static Element addJdomContent(org.jdom.Element node, String name,
    		String value) {
    	org.jdom.Element subnode = new org.jdom.Element(name);
    	subnode.setText(value);
    	node.addContent(subnode);
    	
    	return subnode;
    }

    public static Element addJdomContent(org.jdom.Element node, String name,
    		List values) {
    	org.jdom.Element subnode = new org.jdom.Element(name);
    	subnode.addContent(values);
    	node.addContent(subnode);
    	
    	return subnode;
    }

    public static Element addJdomCdata(org.jdom.Element node, String name,
    		String value) {
    	org.jdom.Element subnode = new org.jdom.Element(name);
    	CDATA cdata = new CDATA(value);
    	subnode.addContent(cdata);
    	node.addContent(subnode);
    	
    	return subnode;
    }    

    public static Attribute addJdomAttribute(org.jdom.Element node, String name,
    		String value) {
    	org.jdom.Attribute subnode = new org.jdom.Attribute(name,value);
    	node.setAttribute(subnode);
    	
    	return subnode;
    }
    
    public static Element addJdomAttributeElement(org.jdom.Element node, String name,
    		String value) {
    	org.jdom.Attribute subnode = new org.jdom.Attribute(name,value);
    	node.setAttribute(subnode);
    	
    	return node;
    }
    
    //---------------------------//

    public static void jdomOutputXml(Document outdoc, OutputStream  out)
    throws IOException {

    	org.jdom.output.Format f = Format.getPrettyFormat();
    	f.setEncoding("UTF-8");
    	XMLOutputter xmlout = new XMLOutputter(f);

    	xmlout.output(outdoc, out);

    }
    
    public static void jdomOutputXml(Document outdoc,ServletResponse response) throws IOException {
		Writer out = new java.io.OutputStreamWriter(response.getOutputStream(),"UTF-8");
		jdomOutputXml(outdoc,response,out);
		out.close();
	}
    public static void jdomOutputXml(Document outdoc,ServletResponse response,Writer out) throws IOException {
		response.setContentType("text/xml;character=UTF-8");
		
		//Writer out = new java.io.OutputStreamWriter(response.getOutputStream(),
		//        "UTF-8");
		org.jdom.output.Format f = Format.getPrettyFormat();
		f.setEncoding("UTF-8");
		XMLOutputter xmlout = new XMLOutputter(f);
		xmlout.output(outdoc, out);
    }
    
    public static void jdomOutputXml(Document outdoc, Writer out)
			throws IOException {

		org.jdom.output.Format f = Format.getPrettyFormat();
		f.setEncoding("UTF-8");
		XMLOutputter xmlout = new XMLOutputter(f);
    	
		xmlout.output(outdoc, out);

	}
    
	public static void jdomOutputXmlSimple(Document outdoc, Writer out)
			throws IOException {

		org.jdom.output.Format f = Format.getCompactFormat();
		f.setEncoding("UTF-8");
		XMLOutputter xmlout = new XMLOutputter(f);
		
		xmlout.output(outdoc, out);

	}    
	
	public static void jdomOutputXmlRaw(Document outdoc, Writer out)
	throws IOException {

		org.jdom.output.Format f = Format.getRawFormat();
		f.setEncoding("UTF-8");
		XMLOutputter xmlout = new XMLOutputter(f);

		xmlout.output(outdoc, out);

	}    	
    
    public static String jdomToString(Document outdoc)throws IOException{
        StringWriter sw = new StringWriter();
        JDomUtil.jdomOutputXml(outdoc,sw);
        return sw.toString();
    }    
    
    public static String jdomToStringSimple(Document outdoc)throws IOException{
        StringWriter sw = new StringWriter();
        JDomUtil.jdomOutputXmlSimple(outdoc,sw);
        return sw.toString();
    }        

    public static String jdomToStringRaw(Document outdoc)throws IOException{
        StringWriter sw = new StringWriter();
        JDomUtil.jdomOutputXmlRaw(outdoc,sw);
        return sw.toString();
    }  
    
    public static Document toDocument(ResultSet rs)throws SQLException{
    	Document outdoc = new Document(new Element("Results"));

    	ResultSetMetaData rsmd = rs.getMetaData();
    	int colCount = rsmd.getColumnCount();

    	while (rs.next())
    	{
    		Element row = addJdomContent(outdoc.getRootElement(),"Row");
    		for (int i = 1; i <= colCount; i++)
    		{
    			String columnName = rsmd.getColumnName(i);
    			Object value = rs.getObject(i);
    			
    			addJdomContent(row,columnName,value.toString());
    		}
    	}
    	return outdoc;
    }
    
    public static String getNonNullString(Object s) {
        if (s == null) {
            return "";
        } 
        else if(s instanceof Date){
        	String result;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            SimpleDateFormat sdf1 = new SimpleDateFormat("Z");
            result = sdf.format(s)+new StringBuffer(sdf1.format(s)).insert(3,":").toString();
            return result;
        }
        else{
            return s.toString();
        }
    }
    
    /**
    *
    * @return
    * @throws IOException
    * @throws JDOMException
    */
   public static Document parser(Reader reader) throws JDOMException,
           IOException {
       SAXBuilder builder = new SAXBuilder();
       return builder.build(reader);
   }

   public static Document parser(InputStream input) throws JDOMException,
   	IOException {
   		SAXBuilder builder = new SAXBuilder();
   		return builder.build(input);
   }
   
   /**
   *
   * @param doc
   * @param query
   * @return
   * @throws JDOMException 
   * @throws Exception
   */
  public static Element XPathSelectSingle(Document doc, String query) throws JDOMException  {
  	XPath xpath = XPath.newInstance(query);
  	return (Element) xpath.selectSingleNode(doc);
  }
  
  public static List<Element> XPathSelect(Document doc, String query) throws JDOMException  {
	  	XPath xpath = XPath.newInstance(query);
	  	return (List<Element>) xpath.selectNodes(doc);
	  }

}