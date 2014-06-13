package tw.com.useful.data.model;

import java.util.ArrayList;
import java.util.List;

import tw.com.useful.base.ReadableJsonObject;
import tw.com.useful.util.DBObjectConverter;
import tw.com.useful.util.ReadableJsonConverter;

import com.mongodb.DBObject;
import com.mongodb.DBRef;


public class User extends BaseDataModel implements ReadableJsonObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public User(String id, String password){
		put("password", password);
		put("_id", id);
	}
	
	public User(String id, String password, List<DBRef> subscribes){
		this(id, password);
		put("subscribes", subscribes);
	}
	
	public User(){
		
	}

	public String getPassword() {
		return getString("password");
	}

	public void setPassword(String password) {
		put("password", password);
	}
	
	public List<DBRef> getSubscribes() {
		return (List<DBRef>) get("subscribes");
	}

	public void setSubscribes(List<DBRef> subscribes) {
		put("subscribes", subscribes);
	}

	@Override
	public DBObject toReadableJson() {
		User cloneObj = (User) this.clone();
		List<DBRef> subscribesRef = cloneObj.getSubscribes();
		List<DBObject> convertedSubscribes = new ArrayList<DBObject>();
		for(int i = 0;i < subscribesRef.size();i++){
			Subscribe sub = new Subscribe(subscribesRef.get(i).fetch());
			convertedSubscribes.add(ReadableJsonConverter.convert(sub));
		}
		cloneObj.put("subscribes", convertedSubscribes);
		return cloneObj;
	}
}
