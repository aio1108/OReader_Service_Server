package tw.com.useful.data.model;

import com.mongodb.BasicDBObject;



public class Field extends BasicDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Field(String code, String name){
		put("code", code);
		put("name", name);
	}
	
	public Field(){
		
	}

	public String getName() {
		return getString("name");
	}

	public void setName(String name) {
		put("name", name);
	}
	
	public String getCode() {
		return getString("code");
	}

	public void setCode(String code) {
		put("code", code);
	}

}
