package tw.com.useful.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import tw.com.useful.data.model.Field;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

public class FieldDao extends BaseDao {
	
	public FieldDao(){
		collection = database.getCollection("Field");
		collection.setObjectClass(Field.class);
	}
	
	public Field findById(ObjectId id){
		DBObject objectId = new BasicDBObject();
		objectId.put("_id", id);
		return (Field) collection.findOne(objectId);
	}
	
	public List<Field> findByName(String fieldName){
		DBObject name = new BasicDBObject();
		name.put("name", name);
		DBCursor cursor = collection.find(name);
		List<Field> result = toList(cursor);
		return result;
	}
	
	public List<Field> find(){
		DBCursor cursor = collection.find();
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
		return collection.save(field);
	}
	
	public WriteResult insert(Field field){
		return collection.insert(field);
	}
	
	public WriteResult update(Field field){
		DBObject objectId = new BasicDBObject();
		objectId.put("_id", field.getId());
		return collection.update(objectId, field);
	}
	
	public WriteResult remove(ObjectId id){
		DBObject objectId = new BasicDBObject();
		objectId.put("_id", id);
		return collection.remove(objectId);
	}
	
	public WriteResult remove(Field field){
		return remove(field.getId());
	}
}
