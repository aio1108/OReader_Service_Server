package tw.com.useful.action.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;

import net.sf.json.JSON;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import tw.com.useful.runner.util.LogicalService;
import tw.com.useful.runner.util.LsUtil;
import tw.com.useful.runner.util.RmJDomUtil;
import tw.com.useful.util.JDomUtil;
import tw.com.useful.util.JSONUtil;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

public class DataConvertService extends LogicalService
{
	private Logger logger = Logger.getLogger(DataConvertService.class);
	private ListeningExecutorService executorService;
	private CountDownLatch waitCount;
	
	public Map getDsFromExcel() throws IOException {
		String fileName = (String)this.getInputParameter("fileName","");
		String sheetIndex = (String)this.getInputParameter("sheet","0");
		Map resultMap = transformExcelToMap(fileName, sheetIndex);
		return resultMap;
	}
	
	public Map getDsWithFirstGroupFromExcel() throws IOException {
		String fileName = (String)this.getInputParameter("fileName","");
		String sheetIndex = (String)this.getInputParameter("sheet","0");
		String columnIndex = (String)this.getInputParameter("columnIndex","");
		Map resultMap = transformExcelToMapWithFirstGroup(fileName, sheetIndex, columnIndex);
		return resultMap;
	}

	private Map transformExcelToMapWithFirstGroup(String fileName, String sheetIndex, String columnIndex) throws IOException {
		Set distinctSet = new HashSet(); 
		Map resultMap = new Hashtable();
		FileInputStream fis = new FileInputStream(fileName);
        POIFSFileSystem fs = new POIFSFileSystem( fis );
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheetAt(Integer.parseInt(sheetIndex));
        for (int i = 0; i < sheet.getPhysicalNumberOfRows() ; i++) {
            HSSFRow row = sheet.getRow(i);
            HSSFCell filterCell = row.getCell(getColumnNum(columnIndex));
            if(distinctSet.contains(filterCell.toString())){
            	break;
            }else{
            	distinctSet.add(filterCell.toString());
            	for ( Iterator<HSSFCell> cellsIT = row.cellIterator(); cellsIT.hasNext(); ) { 
                	HSSFCell cell = cellsIT.next(); 
                	if (cell == null || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK){
                        continue;
                    }
                	String colIndex = getColumnIndex(cell.getCellNum());
                	resultMap.put(colIndex+ String.valueOf(i+1), cell.toString());
                }
            }
        }
        fis.close();
        return resultMap;
	}

	private Map transformExcelToMap(String fileName, String sheetIndex) throws IOException {
		Map resultMap = new Hashtable();
		FileInputStream fis = new FileInputStream(fileName);
        POIFSFileSystem fs = new POIFSFileSystem( fis );
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheetAt(Integer.parseInt(sheetIndex));
        for (int i = 0; i < sheet.getPhysicalNumberOfRows() ; i++) {
            HSSFRow row = sheet.getRow(i);
            for ( Iterator<HSSFCell> cellsIT = row.cellIterator(); cellsIT.hasNext(); ) { 
            	HSSFCell cell = cellsIT.next(); 
            	if (cell == null || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK){
                    continue;
                }
            	String colIndex = getColumnIndex(cell.getCellNum());
            	resultMap.put(colIndex+ String.valueOf(i+1), cell.toString());
            }
        }
        fis.close();
        return resultMap;
	}

	private String getColumnIndex(short cellNum) {
		String temp[] = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z".split(",");
		if(cellNum <= 25){
			return temp[cellNum];
		}else{
			short firstDigit = (short) (cellNum / 26);
			short secondDigit = (short) (cellNum % 26);
			return temp[firstDigit] + temp[secondDigit];
		}
	}
	
	private short getColumnNum(String columnIndex) {
		String temp[] = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z".split(",");
		if(columnIndex.length() == 1){
			for(int i = 0;i < temp.length;i++){
				if(columnIndex.equals(temp[i])){
					return (short) i;
				}
			}
		}else if(columnIndex.length() == 2){
			short result = 0;
			String firstDigit = columnIndex.substring(0, 1);
			String secondDigit = columnIndex.substring(1);
			for(int i = 0;i < temp.length;i++){
				if(firstDigit.equals(temp[i])){
					result = (short) (result + ((i + 1) * 26));
				}
				if(secondDigit.equals(temp[i])){
					result = (short) (result + i);
				}
			}
			return result;
		}
		return -1;
	}

	public List filterDataFromExcelDs()
	{
		Map dsMap = (Map)this.getInputParameter("dataSet","");
		int startIndex = Integer.parseInt((String)this.getInputParameter("startRowIndex","-1"));
		int endIndex = (((String)this.getInputParameter("endRowIndex","-1")).equals("-1"))?dsMap.size():Integer.parseInt((String)this.getInputParameter("endRowIndex","-1"));
		String colIndex = (String)this.getInputParameter("columnIndexList","");	
		List resultList = filterToListFromExcelDS(dsMap, startIndex, endIndex, colIndex);
		return resultList;
	}
	
