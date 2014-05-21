package tw.com.useful.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import tw.com.useful.connection.MongoDBConnection;
import tw.com.useful.data.model.Field;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.WriteResult;

public class FieldDao {
	private DB database;
	private DBCollection filedCollection;
	
	public FieldDao(){
		database = MongoDBConnection.getInstance().getDatabase();
		filedCollection = database.getCollection("Field");
		filedCollection.setObjectClass(Field.class);
	}
	
	public Field findById(ObjectId id){
		DBObject objectId = new BasicDBObject();
		objectId.put("_id", id);
		return (Field) filedCollection.findOne(objectId);
	}
	
	public List<Field> findByName(String fieldName){
		DBObject name = new BasicDBObject();
		name.put("name", name);
		DBCursor cursor = filedCollection.find(name);
		List<Field> result = toList(cursor);
		return result;
	}
	
	public List<Field> find(){
		DBCursor cursor = filedCollection.find();
		List<Field> result = toList(cursor);
		return result;
	}
	
	private List<Field> toList(DBCursor cursor) {
		List<Field> result = new ArrayList<Field>();
		while(cursor.hasNext()){
			result.add((Field) cursor.next());
		}
		return result;
	}

	public WriteResult save(Field field){
		return filedCollection.save(field);
	}
	
	public WriteResult insert(Field field){
		return filedCollection.insert(field);
	}
	
	public WriteResult update(Field field){
		DBObject objectId = new BasicDBObject();
		objectId.put("_id", field.getId());
		return filedCollection.update(objectId, field);
	}
	
	public WriteResult remove(ObjectId id){
		DBObject objectId = new BasicDBObject();
		objectId.put("_id", id);
		return filedCollection.remove(objectId);
	}
	
	public WriteResult remove(Field field){
		return remove(field.getId());
	}
	
	public WriteResult removeAll(){
		return filedCollection.remove(new BasicDBObject());
	}
	
	public DBRef getDBRef(ObjectId id){
		return new DBRef(database, filedCollection.getName(), id);
	}
}
