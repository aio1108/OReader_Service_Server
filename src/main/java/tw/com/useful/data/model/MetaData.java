package tw.com.useful.data.model;

import java.util.List;

import org.bson.types.ObjectId;

import tw.com.useful.base.ReadableJsonObject;
import tw.com.useful.util.DBObjectConverter;

import com.mongodb.DBObject;
import com.mongodb.DBRef;

public class MetaData extends BaseDataModel implements ReadableJsonObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MetaData(){
		
	}
	
	public MetaData(String name, String description, List<Field> fields, List<DBRef> types, DBRef defaultType, String action, List<DBRef> category, String frequency, String department, String source){
		put("name", name);
		put("description", description);
		put("fields", fields);
		put("viewTypes", types);
		put("defaultViewType", defaultType);
		put("action", action);
		put("category", category);
		put("frequency", frequency);
		put("department", department);
		put("source", source);
	}

	public MetaData(DBObject obj) {
		put("_id", obj.get("_id"));
		put("name", obj.get("name"));
		put("description", obj.get("description"));
		put("fields", obj.get("fields"));
		put("viewTypes", obj.get("viewTypes"));
		put("defaultViewType", obj.get("defaultViewType"));
		put("action", obj.get("action"));
		put("category", obj.get("category"));
		put("frequency", obj.get("frequency"));
		put("department", obj.get("department"));
		put("source", obj.get("source"));
	}

	public String getName() {
		return getString("name");
	}

	public void setName(String name) {
		put("name", name);
	}

	public String getDescription() {
		return getString("description");
	}

	public void setDescription(String description) {
		put("description", description);
	}
	
	public List<Field> getFields() {
		return DBObjectConverter.convert((List<DBObject>) get("fields"), Field.class);
	}

	public void setFields(List<Field> fields) {
		put("fields", fields);
	}

	public List<DBRef> getViewTypes() {
		return (List<DBRef>) get("viewTypes");
	}

	public void setViewTypes(List<DBRef> viewTypes) {
		put("viewTypes", viewTypes);
	}

	public DBRef getDefaultViewType() {
		return (DBRef) get("defaultViewType");
	}

	public void setDefaultViewType(DBRef defaultViewType) {
		put("defaultViewType", defaultViewType);
	}
	
	public List<DBRef> getCategory() {
		return (List<DBRef>) get("category");
	}

	public void setCategory(List<DBRef> category) {
		put("category", category);
	}
	
	public String getAction() {
		return getString("action");
	}

	public void setAction(String action) {
		put("action", action);
	}
	
	public String getFrequency() {
		return getString("frequency");
	}

	public void setFrequency(String frequency) {
		put("frequency", frequency);
	}
	
	public String getDepartment() {
		return getString("department");
	}

	public void setDepartment(String department) {
		put("department", department);
	}
	
	public String getSource() {
		return getString("source");
	}

	public void setSource(String source) {
		put("source", source);
	}

	@Override
	public DBObject toReadableJson() {
		MetaData cloneObj = (MetaData) this.clone();
		ObjectId id = (ObjectId) cloneObj.getId();
		cloneObj.put("_id", id.toHexString());
		ViewType defaultViewType = (ViewType) DBObjectConverter.convertDBRef(cloneObj.getDefaultViewType(), ViewType.class);
		cloneObj.put("defaultViewType", defaultViewType);
		List<ViewType> viewTypes = DBObjectConverter.convertDBRef(cloneObj.getViewTypes(), ViewType.class);
		cloneObj.put("viewTypes", viewTypes);
		List<Category> category = DBObjectConverter.convertDBRef(cloneObj.getCategory(), Category.class);
		cloneObj.put("category", category);
		return cloneObj;
	}
}
