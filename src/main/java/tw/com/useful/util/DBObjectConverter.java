package tw.com.useful.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.mongodb.DBObject;
import com.mongodb.DBRef;

public class DBObjectConverter {

	public static List convert(List<? extends DBObject> list, Class clazz){
		Gson gson = new Gson();
		List result = new ArrayList();
		for(int i = 0;i < list.size();i++){
			result.add(convert(list.get(i), clazz));
		}
		return result;
	}
	
	public static Object convert(DBObject obj, Class clazz){
		Gson gson = new Gson();
		return gson.fromJson(obj.toString(), clazz);
	}
	
	public static List convertDBRef(List<? extends DBRef> list, Class clazz){
		Gson gson = new Gson();
		List result = new ArrayList();
		for(int i = 0;i < list.size();i++){
			result.add(convertDBRef(list.get(i), clazz));
		}
		return result;
	}
	
	public static Object convertDBRef(DBRef ref, Class clazz){
		return convert(ref.fetch(), clazz);
	}
}
