package tw.com.useful.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import tw.com.useful.data.model.Subscribe;
import tw.com.useful.service.SubscribeService;
import tw.com.useful.service.ViewTypeService;

import com.mongodb.WriteResult;

public class UpdateSubscribeServlet extends HttpServlet {
	private Logger logger = Logger.getLogger(UpdateSubscribeServlet.class);
	
	private static final long serialVersionUID = 1L;
       
    public UpdateSubscribeServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SubscribeService subscribeService = new SubscribeService();
		ViewTypeService viewTypeService = new ViewTypeService();
		String subscribeId = request.getParameter("subscribeId");
		String preferType = request.getParameter("preferType");
		Subscribe subscribe = subscribeService.findById(new ObjectId(subscribeId));
		subscribe.setPreferViewType(viewTypeService.getDBRef(preferType));
		WriteResult updateResult = subscribeService.update(subscribe);
		String jsonResult = (updateResult.getField("err") == null)?"{\"success\":true}":"{\"success\":false}";
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
