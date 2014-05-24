package tw.com.useful.base;

import com.mongodb.DBObject;

public interface ReadableJsonObject {
	public DBObject toReadableJson();
}
