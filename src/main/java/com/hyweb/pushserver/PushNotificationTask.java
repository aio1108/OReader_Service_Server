package com.hyweb.pushserver;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javapns.Push;
import javapns.notification.PushedNotifications;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.hyweb.runner.util.SpringContext;

public class PushNotificationTask implements Runnable {
	Logger log = Logger.getLogger(this.getClass());
	private static ResourceBundle appRb = PropertyResourceBundle.getBundle("config");
	
	public void run() {
		// TODO Auto-generated method stub
		log.info("\n scan the db and looking for news need to be pushed....");
		DataSource dsGIP = (DataSource)SpringContext.getBean("dataSourceGIP");
		//DataSource dsPush = (DataSource)SpringContext.getBean("dataSource");
		JdbcTemplate jdbcTemplate = (JdbcTemplate)SpringContext.getBean("jdbcTemplate");
		jdbcTemplate.setDataSource(dsGIP);
		StringBuffer sqlSb = new StringBuffer();
		sqlSb.append("SELECT a.sTitle, a.iCUItem FROM CuDtGeneric as a, multiform as b ");
		sqlSb.append("WHERE a.iCUItem = b.giCuItem ");
		sqlSb.append("AND a.iCTUnit = '1818' ");
		sqlSb.append("AND a.topCat IN ('02', '03', '04', '05') ");
		sqlSb.append("AND a.fCTUPublic = 'Y' ");
		sqlSb.append("AND b.CuDTx29F81 = 'Y' ");
		sqlSb.append("AND (b.CuDTx29F82 != '1' OR b.CuDTx29F82 is null) ");
		List resultData = jdbcTemplate.queryForList(sqlSb.toString());
		for(int i=0;i<resultData.size();i++){
			Map obj = (Map)resultData.get(i);
			updatePushStatus(obj.get("iCUItem").toString());
			log.info("\n push message to GCM service....");
			log.info("\n item id = " + obj.get("iCUItem").toString() + " ....");
			log.info("\n message title = " + obj.get("sTitle").toString() + " ....");
			pushMessageToAndroid("最新消息", obj.get("sTitle").toString());
			pushMessageToiOS("最新消息", obj.get("sTitle").toString());
		}
	}
	
	private void updatePushStatus(String xItem){
		log.info("\n update push status after pushed....");
		DataSource dsGIP = (DataSource)SpringContext.getBean("dataSourceGIP");
		//DataSource dsPush = (DataSource)SpringContext.getBean("dataSource");
		JdbcTemplate jdbcTemplate = (JdbcTemplate)SpringContext.getBean("jdbcTemplate");
		jdbcTemplate.setDataSource(dsGIP);
		StringBuffer sqlSb = new StringBuffer();
		sqlSb.append("UPDATE multiform SET CuDTx29F82 = '1' WHERE giCuItem = '" + xItem + "' ");
		jdbcTemplate.execute(sqlSb.toString());
		//jdbcTemplate.setDataSource(dsPush);
	}
	
