package tw.com.useful.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import tw.com.useful.common.ConfigProperties;
import tw.com.useful.connection.MongoDBConnection;
import tw.com.useful.dao.MetaDataDao;
import tw.com.useful.dao.ViewTypeDao;
import tw.com.useful.data.model.Field;
import tw.com.useful.data.model.MetaData;
import tw.com.useful.data.model.ViewType;

import com.mongodb.DBRef;

public class MongoDBConnectionTest {

	private MetaDataDao metaDataDao;
	private ViewTypeDao viewTypeDao;
	
	@Before
	public void setup(){
		Properties properties = new Properties();
		properties.put("mongodb.host", "127.0.0.1");
		properties.put("mongodb.port", "27017");
		properties.put("mongodb.name", "test");
		ConfigProperties.getInstance().setProperties(properties);
		metaDataDao = new MetaDataDao();
		viewTypeDao = new ViewTypeDao();
		clearCollections();
		prepareViewTypes();
	}
	
	private void clearCollections() {
		viewTypeDao.removeAll();
		metaDataDao.removeAll();
	}

	private void prepareViewTypes() {
		ViewType type1 = new ViewType("map", "地圖");
		ViewType type2 = new ViewType("table", "表格");
		viewTypeDao.save(type1);
		viewTypeDao.save(type2);
		Assert.assertNotNull(type1.getId());
		Assert.assertNotNull(type2.getId());
	}
	
	//@Ignore
	@Test
	public void testMetaDataSave(){
		List<ViewType> types = viewTypeDao.find();
		MetaData obj = new MetaData();
		obj.setName("測試資料");
		obj.setDescription("測試用");
		obj.setDefaultViewType(viewTypeDao.getDBRef(types.get(0).getId()));
		List fieldList = new ArrayList();
		fieldList.add(new Field("date", "日期"));
		fieldList.add(new Field("place", "地點"));
		obj.setFields(fieldList);
		List typeList = new ArrayList();
		typeList.add(viewTypeDao.getDBRef(types.get(0).getId()));
		typeList.add(viewTypeDao.getDBRef(types.get(1).getId()));
		obj.setViewTypes(typeList);
		metaDataDao.save(obj);
		Assert.assertNotNull(obj.getId());
		MetaData queryResult = (MetaData) metaDataDao.findById(obj.getId());
		ViewType defaultType = (ViewType) queryResult.getDefaultViewType().fetch();
		Assert.assertEquals("地圖", defaultType.getName());
		List<Field> fieldRefs = queryResult.getFields();
		Assert.assertEquals("日期", fieldRefs.get(0).getName());
		Assert.assertEquals("地點", fieldRefs.get(1).getName());
	}
	
	@After
	public void done(){
		viewTypeDao.removeAll();
		metaDataDao.removeAll();
		MongoDBConnection.getInstance().close();
	}

}
