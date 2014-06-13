package tw.com.useful.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import tw.com.useful.common.ConfigProperties;
import tw.com.useful.connection.MongoDBConnection;
import tw.com.useful.data.model.Category;
import tw.com.useful.data.model.Field;
import tw.com.useful.data.model.MetaData;
import tw.com.useful.data.model.Subscribe;
import tw.com.useful.data.model.User;
import tw.com.useful.data.model.ViewType;
import tw.com.useful.service.CategoryService;
import tw.com.useful.service.MetaDataService;
import tw.com.useful.service.SubscribeService;
import tw.com.useful.service.UserService;
import tw.com.useful.service.ViewTypeService;

public class MongoDBConnectionTest {

	private Logger logger = Logger.getLogger(MongoDBConnectionTest.class);
	private MetaDataService metaDataService;
	private ViewTypeService viewTypeService;
	private CategoryService categoryService;
	private UserService userService;
	private SubscribeService subscribeService;
	
	@Before
	public void setup(){
		Properties properties = new Properties();
		properties.put("mongodb.host", "kahana.mongohq.com");
		properties.put("mongodb.port", "10072");
		properties.put("mongodb.name", "TestDB");
		properties.put("mongodb.user", "usefulpeople");
		properties.put("mongodb.password", "iamuseful");
		ConfigProperties.getInstance().setProperties(properties);
		metaDataService = new MetaDataService();
		viewTypeService = new ViewTypeService();
		categoryService = new CategoryService();
		userService = new UserService();
		subscribeService = new SubscribeService();
		clearCollections();
		prepareViewTypes();
		prepareCategories();
	}

	private void prepareCategories() {
		Category category1 = new Category("business", "經濟");
		Category category2 = new Category("travel", "旅遊");
		categoryService.insert(category1);
		categoryService.insert(category2);
		Assert.assertNotNull(category1.getId());
		Assert.assertNotNull(category2.getId());
	}

	private void clearCollections() {
		metaDataService.removeAll();
		viewTypeService.removeAll();
		categoryService.removeAll();
		userService.removeAll();
		subscribeService.removeAll();
	}

	private void prepareViewTypes() {
		ViewType type1 = new ViewType("map", "地圖");
		ViewType type2 = new ViewType("table", "表格");
		viewTypeService.insert(type1);
		viewTypeService.insert(type2);
		Assert.assertNotNull(type1.getId());
		Assert.assertNotNull(type2.getId());
	}
	
	@Ignore
	@Test
	public void testMetaDataSave(){
		List<ViewType> types = viewTypeService.find();
		List<Category> categories = categoryService.find();
		MetaData obj = new MetaData();
		obj.setName("測試資料");
		obj.setDescription("測試用");
		obj.setAction("business_indicator.xml");
		obj.setDefaultViewType(viewTypeService.getDBRef(types.get(0).getId()));
		List categoryList = new ArrayList();
		categoryList.add(categoryService.getDBRef(categories.get(0).getId()));
		obj.setCategory(categoryList);
		List fieldList = new ArrayList();
		fieldList.add(new Field("date", "日期"));
		fieldList.add(new Field("place", "地點"));
		obj.setFields(fieldList);
		List typeList = new ArrayList();
		typeList.add(viewTypeService.getDBRef(types.get(0).getId()));
		typeList.add(viewTypeService.getDBRef(types.get(1).getId()));
		obj.setViewTypes(typeList);
		metaDataService.insert(obj);
		Assert.assertNotNull(obj.getId());
		MetaData queryResult = (MetaData) metaDataService.findById((ObjectId) obj.getId());
		ViewType defaultType = (ViewType) queryResult.getDefaultViewType().fetch();
		Assert.assertEquals("地圖", defaultType.getName());
		List<Field> fieldRefs = queryResult.getFields();
		Assert.assertEquals("日期", fieldRefs.get(0).getName());
		Assert.assertEquals("地點", fieldRefs.get(1).getName());
	}
	
	@Ignore
	@Test
	public void testMetaDataService(){
		List<ViewType> types = viewTypeService.find();
		List<Category> categories = categoryService.find();
		List categoryList = new ArrayList();
		List typeList = new ArrayList();
		List fieldList = new ArrayList();
		fieldList.add(new Field("date", "日期"));
		fieldList.add(new Field("indicator", "指標"));
		typeList.add(viewTypeService.getDBRef(types.get(1).getId()));
		categoryList.add(categoryService.getDBRef(categories.get(0).getId()));
		MetaData obj = new MetaData("景氣指標", "景氣指標的Open Data", fieldList, typeList, viewTypeService.getDBRef(types.get(1).getId()), "business_indicator.xml", categoryList, "1個月", "行政院主計處", "http://tw.yahoo.com");
		metaDataService.insert(obj);
		Assert.assertNotNull(obj.getId());
		fieldList.clear();
		fieldList.add(new Field("year", "年份"));
		fieldList.add(new Field("city", "縣市"));
		fieldList.add(new Field("people", "人數"));
		typeList.clear();
		categoryList.clear();
		typeList.add(viewTypeService.getDBRef(types.get(0).getId()));
		typeList.add(viewTypeService.getDBRef(types.get(1).getId()));
		categoryList.add(categoryService.getDBRef(categories.get(0).getId()));
		categoryList.add(categoryService.getDBRef(categories.get(1).getId()));
		MetaData obj1 = new MetaData("各縣市觀光人數", "各縣市觀光人數的Open Data", fieldList, typeList, viewTypeService.getDBRef(types.get(0).getId()), "city_tourist.xml", categoryList, "6個月", "內政部觀光局", "http://www.google.com");
		metaDataService.insert(obj1);
		Assert.assertNotNull(obj1.getId());
	}
	
	@Test
	public void testUserService(){
		testMetaDataService();
		List<ViewType> types = viewTypeService.find();
		List<MetaData> metas = metaDataService.find();
		Subscribe sub1 = new Subscribe(viewTypeService.getDBRef(types.get(1).getId()), metaDataService.getDBRef(metas.get(0).getId()));
		Subscribe sub2 = new Subscribe(viewTypeService.getDBRef(types.get(0).getId()), metaDataService.getDBRef(metas.get(1).getId()));
		Subscribe sub3 = new Subscribe(viewTypeService.getDBRef(types.get(1).getId()), metaDataService.getDBRef(metas.get(1).getId()));
		subscribeService.insert(sub1);
		subscribeService.insert(sub2);
		subscribeService.insert(sub3);
		List subscribeList1 = new ArrayList();
		subscribeList1.add(subscribeService.getDBRef(sub1.getId()));
		User user1 = new User("weichen@hyweb.com.tw", "1111", subscribeList1);
		List subscribeList2 = new ArrayList();
		subscribeList2.add(subscribeService.getDBRef(sub2.getId()));
		subscribeList2.add(subscribeService.getDBRef(sub3.getId()));
		User user2 = new User("sunnywu@hyweb.com.tw", "2222", subscribeList2);
		userService.insert(user1);
		userService.insert(user2);
		Assert.assertTrue(userService.find().size() > 0);
	}
	
	@After
	public void done(){
		//clearCollections();
		MongoDBConnection.getInstance().close();
	}

}
