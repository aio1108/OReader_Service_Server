package com.hyweb.pushserver;

public class PushNotificationResult {
	private boolean status = true;
	private String message = "";
	
	public PushNotificationResult(boolean st, String msg){
		status = st;
		message = msg;
	}
	
	public boolean getStatus(){
		return status;
	}
	
	public String getMessage(){
		return message;
	}
	
}
