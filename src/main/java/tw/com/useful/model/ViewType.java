package tw.com.useful.model;

import com.mongodb.BasicDBObject;

public class ViewType extends BasicDBObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ViewType(){
		
	}

	public String getName() {
		return getString("name");
	}

	public void setName(String name) {
		put("name", name);
	}
}
