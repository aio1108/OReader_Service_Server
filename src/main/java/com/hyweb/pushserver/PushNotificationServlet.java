package com.hyweb.pushserver;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 * Servlet implementation class PushNotificationServlet
 */
public class PushNotificationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Logger log = Logger.getLogger(this.getClass());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PushNotificationServlet() {
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
		// TODO Auto-generated method stub
		String messageText = request.getParameter("message");
		String messageTitle = request.getParameter("title");
		String token = request.getParameter("token");
		String platform = request.getParameter("platform");
		PushNotificationResult execResult = new PushNotificationResult(false, "呼叫失敗");
		log.info("\n push message to device....");
		if(token.length() != 0){
			if(platform.equals("ios")){
				execResult = PushNotificationTask.pushMessageToiOS(messageTitle, messageText, token);
			}else{
				execResult = PushNotificationTask.pushMessageToAndroid(messageTitle, messageText, token);
			}
		}else{
			//execResult = PushNotificationTask.pushMessageToAndroid(messageTitle, messageText) && PushNotificationTask.pushMessageToiOS(messageTitle, messageText);
		}
		response.setHeader("content-type", "text/html;charset=utf-8");
		if(execResult.getStatus()){			
			response.getOutputStream().write(("訊息已傳送!!").getBytes("utf-8"));
		}else{
			response.getOutputStream().write(("發生錯誤，訊息無法傳送!! Error Message: " + execResult.getMessage()).getBytes("utf-8"));
		}
	}

}
