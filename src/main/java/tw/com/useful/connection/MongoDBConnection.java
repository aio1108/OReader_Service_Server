package tw.com.useful.connection;

import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import tw.com.useful.common.ConfigProperties;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class MongoDBConnection {
	private static MongoDBConnection instance;
	private MongoClient mongoClient;
	private DB database;
	private Logger logger = Logger.getLogger(MongoDBConnection.class);
	
	private MongoDBConnection(){
		String host = ConfigProperties.getInstance().getConfig("mongodb.host");
		int port = Integer.parseInt(ConfigProperties.getInstance().getConfig("mongodb.port"));
		String name = ConfigProperties.getInstance().getConfig("mongodb.name");
		try {
			mongoClient = new MongoClient(host, port);
			database = mongoClient.getDB(name);
		} catch (UnknownHostException e) {
			logger.debug("Unknow MongoDB Host!!");
			e.printStackTrace();
		}
	}
	
	public static MongoDBConnection getInstance(){
		if(instance == null){
			instance = new MongoDBConnection();
		}
		return instance;
	}
	
	public DB getDatabase(){
		return database;
	}
	
	public void close(){
		mongoClient.close();
	}
}
