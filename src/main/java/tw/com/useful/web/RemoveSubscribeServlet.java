package tw.com.useful.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import tw.com.useful.data.model.Subscribe;
import tw.com.useful.data.model.User;
import tw.com.useful.service.CategoryService;
import tw.com.useful.service.MetaDataService;
import tw.com.useful.service.SubscribeService;
import tw.com.useful.service.UserService;
import tw.com.useful.service.ViewTypeService;
import tw.com.useful.util.ReadableJsonConverter;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBRef;
import com.mongodb.WriteResult;

public class RemoveSubscribeServlet extends HttpServlet {
	private Logger logger = Logger.getLogger(RemoveSubscribeServlet.class);
	
	private static final long serialVersionUID = 1L;
       
    public RemoveSubscribeServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SubscribeService subscribeService = new SubscribeService();
		UserService userService = new UserService();
		String id = request.getParameter("id");
		String subscribeId = request.getParameter("subscribeId");
		Subscribe subscribe = subscribeService.findById(new ObjectId(subscribeId));
		User user = userService.findById(id);
		List<DBRef> subscribes = user.getSubscribes();
		subscribes.remove(subscribeService.getDBRef(subscribe.getId()));
		user.setSubscribes(subscribes);
		WriteResult updateResult = userService.update(user);
		WriteResult removeResult = subscribeService.remove(subscribe);
		String jsonResult = (removeResult.getField("err") == null && updateResult.getField("err") == null)?"{\"success\":true}":"{\"success\":false}";
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
