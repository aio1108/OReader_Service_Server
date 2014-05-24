package tw.com.useful.data.model;

import com.mongodb.BasicDBObject;

public class BaseDataModel extends BasicDBObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Object getId(){
		return get("_id");
	}
}
