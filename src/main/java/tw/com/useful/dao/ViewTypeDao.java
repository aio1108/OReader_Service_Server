package tw.com.useful.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import tw.com.useful.connection.MongoDBConnection;
import tw.com.useful.data.model.ViewType;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.WriteResult;

public class ViewTypeDao {
	private DB database;
	private DBCollection viewTypeCollection;
	
	public ViewTypeDao(){
		database = MongoDBConnection.getInstance().getDatabase();
		viewTypeCollection = database.getCollection("ViewType");
		viewTypeCollection.setObjectClass(ViewType.class);
	}
	
	public ViewType findById(ObjectId id){
		DBObject objectId = new BasicDBObject();
		objectId.put("_id", id);
		return (ViewType) viewTypeCollection.findOne(objectId);
	}
	
	public List<ViewType> findByName(String fieldName){
		DBObject name = new BasicDBObject();
		name.put("name", name);
		DBCursor cursor = viewTypeCollection.find(name);
		List<ViewType> result = toList(cursor);
		return result;
	}
	
	public List<ViewType> find(){
		DBCursor cursor = viewTypeCollection.find();
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
		return viewTypeCollection.save(viewType);
	}
	
	public WriteResult insert(ViewType viewType){
		return viewTypeCollection.insert(viewType);
	}
	
	public WriteResult update(ViewType viewType){
		DBObject objectId = new BasicDBObject();
		objectId.put("_id", viewType.getId());
		return viewTypeCollection.update(objectId, viewType);
	}
	
	public WriteResult remove(ObjectId id){
		DBObject objectId = new BasicDBObject();
		objectId.put("_id", id);
		return viewTypeCollection.remove(objectId);
	}
	
	public WriteResult remove(ViewType viewType){
		return remove(viewType.getId());
	}
	
	public WriteResult removeAll(){
		return viewTypeCollection.remove(new BasicDBObject());
	}
	
	public DBRef getDBRef(ObjectId id){
		return new DBRef(database, viewTypeCollection.getName(), id);
	}
}
