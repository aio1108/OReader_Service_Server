package tw.com.useful.service;

import java.util.List;

import tw.com.useful.dao.SubscribeDao;
import tw.com.useful.data.model.Subscribe;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.WriteResult;

public class SubscribeService {
	private SubscribeDao subscribeDao = new SubscribeDao();
	
	public WriteResult insert(Subscribe subscribe){
		return subscribeDao.insert(subscribe);
	}
	
	public WriteResult update(Subscribe subscribe){
		return subscribeDao.update(subscribe);
	}
	
	public WriteResult remove(Subscribe subscribe){
		return subscribeDao.remove(subscribe);
	}
	
	public WriteResult remove(Object id){
		return subscribeDao.remove(id);
	}
	
	public WriteResult removeAll(){
		return subscribeDao.removeAll();
	}
	
	public List<Subscribe> find(){
		return subscribeDao.find();
	}
	
	public DBRef getDBRef(Object id){
		return subscribeDao.getDBRef(id);
	}
	
	public Subscribe findById(Object id){
		DBObject query = new BasicDBObject();
		query.put("_id", id);
		return subscribeDao.findOne(query);
	}
}
