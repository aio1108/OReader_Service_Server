package com.hyweb.web;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JSONString;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
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
public class GetPdfCoverServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetPdfCoverServlet() {
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
		RmLogger _log = RmLogger.getRmLogger("GetPdfCoverServlet");
		_log.debug("GetPdfCoverServlet - [start]");
		
     	
     	try
     	{
     		response.setContentType("image/*;charset=UTF-8");
     		OutputStream outStream = response.getOutputStream();
     		
     		String file = request.getParameter("file");
     		Double width = request.getParameter("w")==null? 800 : Double.valueOf(request.getParameter("w"));
     		
     		String publicPath = "D:/Web_Inter/site/dgbas/public/data/";
     		String pdfPath = publicPath + file;
     		String coverImgPath = publicPath + file.substring(0,file.indexOf("."))+".jpg";
     		
     		File coverImgFile = new File(coverImgPath);
     		_log.debug("image path="+coverImgPath);
     		
     		if(! coverImgFile.exists())
     		{
	    		PDDocument pdf=null;
	    		try
	    		{
	    			File pdfFile = new File(pdfPath);
	    			System.out.println("PDF path ="+ pdfFile.getPath() +", is exist:"+pdfFile.exists());
	    			
	    			
	    			if(pdfFile.exists())
	    			{
	    				pdf = PDDocument.load(new File(pdfPath));
		    			List<PDPage> pages = pdf.getDocumentCatalog().getAllPages();
		    			PDPage p = pages.get(0);
		    			
		    			BufferedImage originalImage = p.convertToImage();
		    			double w = request.getParameter("w")==null? 800 : Double.valueOf(request.getParameter("w"));
		    			double r = w/originalImage.getWidth();
		    			
		    			double h = originalImage.getHeight()*r;
		    			System.out.println("r ="+r);
		    			System.out.println("originalImage.getWidth()="+originalImage.getWidth() +", w="+w +", org height="+originalImage.getHeight()+", h="+h);
		    			BufferedImage resizedImage = new BufferedImage((int)w, (int)h, originalImage.getType());
		    			Graphics2D g = resizedImage.createGraphics();
		    			g.drawImage(originalImage, 0, 0, (int)w, (int)h, null);
		    			g.dispose();
		    			
		    			ImageIO.write(resizedImage, "jpg", coverImgFile);
		    			
		    			ImageIO.write(resizedImage, "jpg", outStream);
	    			}
	    		}
	    		catch(Exception ex)
	    		{
	    			ex.printStackTrace();
	    		}
	    		finally
	    		{
	    			if(pdf!=null) pdf.close();
	    		}
     		}
     		else
     		{
     			ImageIO.write( ImageIO.read(coverImgFile) , "jpg" , outStream);
     		}
     		
    		outStream.flush();
    		outStream.close();

     	} 
     	catch (Exception e)
     	{
     		e.printStackTrace();
     		_log.error(e);
     	} 
	}
}
