package tw.com.useful.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import tw.com.useful.data.model.ViewType;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

public class ViewTypeDao extends BaseDao {
	
	public ViewTypeDao(){
		collection = database.getCollection("ViewType");
		collection.setObjectClass(ViewType.class);
	}
	
	public ViewType findById(ObjectId id){
		DBObject objectId = new BasicDBObject();
		objectId.put("_id", id);
		return (ViewType) collection.findOne(objectId);
	}
	
	public List<ViewType> findByName(String fieldName){
		DBObject name = new BasicDBObject();
		name.put("name", name);
		DBCursor cursor = collection.find(name);
		List<ViewType> result = toList(cursor);
		return result;
	}
	
	public List<ViewType> find(){
		DBCursor cursor = collection.find();
		List<ViewType> result = toList(cursor);
		return result;
	}
	
	private List<ViewType> toList(DBCursor cursor) {
		List<ViewType> result = new ArrayList<ViewType>();
		while(cursor.hasNext()){
			result.add((ViewType) cursor.next());
		}
		return result;
	}
	
	public WriteResult save(ViewType viewType){
		return collection.save(viewType);
	}
	
	public WriteResult insert(ViewType viewType){
		return collection.insert(viewType);
	}
	
	public WriteResult update(ViewType viewType){
		DBObject objectId = new BasicDBObject();
		objectId.put("_id", viewType.getId());
		return collection.update(objectId, viewType);
	}
	
	public WriteResult remove(ObjectId id){
		DBObject objectId = new BasicDBObject();
		objectId.put("_id", id);
		return collection.remove(objectId);
	}
	
	public WriteResult remove(ViewType viewType){
		return remove(viewType.getId());
	}
}