	public static PushNotificationResult pushMessageToAndroid(String title, String msg){
		try{
			Sender sender = new Sender("AIzaSyA2sLClHpQlAu32tte2Mi_WXv_oErnyTcE");
			//Message message = new Message.Builder().collapseKey("1").timeToLive(259200).delayWhileIdle(false).addData("message", URLEncoder.encode(msg, "UTF-8")).addData("title", title).addData("msgcnt", "1").build();
			Message message = new Message.Builder().timeToLive(259200).delayWhileIdle(false).addData("message", URLEncoder.encode(msg, "UTF-8")).addData("title", title).addData("msgcnt", "1").build();
			Object[] tokens = DeviceTokenHolder.getTokens("android"); 
			int tokensSize = tokens.length;
			
			for(int i=0;i<tokensSize;i++){
				Result obj = sender.send(message, (String)tokens[i], 5);
				if (obj.getMessageId() != null) {
					 String canonicalRegId = obj.getCanonicalRegistrationId();
					 if (canonicalRegId != null) {
					   // same device has more than on registration ID: update database
						 DeviceTokenHolder.unregister((String)tokens[i], "android");
					 }
					 PushLogger.log((String)tokens[i], "OK", "Android");
				} else {
					 String error = obj.getErrorCodeName();
					 PushLogger.log((String)tokens[i], error, "Android");
					 if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
					   // application has been removed from device - unregister database
						 DeviceTokenHolder.unregister((String)tokens[i], "android");
					 }
				}
			}
			return new PushNotificationResult(true, "OK");
		}catch(Exception e){
			PushLogger.log("SYSTEMERROR", e.getMessage(), "Android");
			return new PushNotificationResult(false, e.getMessage());
		}
	}
	
	public static PushNotificationResult pushMessageToAndroid(String title, String msg, String token){
		try{
			String gcmKey = appRb.getString("gcmKey");
			Sender sender = new Sender(gcmKey);
			//Message message = new Message.Builder().collapseKey("1").timeToLive(259200).delayWhileIdle(false).addData("message", URLEncoder.encode(msg, "UTF-8")).addData("title", title).addData("msgcnt", "1").build();
			Message message = new Message.Builder().timeToLive(259200).delayWhileIdle(false).addData("message", URLEncoder.encode(msg, "UTF-8")).addData("title", title).addData("msgcnt", "1").build();
			Result obj = sender.send(message, token, 5);
			if (obj.getMessageId() != null) {
				 String canonicalRegId = obj.getCanonicalRegistrationId();
				 if (canonicalRegId != null) {
				   // same device has more than on registration ID: update database
					 DeviceTokenHolder.unregister(token, "android");
				 }
				 PushLogger.log(token, "OK", "Android");
			} else {
				 String error = obj.getErrorCodeName();
				 PushLogger.log(token, error, "Android");
				 if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
				   // application has been removed from device - unregister database
					 DeviceTokenHolder.unregister(token, "android");
				 }
			}
			return new PushNotificationResult(true, "OK");
		}catch(Exception e){
			System.out.println(e.getMessage());
			return new PushNotificationResult(false, e.getMessage());
		}
	}
	
	public static PushNotificationResult pushMessageToiOS(String title, String msg){
		String p12File = appRb.getString("p12File");
		String p12Password = appRb.getString("p12Key");
		try{
			//need to implement
			List tokenList = new ArrayList<String>();
			Object[] tokens = DeviceTokenHolder.getTokens("ios"); 
			int tokensSize = tokens.length;
			for(int i=0;i<tokensSize;i++){
				tokenList.add((String)tokens[i]);
			}
			int tokenListLength = tokenList.size();
			for(int i=0;i<tokenListLength;i++){
				PushedNotifications p = Push.alert(msg, p12File, p12Password, true, tokenList.get(i).toString());
				if(p.getFailedNotifications().size() == 0){
					PushLogger.log(tokenList.get(i).toString(), "OK", "iOS");
				}else{
					String error = p.getFailedNotifications().get(0).getException().getMessage();
					PushLogger.log(tokenList.get(i).toString(), error, "iOS");
				}
			}
			return new PushNotificationResult(true, "OK");
		}catch(Exception e){
			PushLogger.log("SYSTEMERROR", e.getMessage(), "iOS");
			return new PushNotificationResult(false, e.getMessage());
		}
	}
	
	public static PushNotificationResult pushMessageToiOS(String title, String msg, String token){
		String p12File = appRb.getString("p12File");
		String p12Password = appRb.getString("p12Key");
		try{
			PushedNotifications p = Push.alert(msg, p12File, p12Password, true, token);
			if(p.getFailedNotifications().size() == 0){
				PushLogger.log(token, "OK", "iOS");
			}else{
				String error = p.getFailedNotifications().get(0).getException().getMessage();
				PushLogger.log(token, error, "iOS");
			}
			return new PushNotificationResult(true, "OK");
		}catch(Exception e){
			System.out.println(e.getMessage());
			PushLogger.log("SYSTEMERROR", e.getMessage(), "iOS");
			return new PushNotificationResult(false, e.getMessage());
		}
	}

}
