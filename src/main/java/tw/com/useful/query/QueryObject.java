package tw.com.useful.query;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;

public class QueryObject extends BasicDBObject {

	private static final long serialVersionUID = 1L;
	
	public QueryObject(){
		
	}
	
	public QueryObject(String field, Object value){
		super(field, value);
	}
	
	public QueryObject and(QueryObject obj){
		List<QueryObject> list = new ArrayList<QueryObject>();
		list.add(this);
		list.add(obj);
		QueryObject result = new QueryObject();
		result.put("$and", list);
		return result;
	}
	
	public QueryObject or(QueryObject obj){
		List<QueryObject> list = new ArrayList<QueryObject>();
		list.add(this);
		list.add(obj);
		QueryObject result = new QueryObject();
		result.put("$or", list);
		return result;
	}

}
