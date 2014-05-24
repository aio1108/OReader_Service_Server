package tw.com.useful.service;

import java.util.List;

import org.bson.types.ObjectId;

import tw.com.useful.dao.ViewTypeDao;
import tw.com.useful.data.model.ViewType;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.WriteResult;

public class ViewTypeService {
	private ViewTypeDao viewTypeDao = new ViewTypeDao();
	
	public WriteResult insert(ViewType type){
		return viewTypeDao.insert(type);
	}
	
	public WriteResult update(ViewType type){
		return viewTypeDao.update(type);
	}
	
	public WriteResult remove(ViewType type){
		return viewTypeDao.remove(type);
	}
	
	public WriteResult remove(Object id){
		return viewTypeDao.remove(id);
	}
	
	public WriteResult removeAll(){
		return viewTypeDao.removeAll();
	}
	
	public List<ViewType> find(){
		return viewTypeDao.find();
	}
	
	public DBRef getDBRef(Object id){
		return viewTypeDao.getDBRef(id);
	}
	
	public ViewType findById(Object id){
		DBObject query = new BasicDBObject();
		query.put("_id", id);
		return viewTypeDao.findOne(query);
	}
	
	public List<ViewType> findByName(String typeName){
		DBObject query = new BasicDBObject();
		query.put("name", typeName);
		return viewTypeDao.find(query);
	}
}
