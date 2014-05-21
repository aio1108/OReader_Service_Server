package tw.com.useful.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.mongodb.DBObject;

public class DBObjectConverter {

	public static List convert(List<DBObject> list, Class clazz){
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
}
