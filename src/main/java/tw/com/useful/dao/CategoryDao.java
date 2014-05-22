package tw.com.useful.dao;

import java.util.ArrayList;
import java.util.List;

import tw.com.useful.data.model.Category;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

public class CategoryDao extends BaseDao {
	
	public CategoryDao(){
		collection = database.getCollection("Category");
		collection.setObjectClass(Category.class);
	}
	
	public Category findOne(DBObject query){
		return (Category) collection.findOne(query);
	}
	
	public List<Category> find(DBObject query){
		DBCursor cursor = collection.find(query);
		return toList(cursor);
	}
	
	public List<Category> find(){
		DBCursor cursor = collection.find();
		return toList(cursor);
	}
	
	private List<Category> toList(DBCursor cursor) {
		List<Category> result = new ArrayList<Category>();
		while(cursor.hasNext()){
			result.add((Category) cursor.next());
		}
		return result;
	}
	
	public WriteResult save(Category category){
		return collection.save(category);
	}
	
	public WriteResult insert(Category category){
		return collection.insert(category);
	}
	
	public WriteResult update(Category category){
		DBObject objectId = new BasicDBObject();
		objectId.put("_id", category.getId());
		return collection.update(objectId, category);
	}
	
	public WriteResult remove(Category category){
		return remove(category.getId());
	}
}
