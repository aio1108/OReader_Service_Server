package tw.com.useful.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import tw.com.useful.data.model.Category;
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
	
	public ViewType findOne(DBObject query){
		return (ViewType) collection.findOne(query);
	}
	
	public List<ViewType> find(DBObject query){
		DBCursor cursor = collection.find(query);
		return toList(cursor);
	}
	
	public List<ViewType> find(){
		DBCursor cursor = collection.find();
		return toList(cursor);
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
	
	public WriteResult remove(ViewType viewType){
		return remove(viewType.getId());
	}
}
