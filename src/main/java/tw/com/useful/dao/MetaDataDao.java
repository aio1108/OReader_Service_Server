package tw.com.useful.dao;

import java.util.ArrayList;
import java.util.List;

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
	
	public List<MetaData> find(DBObject query){
		DBCursor cursor = collection.find(query);
		return toList(cursor);
	}
	
	public MetaData findOne(DBObject query){
		return (MetaData) collection.findOne(query);
	}
	
	public List<MetaData> find(){
		DBCursor cursor = collection.find();
		return toList(cursor);
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
	
	public WriteResult remove(MetaData metaData){
		return remove(metaData.getId());
	}
}
