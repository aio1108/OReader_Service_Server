package com.hyweb.action.service;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import tw.com.useful.runner.util.LogicalService;
import tw.com.useful.runner.util.RmJDomUtil;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.gson.Gson;
import com.hyweb.util.JDomUtil;
import com.hyweb.util.JSONUtil;

public class DataConvertService extends LogicalService
{
	private ListeningExecutorService executorService;
	private List cityRowList = new ArrayList();
	private List indicatorRowList = new ArrayList();
	private CountDownLatch waitCount;
	private Document concurrentDoc = new Document();
	
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
	
	public Document getDsWithConditionsFromExcel() throws IOException{
		String fileName = (String)this.getInputParameter("fileName","");
		String sheetIndex = (String)this.getInputParameter("sheet","0");
		String[] indices = ((String)this.getInputParameter("indices","")).split(",");
		String[] cities = ((String)this.getInputParameter("cities","")).split(",");
		String[] years = ((String)this.getInputParameter("years","")).split(",");
		concurrentDoc.setRootElement(new Element("result"));
		executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(indices.length));
		
		Map citiesMap = transformExcelToMap(fileName, "4");
		int cityEndIndex = findEndIndexByFlag(citiesMap, "####");
		cityRowList = filterToListFromExcelDS(citiesMap, 2, cityEndIndex, "B,C,D,E");
		
		Map indicatorMap = transformExcelToMap(fileName, "0");
		int indicatorEndIndex = findEndIndexByFlag(indicatorMap, "####");
		indicatorRowList = filterToListFromExcelDS(indicatorMap, 2, indicatorEndIndex, "B,C,D,E,F,G,H,I,J");
		
		try{
			if(indices.length != 0 && cities.length != 0 && years.length != 0){
				waitCount = new CountDownLatch(indices.length);
				for(int i = 0;i < indices.length;i++){
					ListenableFuture<String> futureTask = executorService.submit(new Task(fileName, sheetIndex, indices[i], cities, years));
			        FutureCallbackImpl callback = new FutureCallbackImpl();
			        Futures.addCallback(futureTask, callback);
				}
		        waitCount.await();
		        return concurrentDoc;
			}
			return concurrentDoc;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return concurrentDoc;
		}finally{
			executorService.shutdownNow();
		}
	}
	
	private void addExcelRowsToDocWithConditions(String fileName, String sheetIndex, String index, String[] cities, String[] years) throws IOException { 
		FileInputStream fis = new FileInputStream(fileName);
        POIFSFileSystem fs = new POIFSFileSystem( fis );
        Map cityMap = new Hashtable();
        List<Double> yearList = new ArrayList<Double>();
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheetAt(Integer.parseInt(sheetIndex));
        HSSFRow headerRow = sheet.getRow(0);
        for(int i = 0;i < years.length;i++){
        	yearList.add(Double.parseDouble(years[i]));
        }
        for(int i = 0;i < headerRow.getPhysicalNumberOfCells();i++){
        	HSSFCell headerCell = headerRow.getCell(i);
        	if(headerCell.toString().startsWith("C")){
        		cityMap.put(headerCell.toString(), i);
        	}
        }
        for (int i = 1; i < sheet.getPhysicalNumberOfRows() ; i++) {
            HSSFRow row = sheet.getRow(i);
            HSSFCell indexCell = row.getCell(1);
            HSSFCell yearCell = row.getCell(2);
            if(indexCell.toString().equals(index) && yearList.contains(Double.parseDouble(yearCell.toString()))){
            	Map resultMap = new Hashtable();
            	Element indexElement = new Element("index");
            	Element idElement = new Element("id");
            	Element nameElement = new Element("name");
            	Element yearElement = new Element("year");
            	idElement.setText(index);
            	nameElement.setText(findIndexName(index));
            	yearElement.setText(yearCell.toString());
            	indexElement.addContent(idElement);
            	indexElement.addContent(nameElement);
            	indexElement.addContent(yearElement);
            	for(int j = 0;j < cities.length;j++){
            		if(cityMap.containsKey(cities[j])){
            			int cellIndex = (Integer) cityMap.get(cities[j]);
            			HSSFCell cell = row.getCell(cellIndex);
            			if (cell == null || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK){
                            continue;
                        }
            			Element cityElement = new Element("city");
            			Element cityIdElement = new Element("id");
            			Element cityNameElement = new Element("name");
            			Element valueElement = new Element("value");
            			cityIdElement.setText(cities[j]);
            			cityNameElement.setText(findCityName(cities[j]));
            			valueElement.setText(cell.toString());
            			cityElement.addContent(cityIdElement);
            			cityElement.addContent(cityNameElement);
            			cityElement.addContent(valueElement);
            			indexElement.addContent(cityElement);
            		}
            	}
            	concurrentDoc.getRootElement().addContent(indexElement);
            }
        }
        fis.close();
	}
	
	private String findIndexName(String id) {
		for(int i = 0;i < indicatorRowList.size();i++){
			Map row = (Map) indicatorRowList.get(i);
			if(row.get("B").toString().equals(id)){
				return row.get("C").toString(); 
			}
		}
		return "";
	}

	private String findCityName(String id) {
		for(int i = 0;i < cityRowList.size();i++){
			Map row = (Map) cityRowList.get(i);
			if(row.get("B").toString().equals(id)){
				return row.get("C").toString(); 
			}
		}
		return "";
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
	
	public Document getXMLDataFromURL() throws TransformerException{
		Document doc = new Document();
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
			
			doc = RmJDomUtil.buildXml(is);
		}catch (HttpException e){
			
		}catch (IOException e){
			
		}catch (JDOMException e){
			e.printStackTrace();
		}
		
		TransformerFactory factory = TransformerFactory.newInstance();
		Source xsl = null;
		Transformer transformer = factory.newTransformer(xsl);
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		Result result = null;
		transformer.transform(xsl, result);
		
		/*
		String result = "";
		try {
			result = JDomUtil.jdomToString(doc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONUtil xmlSerializer = new JSONUtil(); 
		xmlSerializer.setRemoveNamespacePrefixFromElements(true);
		xmlSerializer.setSkipNamespaces(true); 
		xmlSerializer.setTypeHintsCompatibility(true);
		xmlSerializer.setTypeHintsEnabled(false);
		JSON json = xmlSerializer.read(result);  
		result = json.toString(2);
		
		Gson gson = new Gson();
		Map obj = gson.fromJson(result, Map.class);
		return obj;
		*/
		
		return doc;
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
        	addExcelRowsToDocWithConditions(fileName, sheetIndex, index, cities, years);
            return "Task Done";
        }
    }
	
}
