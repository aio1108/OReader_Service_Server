package tw.com.useful.service;

import java.util.List;

import tw.com.useful.dao.UserDao;
import tw.com.useful.data.model.User;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.WriteResult;

public class UserService {
	private UserDao userDao = new UserDao();
	
	public WriteResult insert(User user){
		return userDao.insert(user);
	}
	
	public WriteResult update(User user){
		return userDao.update(user);
	}
	
	public WriteResult remove(User user){
		return userDao.remove(user);
	}
	
	public WriteResult remove(Object id){
		return userDao.remove(id);
	}
	
	public WriteResult removeAll(){
		return userDao.removeAll();
	}
	
	public List<User> find(){
		return userDao.find();
	}
	
	public DBRef getDBRef(Object id){
		return userDao.getDBRef(id);
	}
	
	public User findById(Object id){
		DBObject query = new BasicDBObject();
		query.put("_id", id);
		return userDao.findOne(query);
	}
}
