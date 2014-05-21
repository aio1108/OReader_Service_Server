package tw.com.useful.data.model;

import java.util.List;

import com.mongodb.DBRef;

public class MetaData extends BaseDataModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MetaData(){
		
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

	public List<DBRef> getFields() {
		return (List<DBRef>) get("fields");
	}

	public void setFields(List<DBRef> fields) {
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

	public void setAction(String name) {
		put("action", name);
	}
}
