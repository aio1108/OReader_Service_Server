package tw.com.useful.data.model;

import java.util.Map;

import org.bson.types.ObjectId;

import tw.com.useful.base.ReadableJsonObject;
import tw.com.useful.util.DBObjectConverter;
import tw.com.useful.util.ReadableJsonConverter;

import com.google.gson.internal.LinkedTreeMap;
import com.mongodb.DBObject;
import com.mongodb.DBRef;

public class Subscribe extends BaseDataModel implements ReadableJsonObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Subscribe(){
		
	}
	
	public Subscribe(DBRef preferViewType, DBRef metaData){
		put("preferViewType", preferViewType);
		put("metaData", metaData);
	}

	public Subscribe(DBObject obj) {
		put("_id", obj.get("_id"));
		put("preferViewType", obj.get("preferViewType"));
		put("metaData", obj.get("metaData"));
	}

	public DBRef getPreferViewType() {
		return (DBRef) get("preferViewType");
	}

	public void setPreferViewType(DBRef preferViewType) {
		put("preferViewType", preferViewType);
	}
	
	public DBRef getMetaData() {
		return (DBRef) get("metaData");
	}

	public void setMetaData(DBRef metaData) {
		put("metaData", metaData);
	}

	@Override
	public DBObject toReadableJson() {
		Subscribe cloneObj = (Subscribe) this.clone();
		ObjectId id = (ObjectId) cloneObj.getId();
		cloneObj.put("_id", id.toHexString());
		ViewType preferViewType = (ViewType) DBObjectConverter.convertDBRef(cloneObj.getPreferViewType(), ViewType.class);
		cloneObj.put("preferViewType", preferViewType);
		DBRef metaDataRef = cloneObj.getMetaData();
		MetaData metaData = new MetaData(metaDataRef.fetch());
		cloneObj.put("metaData", ReadableJsonConverter.convert(metaData));
		return cloneObj;
	}
}
