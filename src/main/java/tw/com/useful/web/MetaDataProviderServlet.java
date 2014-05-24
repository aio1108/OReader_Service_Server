package tw.com.useful.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import tw.com.useful.data.model.MetaData;
import tw.com.useful.service.MetaDataService;
import tw.com.useful.util.ReadableJsonConverter;

import com.google.gson.Gson;

public class MetaDataProviderServlet extends HttpServlet {
	private Logger logger = Logger.getLogger(MetaDataProviderServlet.class);
	
	private static final long serialVersionUID = 1L;
       
    public MetaDataProviderServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MetaDataService metaDataService = new MetaDataService();
		String category = request.getParameter("category");
		String key = request.getParameter("key");
		List<MetaData> result = metaDataService.findByCategoryAndKeyword(category, key);
		Gson gson = new Gson();
		String jsonResult = gson.toJson(ReadableJsonConverter.convert(result));
		String callback = request.getParameter("callback")==null? "" : request.getParameter("callback").toString();
		logger.debug("callback =============="+ callback);
		if(callback.length() > 0){
			//for JSONP call
			response.setContentType("application/javascript;charset=UTF-8");
			jsonResult = callback + "(" + jsonResult + ")";
		}else{
			response.setContentType("application/json;charset=UTF-8");
		}
		response.addHeader("Access-Control-Allow-Origin", "*");
		PrintWriter pw = response.getWriter();
		pw.write(jsonResult);
		pw.flush();
		pw.close();
	}
}
