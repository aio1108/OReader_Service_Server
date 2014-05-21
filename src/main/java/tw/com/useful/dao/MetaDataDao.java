package tw.com.useful.dao;

import java.util.ArrayList;
import java.util.List;

import tw.com.useful.connection.MongoDBConnection;
import tw.com.useful.model.MetaData;
import tw.com.useful.model.ViewType;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.WriteResult;

public class MetaDataDao {
	private DB database;
	private DBCollection metaDataCollection;
	
	public MetaDataDao(){
		database = MongoDBConnection.getInstance().getDatabase();
		metaDataCollection = database.getCollection("MetaData");
		metaDataCollection.setObjectClass(MetaData.class);
	}
	
	public MetaData findById(Object id){
		DBObject objectId = new BasicDBObject();
		objectId.put("_id", id);
		return (MetaData) metaDataCollection.findOne(objectId);
	}
	
	public List<MetaData> find(){
		DBCursor cursor = metaDataCollection.find();
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
		return metaDataCollection.save(metaData);
	}
	
	public WriteResult insert(MetaData metaData){
		return metaDataCollection.insert(metaData);
	}
	
	public WriteResult update(MetaData metaData){
		DBObject objectId = new BasicDBObject();
		objectId.put("_id", metaData.get("_id"));
		return metaDataCollection.update(objectId, metaData);
	}
	
	public WriteResult remove(Object id){
		DBObject objectId = new BasicDBObject();
		objectId.put("_id", id);
		return metaDataCollection.remove(objectId);
	}
	
	public WriteResult remove(MetaData metaData){
		return remove(metaData.get("_id"));
	}
	
	public WriteResult removeAll(){
		return metaDataCollection.remove(new BasicDBObject());
	}
	
	public DBRef getDBRef(Object id){
		return new DBRef(database, metaDataCollection.getName(), id);
	}
}
