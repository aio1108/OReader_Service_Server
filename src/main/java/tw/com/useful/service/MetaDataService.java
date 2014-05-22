package tw.com.useful.service;

import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.WriteResult;

import tw.com.useful.dao.MetaDataDao;
import tw.com.useful.data.model.MetaData;

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
	
	public MetaData findById(ObjectId id){
		DBObject query = new BasicDBObject();
		query.put("_id", id);
		return metaDataDao.findOne(query);
	}
	
	public DBRef getDBRef(ObjectId id){
		return metaDataDao.getDBRef(id);
	}
}
