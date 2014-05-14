package com.hyweb.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.mortbay.util.ajax.JSON;


/**
 * 資料字串共通處理類別
 *
 */
public class CommonUtil {
	private final static Log LOGGER = LogFactory.getLog(CommonUtil.class);

    public static String getNonNullString(Object s) {
        if (s == null) {
            return "";
        }        
        else {
            return s.toString();
        }
    }
    
    public static String getNonNullString(Clob s)throws SQLException,IOException {
        if (s == null) {
            return "";
        } 
        else{
        	Clob clob = (Clob)s;
        	Reader in = clob.getCharacterStream();
        	StringWriter sw = new StringWriter();
        	char[] buf = new char[1024];
        	int len = 0;
        	while ((len = in.read(buf)) != -1) {
        		sw.write(buf, 0, len);
        	}

        	return sw.toString();        	
        }
    }

    public static String getNonNullString(String s) {
        if (s == null) {
            return "";
        } else {
            return s;
        }
    }

    public static String getNonNullString(org.jdom.Element s) {
      if(s == null){
          return "";
      }
      else{
        return s.getText();
      }
    }

    //----------------------------------------//

    public static int countOccurrence(String s1, String s2) {
        int i = s1.indexOf(s2);
        int count = 0;
        while (i > -1) {
            count++;
            i = s1.indexOf(s2, i + s2.length());
        }
        return count;
    }

    public static String getNumberString(float f,String format){
      NumberFormat formatter = new DecimalFormat(format);
      return formatter.format(f);
    }

    public static String getDateString(Date d, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(d);
    }


    public static String getCalendarString(Calendar rcal){
        String rcalstr = (rcal.get(Calendar.YEAR)-1911)+"年"
        + (rcal.get(Calendar.MONTH)+1)+"月"
        + (rcal.get(Calendar.DATE)) + "日";

        if(rcal.get(Calendar.AM_PM)==Calendar.AM){
          rcalstr+="上午";
        }
        else{
          rcalstr+="下午";
        }

        rcalstr += CommonUtil.twoChar(rcal.get(Calendar.HOUR))+":"+CommonUtil.twoChar(rcal.get(Calendar.MINUTE));
        return rcalstr;
    }

    public static String[] getRocDateString(Date d) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(d);

        int year = cal.get(Calendar.YEAR);
        year = year - 1911;
        String[] result = new String[] {String.valueOf(year),
                          twoChar(cal.get(Calendar.MONTH) + 1)
                          , twoChar(cal.get(Calendar.DATE))};