	private List filterToListFromExcelDS(Map dsMap, int startIndex, int endIndex, String colIndex){
		System.out.println("dataSet = "+this.getInputParameter("dataSet",""));
		System.out.println("startIndex = "+startIndex);
		List resultList = new ArrayList();
		String[] colIndexAry = colIndex.split(",");
		for(int i=startIndex;i<=endIndex;i++){
			TreeMap rowMap = new TreeMap();				
			for(int j=0;j<colIndexAry.length;j++){
				String key = colIndexAry[j]+i;
				String value = dsMap.get(key)==null? "" : dsMap.get(key).toString();
				if(value.length()>0) rowMap.put(colIndexAry[j], value);
			}
			if(rowMap.isEmpty()) continue;
			resultList.add(rowMap);
		}
		return resultList;
	}
	
	public List applyInFilter()
	{
		List resultList = new ArrayList();
		System.out.println("dataSet = "+this.getInputParameter("dataSet",""));
		List dsList = (List)this.getInputParameter("dataSet","");
		String columnIndex = (String)this.getInputParameter("columnIndex","");
		String[] columnValues = ((String)this.getInputParameter("columnValues","")).split(",");
		for(int i = 0;i < dsList.size();i++){
			TreeMap rowMap = (TreeMap) dsList.get(i);
			for(int j = 0;j < columnValues.length;j++){
				if(rowMap.get(columnIndex).equals(columnValues[j])){
					resultList.add(rowMap);
					break;
				}
			}
		}
		return resultList;
	}
	
	public List applyStartsWithFilter()
	{
		List resultList = new ArrayList();
		System.out.println("dataSet = "+this.getInputParameter("dataSet",""));
		List dsList = (List)this.getInputParameter("dataSet","");
		String columnIndex = (String)this.getInputParameter("columnIndex","");
		String columnValue = (String)this.getInputParameter("columnValue","");
		for(int i = 0;i < dsList.size();i++){
			TreeMap rowMap = (TreeMap) dsList.get(i);
			if(((String)rowMap.get(columnIndex)).startsWith(columnValue)){
				resultList.add(rowMap);
			}
		}
		return resultList;
	}
	
	public List applyDistinctFilter()
	{
		List resultList = new ArrayList();
		System.out.println("dataSet = "+this.getInputParameter("dataSet",""));
		List dsList = (List)this.getInputParameter("dataSet","");
		String columnIndex = (String)this.getInputParameter("columnIndex","");
		Set valueSet = new HashSet();
		for(int i = 0;i < dsList.size();i++){
			TreeMap rowMap = (TreeMap) dsList.get(i);
			if(!valueSet.contains(rowMap.get(columnIndex))){
				valueSet.add(rowMap.get(columnIndex));
				TreeMap valueMap = new TreeMap();
				valueMap.put("value", rowMap.get(columnIndex));
				resultList.add(valueMap);
			}
		}
		return resultList;
	}
	
	public Document getXMLDataFromURL() throws Exception{
		Document doc = new Document();
    	String url = (String)this.getInputParameter("url","");
    	String charset = (String)this.getInputParameter("charset", "UTF-8");
    	String xslName = (String) this.getInputParameter("xslName","");
    	
		HttpClient httpclient = new HttpClient();
		GetMethod get = new GetMethod(url);

		try{
			httpclient.getParams().setParameter("http.protocol.content-charset", charset);
			int result = httpclient.executeMethod(get);
			System.out.println("StatusCode: " + result);
			
			String resData = get.getResponseBodyAsString();
			
			InputStream is = new ByteArrayInputStream(resData.getBytes(charset));        
			
			doc = RmJDomUtil.buildXml(is);
		}catch (HttpException e){
			
		}catch (IOException e){
			
		}catch (JDOMException e){
			e.printStackTrace();
		}
		
		if(!xslName.equals("")){
			final String transformedXML = LsUtil.transform(doc, LsUtil.getSysValue("actionXslPath") + xslName);
			InputStream is = new ByteArrayInputStream(transformedXML.getBytes("utf-8"));
			Document transformedDoc = RmJDomUtil.buildXml(is);
			return transformedDoc;
		}
		
		return doc;
	}
	
