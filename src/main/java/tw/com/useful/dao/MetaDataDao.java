package tw.com.useful.dao;

import tw.com.useful.connection.MongoDBConnection;
import tw.com.useful.model.MetaData;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
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
}