        return result;
    }

    public static String twoChar(int i) {
        if (i < 10) {
            return "0" + i;
        } else {
            return String.valueOf(i);
        }
    }

    public static String pzeroChar(int i,int len){
       StringBuffer s = new StringBuffer(String.valueOf(i));
       int k = len-s.length();
       for(int j=0; j < k; j++){
         s.insert(0,"0");
       }
       return s.toString();
    }

    //-------------------------------------------//

    public static int toInt(Object ob) {
        int value = 0;
        if (ob == null) {
            ;
        } else if (ob instanceof String) {
            try {
                value = Integer.parseInt((String) ob);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        } else if (ob instanceof Number) {
            value = ((Number) ob).intValue();
        } else if (ob instanceof Integer) {
            value = ((Integer) ob).intValue();
        } else {
            value = Integer.parseInt(ob.toString());
        }
        return value;
    }

    public static int getSeq(String s){
      int start = 0;
      for(int i = 0; i < s.length(); i++){
        if(s.charAt(i) == '0')
          start++;
        else
          break;
      }
      return Integer.parseInt(s.substring(start));
    }

    //------------------------------------//

    public static Date stringToDate(String s)throws Exception{
      StringTokenizer stz = new StringTokenizer(s,"/");
      if(stz.countTokens() != 3){
          throw new Exception("日期格式錯誤");
      }
      else{
          int y = Integer.parseInt(stz.nextToken());
          int m = Integer.parseInt(stz.nextToken());
          int d = Integer.parseInt(stz.nextToken());
          GregorianCalendar cal = new GregorianCalendar(y,m-1,d);
          return cal.getTime();
      }
    }

    public static int dateDiff(Date date1,Date date2){
      Calendar cal1 = Calendar.getInstance();
      Calendar cal2 = Calendar.getInstance();
      // different date might have different offset
      cal1.setTime(date1);
      long ldate1 = date1.getTime() + cal1.get(Calendar.ZONE_OFFSET) + cal1.get(Calendar.DST_OFFSET);

      cal2.setTime(date2);
      long ldate2 = date2.getTime() + cal2.get(Calendar.ZONE_OFFSET) + cal2.get(Calendar.DST_OFFSET);

      // Use integer calculation, truncate the decimals
      int hr1   = (int)(ldate1/3600000); //60*60*1000
      int hr2   = (int)(ldate2/3600000);

      int days1 = (int)hr1/24;
      int days2 = (int)hr2/24;


      int dateDiff  = days2 - days1;
      //int weekOffset = (cal2.get(Calendar.DAY_OF_WEEK) - cal1.get(Calendar.DAY_OF_WEEK))<0 ? 1 : 0;
      //int weekDiff  = dateDiff/7 + weekOffset;
      //int yearDiff  = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR);
      //int monthDiff = yearDiff * 12 + cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH);
      return dateDiff;

    }

    public static int countEven(int value){
       String s = String.valueOf(value);
       int result = 0;
       for(int i = 0; i <s.length(); i++){
         if(Integer.parseInt(s.substring(i,i+1))%2==0){
             result++;
         }
       }
       return result;
    }

    //----------------------------------//

    public static String fileToString(String filepath)throws Exception{

      FileInputStream in = new FileInputStream(filepath);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
       byte[] buf = new byte[1024];
       int len = 0;
       while ((len = in.read(buf)) != -1) {
         baos.write(buf, 0, len);
      }

      String result = baos.toString("UTF-8");

      return result;
    }

	public static String getFirstParam(String pname,String defaultValue,Map request)
	{
		Object obj = request.get(pname);
		if(obj==null) return defaultValue;
		
		//System.out.println("pname="+pname+ "obj="+obj);
		
		if(obj instanceof String[])
		{
			String[] temp = (String[])obj;
			return temp[0];
		}
		else if(obj instanceof List)
		{
			List params = (List)obj;
			if(params.size()>0 && params.get(0) instanceof String)
			{
				return params.get(0).toString();
			}
		}
		return defaultValue;
	}
	
	public static int getFirstParam(String pname,int defaultValue,Map request)
	{
		String[] params = (String[])request.get(pname);
		if(params != null){			
			return Integer.parseInt(params[0]);			
		}
		else{
			return defaultValue;
		}
	}	
	
	public static boolean getFirstParam(String pname,boolean defaultValue,Map request){
		String[] params = (String[])request.get(pname);
		if(params != null){			
			if("true".equals(params[0])){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return defaultValue;
		}
	}
	
	public static String getSessionAttribute(String pname,String defaultValue,HttpSession session){
		Object param = session.getAttribute(pname);
		if(param != null){
			return (String)param;
		}
		else{
			return defaultValue;
		}
	}
	
	public static int getSessionAttribute(String pname,int defaultValue,HttpSession session){
		Object param = session.getAttribute(pname);
		if(param != null){
			return ((Integer)param).intValue();
		}
		else{
			return defaultValue;
		}
	}	
	
	public static String convertStreamToString(InputStream is) throws UnsupportedEncodingException 
	{ 
		StringBuilder sb = new StringBuilder(); 
		try
		{
	   		BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8")); 
		
			String line = null; 
			while ((line = reader.readLine()) != null)
			{               
				sb.append(line + "\n");             
			}        
		}
		catch (IOException e)
		{           
			e.printStackTrace();         
		} 
		finally 
		{ 		            
			try 
			{                 
				is.close();            
			} 
			catch (IOException e) 
			{
				e.printStackTrace();            
			}
		} 
		        
		return sb.toString();     
	} 	
	
	public static boolean notEmptyString(String s) {
        if (s != null && !s.equals("")) {
            return true;
        } else {
            return false;
        }
    }
	
	public static String[] getParams(String pname,String defaultValue,Map request){
		String[] params = (String[])request.get(pname);
		if(params != null){			
			return params;			
		}
		else{
			return null;
		}
	}
	
	public static void printRequest(HttpServletRequest request,Logger LOGGER) {
        LOGGER.info("--------request parameter------------");
        Enumeration e = request.getParameterNames();
        while (e.hasMoreElements()) 
        {
            String key = (String) e.nextElement();
            LOGGER.info(key + "=" + request.getParameter(key));
        }
        LOGGER.info("--------request attribute------------");
        e = request.getAttributeNames();
        while (e.hasMoreElements()) 
        {
            String key = (String) e.nextElement();
            LOGGER.info(key + "=" + request.getAttribute(key));
        }
        LOGGER.info("---------session attribute-----------");
        HttpSession session = request.getSession();
        LOGGER.info(session.getId());
        e = session.getAttributeNames();
        while (e.hasMoreElements()) 
        {
            String key = (String) e.nextElement();
            LOGGER.info( key + "=" + session.getAttribute(key));
        }
        LOGGER.info("------------------------------------");
    }

	/**
	 * 將Map物件轉為json string,若map是null則回傳null
	 * @param map
	 * @return
	 */
	public static String buildJSONString(Map<String, String> map) {
		if(map == null) {
			return null;
		} else {
			return JSON.toString(map);
		}
	}
	
	public static List convertListDo2Map(List doList)
    {
    	if(doList==null) return null;
    	
    	List resultList = new ArrayList();
    	
    	for(int i=0;i<doList.size();i++)
    	{
    		resultList.add(convertDo2Map(doList.get(i)));
    	}
    	
    	return resultList;
    }
	
	public static Map convertDo2Map(Object obj)
    {
    	if(obj==null) return null;
    	
    	Map map = new HashMap();
    	
    	Method ms [] = obj.getClass().getMethods();
    	for (int i=0;i<ms.length;i++)
        {
    		String mn = ms[i].getName();   
            
            if(!mn.startsWith("get") || mn.equals("getClass")) continue;
            
            //System.out.println("Mthod = "+mn + ", column = "+mn.substring(3));
            
            try {
            	Object valObj = ms[i].invoke(obj, null);
            	map.put(mn.substring(3), valObj);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    	
    	return map;
    	
    }
}
