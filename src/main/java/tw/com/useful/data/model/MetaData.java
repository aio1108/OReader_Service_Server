package tw.com.useful.data.model;

import java.util.List;

import tw.com.useful.util.DBObjectConverter;

import com.mongodb.DBObject;
import com.mongodb.DBRef;

public class MetaData extends BaseDataModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Field> fields;
	
	
	public MetaData(){
		
	}
	
	public MetaData(String name, String description, List<Field> fields, List<DBRef> types, DBRef defaultType, String action){
		put("name", name);
		put("description", description);
		put("fields", fields);
		put("viewTypes", types);
		put("defaultViewType", defaultType);
		put("action", action);
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
	
	public String getAction() {
		return getString("action");
	}

	public void setAction(String action) {
		put("action", action);
	}
}
