package tw.com.useful.util;

import java.util.ArrayList;
import java.util.List;

import tw.com.useful.base.ReadableJsonObject;

import com.mongodb.DBObject;

public class ReadableJsonConverter {
	public static DBObject convert(ReadableJsonObject obj){
		return obj.toReadableJson();
	}
	
	public static List<DBObject> convert(List<? extends ReadableJsonObject> objs){
		List<DBObject> result = new ArrayList<DBObject>();
		for(int i = 0;i < objs.size();i++){
			result.add(convert(objs.get(i)));
		}
		return result;
	}
}
