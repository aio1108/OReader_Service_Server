package tw.com.useful.service;

import java.util.List;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;

import tw.com.useful.dao.MetaDataDao;
import tw.com.useful.data.model.MetaData;
import tw.com.useful.query.QueryObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.WriteResult;

public class MetaDataService {
	private MetaDataDao metaDataDao = new MetaDataDao();
	
	public WriteResult insert(MetaData metaData){
		return metaDataDao.insert(metaData);
	}
	
	public WriteResult update(MetaData metaData){
		return metaDataDao.update(metaData);
	}
	
	public WriteResult remove(MetaData metaData){
		return metaDataDao.remove(metaData);
	}
	
	public WriteResult remove(ObjectId id){
		return metaDataDao.remove(id);
	}
	
	public WriteResult removeAll(){
		return metaDataDao.removeAll();
	}
	
	public List<MetaData> find(){
		return metaDataDao.find();
	}
	
	public List<MetaData> find(DBObject query){
		return metaDataDao.find(query);
	}
	
	public List<MetaData> findByCategoryAndKeyword(String category, String keyword){
		if(category == null && keyword == null){
			return metaDataDao.find();
		}
		QueryObject query = new QueryObject();
		if(keyword != null){
			Pattern pattern = Pattern.compile("^.*"  + keyword+  ".*$", Pattern.CASE_INSENSITIVE);
			QueryObject name = new QueryObject("name", pattern);
			QueryObject desc = new QueryObject("description", pattern);
			query = name.or(desc);
		}
		if(category != null){
			QueryObject cat = new QueryObject("category.$id", category);
			query = query.and(cat);
		}
		return metaDataDao.find(query);
	}
	
	public MetaData findById(ObjectId id){
		DBObject query = new BasicDBObject();
		query.put("_id", id);
		return metaDataDao.findOne(query);
	}
	
	public DBRef getDBRef(ObjectId id){
		return metaDataDao.getDBRef(id);
	}
}
