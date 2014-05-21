package tw.com.useful.data.model;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;

public class BaseDataModel extends BasicDBObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ObjectId getId(){
		return getObjectId("_id");
	}
}
