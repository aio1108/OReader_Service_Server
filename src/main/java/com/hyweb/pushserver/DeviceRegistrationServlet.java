package com.hyweb.pushserver;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DeviceRegistrationServlet
 */
public class DeviceRegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeviceRegistrationServlet() {
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
		String deviceToken = "";
		String platform = "";
		String action = request.getParameter("action");
		response.setHeader("content-type", "text/html;charset=utf-8");
		if(action.equals("register")){
			deviceToken = request.getParameter("rid");
			platform = request.getParameter("platform");
			DeviceTokenHolder.register(deviceToken, platform);
			System.out.println("Platform: " + platform);
			System.out.println("Register Token: " + deviceToken);
			response.getOutputStream().write(("裝置已註冊!!").getBytes("utf-8"));
		}else if(action.equals("unregister")){
			deviceToken = request.getParameter("rid");
			platform = request.getParameter("platform");
			DeviceTokenHolder.unregister(deviceToken, platform);
			System.out.println("Platform: " + platform);
			System.out.println("Register Token: " + deviceToken);
			response.getOutputStream().write(("裝置已解除註冊!!").getBytes("utf-8"));
		}else if(action.equals("clear")){
			platform = request.getParameter("platform");
			System.out.println("Platform: " + platform);
			DeviceTokenHolder.clear(platform);
			response.getOutputStream().write(("已解除所有註冊!!").getBytes("utf-8"));
		}
	}

}
