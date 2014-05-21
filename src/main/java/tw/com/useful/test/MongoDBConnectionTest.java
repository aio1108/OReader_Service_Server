package tw.com.useful.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import tw.com.useful.common.ConfigProperties;
import tw.com.useful.connection.MongoDBConnection;
import tw.com.useful.dao.FieldDao;
import tw.com.useful.dao.MetaDataDao;
import tw.com.useful.dao.ViewTypeDao;
import tw.com.useful.model.Field;
import tw.com.useful.model.MetaData;
import tw.com.useful.model.ViewType;

import com.mongodb.DBRef;

public class MongoDBConnectionTest {

	private MetaDataDao metaDataDao;
	private ViewTypeDao viewTypeDao;
	private FieldDao fieldDao;
	
	@Before
	public void setup(){
		Properties properties = new Properties();
		properties.put("mongodb.host", "127.0.0.1");
		properties.put("mongodb.port", "27017");
		properties.put("mongodb.name", "test");
		ConfigProperties.getInstance().setProperties(properties);
		metaDataDao = new MetaDataDao();
		viewTypeDao = new ViewTypeDao();
		fieldDao = new FieldDao();
		prepareFields();
		prepareViewTypes();
	}
	
	private void prepareViewTypes() {
		ViewType type1 = new ViewType();
		ViewType type2 = new ViewType();
		type1.setName("地圖");
		type2.setName("表格");
		viewTypeDao.save(type1);
		viewTypeDao.save(type2);
		Assert.assertNotNull(type1.get("_id"));
		Assert.assertNotNull(type2.get("_id"));
	}

	private void prepareFields() {
		Field field1 = new Field();
		Field field2 = new Field();
		field1.setName("日期");
		field2.setName("地點");
		fieldDao.save(field1);
		fieldDao.save(field2);
		Assert.assertNotNull(field1.get("_id"));
		Assert.assertNotNull(field2.get("_id"));
	}
	
	@Test
	public void testMetaDataSave(){
		List<Field> fields = fieldDao.find();
		List<ViewType> types = viewTypeDao.find();
		MetaData obj = new MetaData();
		obj.setName("測試資料");
		obj.setDescription("測試用");
		obj.setDefaultViewType(viewTypeDao.getDBRef(types.get(0).get("_id")));
		List fieldList = new ArrayList();
		fieldList.add(fieldDao.getDBRef(fields.get(0).get("_id")));
		fieldList.add(fieldDao.getDBRef(fields.get(1).get("_id")));
		obj.setFields(fieldList);
		List typeList = new ArrayList();
		typeList.add(viewTypeDao.getDBRef(types.get(0).get("_id")));
		typeList.add(viewTypeDao.getDBRef(types.get(1).get("_id")));
		obj.setViewTypes(typeList);
		metaDataDao.save(obj);
		Assert.assertNotNull(obj.get("_id"));
		MetaData queryResult = (MetaData) metaDataDao.findById(obj.get("_id"));
		ViewType defaultType = (ViewType) queryResult.getDefaultViewType().fetch();
		Assert.assertEquals("地圖", defaultType.getName());
		List<DBRef> fieldRefs = queryResult.getFields();
		Assert.assertEquals("日期", ((Field)fieldRefs.get(0).fetch()).getName());
		Assert.assertEquals("地點", ((Field)fieldRefs.get(1).fetch()).getName());
	}
	
	@After
	public void done(){
		fieldDao.removeAll();
		viewTypeDao.removeAll();
		metaDataDao.removeAll();
		MongoDBConnection.getInstance().close();
	}

}
