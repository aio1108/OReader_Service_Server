package tw.com.useful.service;

import java.util.List;

import org.bson.types.ObjectId;

import tw.com.useful.dao.CategoryDao;
import tw.com.useful.data.model.Category;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.WriteResult;

public class CategoryService {
	private CategoryDao categoryDao = new CategoryDao();
	
	public WriteResult insert(Category category){
		return categoryDao.insert(category);
	}
	
	public WriteResult update(Category category){
		return categoryDao.update(category);
	}
	
	public WriteResult remove(Category category){
		return categoryDao.remove(category);
	}
	
	public WriteResult remove(ObjectId id){
		return categoryDao.remove(id);
	}
	
	public WriteResult removeAll(){
		return categoryDao.removeAll();
	}
	
	public List<Category> find(){
		return categoryDao.find();
	}
	
	public DBRef getDBRef(ObjectId id){
		return categoryDao.getDBRef(id);
	}
	
	public Category findById(ObjectId id){
		DBObject query = new BasicDBObject();
		query.put("_id", id);
		return categoryDao.findOne(query);
	}
	
	public List<Category> findByName(String categoryName){
		DBObject query = new BasicDBObject();
		query.put("name", categoryName);
		return categoryDao.find(query);
	}
	
	public List<Category> findByCode(String categoryCode){
		DBObject query = new BasicDBObject();
		query.put("code", categoryCode);
		return categoryDao.find(query);
	}
}
