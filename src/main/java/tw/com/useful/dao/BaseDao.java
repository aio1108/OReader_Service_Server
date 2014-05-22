package tw.com.useful.dao;

import org.bson.types.ObjectId;

import tw.com.useful.connection.MongoDBConnection;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.WriteResult;

public class BaseDao {
	protected DB database = MongoDBConnection.getInstance().getDatabase();
	protected DBCollection collection;
	
	public WriteResult remove(ObjectId id){
		DBObject objectId = new BasicDBObject();
		objectId.put("_id", id);
		return collection.remove(objectId);
	}
	
	public WriteResult removeAll(){
		return collection.remove(new BasicDBObject());
	}
	
	public DBRef getDBRef(ObjectId id){
		return new DBRef(database, collection.getName(), id);
	}
}