	public List getJsonDataFromURL(){
		List resultList = null;
		String url = (String)this.getInputParameter("url","");
    	String charset = (String)this.getInputParameter("charset", "UTF-8");
    	HttpClient httpclient = new HttpClient();
		GetMethod get = new GetMethod(url);
		try{
			httpclient.getParams().setParameter("http.protocol.content-charset", charset);
			int result = httpclient.executeMethod(get);
			System.out.println("StatusCode: " + result);
			String resData = get.getResponseBodyAsString();
			InputStream is = new ByteArrayInputStream(resData.getBytes(charset));        
			JsonReader jsonReader = new JsonReader(new InputStreamReader(is, "UTF-8"));
			Gson gson = new Gson();
			if(jsonReader.peek().equals(JsonToken.BEGIN_ARRAY)){
				resultList = gson.fromJson(jsonReader, List.class);
			}else{
				resultList = new ArrayList<Map>();
				resultList.add(gson.fromJson(jsonReader, Map.class));
			}
		}catch (HttpException e){
			
		}catch (IOException e){
			
		}
		return resultList;
	}
	
	public List<Map> getCSVDataFromURL(){
		List<Map> resultList = new ArrayList<Map>();
		ICsvMapReader mapReader = null;
		String url = (String)this.getInputParameter("url","");
		String headerIds = (String)this.getInputParameter("headerIds","");
    	String charset = (String)this.getInputParameter("charset", "UTF-8");
    	HttpClient httpclient = new HttpClient();
		GetMethod get = new GetMethod(url);
		try{
			httpclient.getParams().setParameter("http.protocol.content-charset", charset);
			int result = httpclient.executeMethod(get);
			System.out.println("StatusCode: " + result);
			String resData = get.getResponseBodyAsString();
			InputStream is = new ByteArrayInputStream(resData.getBytes(charset));        
			
			mapReader = new CsvMapReader(new InputStreamReader(is), CsvPreference.STANDARD_PREFERENCE);
			final String[] header = mapReader.getHeader(true);
			final CellProcessor[] processors = getProcessors(header);
			final Map<String, String> headerMap = getHeaderMap(headerIds, header);
			Map<String, Object> customerMap;
            while( (customerMap = mapReader.read(header, processors)) != null ) {
            	Map<String, Object> row = new HashMap<String, Object>();
            	if(headerIds.length() == 0){
            		row = customerMap;
            	}else{
            		for(String key : headerMap.keySet()){
                		row.put(headerMap.get(key), customerMap.get(key));
                	}
            	}
            	resultList.add(row);
            }
		}catch (HttpException e){
			
		}catch (IOException e){
			
		}finally{
			if(mapReader != null){
				try {
					mapReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    	return resultList;
	}
	
	private Map<String, String> getHeaderMap(String headerIds, String[] header) {
		if(headerIds.length() == 0){
			return null;
		}
		Map<String, String> result = new HashMap<String, String>();
		String[] ids = headerIds.split(",");
		for(int i = 0;i < ids.length;i++){
			result.put(header[i], ids[i]);
		}
		return result;
	}

	private CellProcessor[] getProcessors(String[] header) {
		final CellProcessor[] processors = new CellProcessor[header.length];
		for(int i = 0;i < header.length;i++){
			processors[i] = new Optional();
		}
	    return processors;
	}
	
	public int getEndIndex()
	{
		Map dsMap = (Map)this.getInputParameter("dataSet","");
		String endFlag = (String)this.getInputParameter("endFlag","####");
		int endIndex = findEndIndexByFlag(dsMap, endFlag);
		return endIndex;
	}
	
	private int findEndIndexByFlag(Map dsMap, String endFlag){
		int endIndex = -1;
		
		if(dsMap==null || !dsMap.containsValue(endFlag)) return endIndex;
		
		Iterator it = dsMap.keySet().iterator();
		while (it.hasNext())
        {
            String key = it.next().toString();
            Object value = dsMap.get(key);  
            if(value.toString().equals(endFlag))
            {
            	key = key.replaceAll("[a-zA-Z]", "");
            	endIndex = Integer.parseInt(key)-1;
            	break;
            }
        }
		
		return endIndex;
	}
	
	private class FutureCallbackImpl implements FutureCallback<String> {
		 
        private StringBuilder builder = new StringBuilder();
 
        @Override
        public void onSuccess(String result) {
            builder.append(result).append(" successfully");
            done();
        }
 
        @Override
        public void onFailure(Throwable t) {
            builder.append(t.toString());
            done();
        }
 
        private void done() {
            waitCount.countDown();
        }
 
        public String getCallbackResult() {
            return builder.toString();
        }
    }
 
 
    private class Task implements Callable<String> {
    	private String fileName;
    	private String sheetIndex;
    	private String index;
    	private String[] cities;
    	private String[] years;
        public Task(String fileName, String sheetIndex, String index, String[] cities, String[] years) {
        	this.fileName = fileName;
        	this.sheetIndex = sheetIndex;
        	this.index = index;
        	this.cities = cities;
        	this.years = years;
        }
 
        @Override
        public String call() throws Exception {
            return "Task Done";
        }
    }
	
}
