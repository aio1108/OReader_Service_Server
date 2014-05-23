package tw.com.useful.query;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class CompareQueryObject extends QueryObject {

	private static final long serialVersionUID = 1L;
	
	CompareQueryObject(String operator, String field, Object value){
		DBObject obj = new BasicDBObject();
		obj.put(operator, value);
		put(field, obj);
	}

}
