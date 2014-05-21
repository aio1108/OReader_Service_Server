package tw.com.useful.model;

import com.mongodb.BasicDBObject;

public class Field extends BasicDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Field(){
		
	}

	public String getName() {
		return getString("name");
	}

	public void setName(String name) {
		put("name", name);
	}

}
