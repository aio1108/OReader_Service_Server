package tw.com.useful.dao;

import org.bson.types.ObjectId;

import tw.com.useful.connection.MongoDBConnection;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBRef;
import com.mongodb.WriteResult;

public class BaseDao {
	protected DB database = MongoDBConnection.getInstance().getDatabase();
	protected DBCollection collection;
	
	public WriteResult removeAll(){
		return collection.remove(new BasicDBObject());
	}
	
	public DBRef getDBRef(ObjectId id){
		return new DBRef(database, collection.getName(), id);
	}
}
