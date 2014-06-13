package tw.com.useful.dao;

import java.util.ArrayList;
import java.util.List;

import tw.com.useful.data.model.User;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

public class UserDao extends BaseDao {
	
	public UserDao(){
		collection = database.getCollection("User");
		collection.setObjectClass(User.class);
	}
	
	public User findOne(DBObject query){
		return (User) collection.findOne(query);
	}
	
	public List<User> find(DBObject query){
		DBCursor cursor = collection.find(query);
		return toList(cursor);
	}
	
	public List<User> find(){
		DBCursor cursor = collection.find();
		return toList(cursor);
	}
	
	private List<User> toList(DBCursor cursor) {
		List<User> result = new ArrayList<User>();
		while(cursor.hasNext()){
			result.add((User) cursor.next());
		}
		return result;
	}
	
	public WriteResult save(User user){
		return collection.save(user);
	}
	
	public WriteResult insert(User user){
		return collection.insert(user);
	}
	
	public WriteResult update(User user){
		DBObject objectId = new BasicDBObject();
		objectId.put("_id", user.getId());
		return collection.update(objectId, user);
	}
	
	public WriteResult remove(User user){
		return remove(user.getId());
	}
}
