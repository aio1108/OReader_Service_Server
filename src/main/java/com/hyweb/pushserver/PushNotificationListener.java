package com.hyweb.pushserver;

import java.io.IOException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

/**
 * Application Lifecycle Listener implementation class PushNotificationListener
 *
 */
public class PushNotificationListener extends HttpServlet implements ServletContextListener {
	
	Logger log = Logger.getLogger(this.getClass());
	Timer timer = new Timer();
	private static ResourceBundle configRb = PropertyResourceBundle.getBundle("config");
	
    /**
     * Default constructor. 
     */
    public PushNotificationListener() {
        // TODO Auto-generated constructor stub
    }
    
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {//
    
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    	long interval = Long.parseLong(configRb.getString("interval"));
    	log.info("\n PushNotificationListener initialized....");
    	//timer.schedule(new PushNotificationTask(), 10000, interval); //delay 10 sec and execute every 10 sec
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    	log.info("\n PushNotificationListener destoryed....");
    	timer.cancel();
    }
	
}
