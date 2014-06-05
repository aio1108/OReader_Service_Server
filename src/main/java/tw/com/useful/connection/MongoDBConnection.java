package tw.com.useful.connection;

import java.net.UnknownHostException;
import java.util.Arrays;

import org.apache.log4j.Logger;

import tw.com.useful.common.ConfigProperties;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class MongoDBConnection {
	private static MongoDBConnection instance;
	private MongoClient mongoClient;
	private DB database;
	private Logger logger = Logger.getLogger(MongoDBConnection.class);
	
	private MongoDBConnection(){
		String host = ConfigProperties.getInstance().getConfig("mongodb.host");
		int port = Integer.parseInt(ConfigProperties.getInstance().getConfig("mongodb.port"));
		String name = ConfigProperties.getInstance().getConfig("mongodb.name");
		String user = ConfigProperties.getInstance().getConfig("mongodb.user");
		String password = ConfigProperties.getInstance().getConfig("mongodb.password");
		try {
			MongoCredential credential = MongoCredential.createMongoCRCredential(user, name, password.toCharArray());
			ServerAddress serverAddress = new ServerAddress(host, port);
			mongoClient = new MongoClient(serverAddress, Arrays.asList(credential));
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
