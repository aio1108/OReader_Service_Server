package tw.com.useful.dao;

import java.util.ArrayList;
import java.util.List;

import tw.com.useful.data.model.Subscribe;
import tw.com.useful.data.model.User;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

public class SubscribeDao extends BaseDao {
	
	public SubscribeDao(){
		collection = database.getCollection("Subscribe");
		collection.setObjectClass(Subscribe.class);
	}
	
	public Subscribe findOne(DBObject query){
		return (Subscribe) collection.findOne(query);
	}
	
	public List<Subscribe> find(DBObject query){
		DBCursor cursor = collection.find(query);
		return toList(cursor);
	}
	
	public List<Subscribe> find(){
		DBCursor cursor = collection.find();
		return toList(cursor);
	}
	
	private List<Subscribe> toList(DBCursor cursor) {
		List<Subscribe> result = new ArrayList<Subscribe>();
		while(cursor.hasNext()){
			result.add((Subscribe) cursor.next());
		}
		return result;
	}
	
	public WriteResult save(Subscribe subscribe){
		return collection.save(subscribe);
	}
	
	public WriteResult insert(Subscribe subscribe){
		return collection.insert(subscribe);
	}
	
	public WriteResult update(Subscribe subscribe){
		DBObject objectId = new BasicDBObject();
		objectId.put("_id", subscribe.getId());
		return collection.update(objectId, subscribe);
	}
	
	public WriteResult remove(Subscribe subscribe){
		return remove(subscribe.getId());
	}
}
