package tw.com.useful.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import tw.com.useful.data.model.MetaData;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

public class MetaDataDao extends BaseDao {
	
	public MetaDataDao(){
		collection = database.getCollection("MetaData");
		collection.setObjectClass(MetaData.class);
	}
	
	public MetaData findById(ObjectId id){
		DBObject objectId = new BasicDBObject();
		objectId.put("_id", id);
		return (MetaData) collection.findOne(objectId);
	}
	
	public List<MetaData> find(){
		DBCursor cursor = collection.find();
		List<MetaData> result = toList(cursor);
		return result;
	}
	
	private List<MetaData> toList(DBCursor cursor) {
		List<MetaData> result = new ArrayList<MetaData>();
		while(cursor.hasNext()){
			result.add((MetaData) cursor.next());
		}
		return result;
	}
	
	public WriteResult save(MetaData metaData){
		return collection.save(metaData);
	}
	
	public WriteResult insert(MetaData metaData){
		return collection.insert(metaData);
	}
	
	public WriteResult update(MetaData metaData){
		DBObject objectId = new BasicDBObject();
		objectId.put("_id", metaData.getId());
		return collection.update(objectId, metaData);
	}
	
	public WriteResult remove(ObjectId id){
		DBObject objectId = new BasicDBObject();
		objectId.put("_id", id);
		return collection.remove(objectId);
	}
	
	public WriteResult remove(MetaData metaData){
		return remove(metaData.getId());
	}
}
