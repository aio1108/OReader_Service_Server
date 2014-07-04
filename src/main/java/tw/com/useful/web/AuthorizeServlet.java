package tw.com.useful.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import tw.com.useful.data.model.User;
import tw.com.useful.service.UserService;
import tw.com.useful.util.ReadableJsonConverter;

public class AuthorizeServlet extends HttpServlet {
	private Logger logger = Logger.getLogger(AuthorizeServlet.class);
	
	private static final long serialVersionUID = 1L;
       
    public AuthorizeServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserService userService = new UserService();
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		User result = userService.findById(id);
		Gson gson = new Gson();
		String jsonResult = (result == null)?"{\"auth\": false}":(result.getPassword().equals(pwd))?"{\"auth\": true, \"user\": " + gson.toJson(ReadableJsonConverter.convert(result)) + "}":"{\"auth\": false}";
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
