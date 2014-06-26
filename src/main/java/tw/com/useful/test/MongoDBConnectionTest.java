package tw.com.useful.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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

import com.mongodb.DBRef;

public class MongoDBConnectionTest {

	private Logger logger = Logger.getLogger(MongoDBConnectionTest.class);
	private MetaDataService metaDataService;
	private ViewTypeService viewTypeService;
	private CategoryService categoryService;
	private UserService userService;
	private SubscribeService subscribeService;
	
	@BeforeClass
	public void setup(){
		Properties properties = new Properties();
		properties.put("mongodb.host", "oceanic.mongohq.com");
		properties.put("mongodb.port", "10030");
		properties.put("mongodb.name", "UsefulDB");
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
		Category business = new Category("business", "經濟");
		Category travel = new Category("travel", "旅遊");
		Category farm = new Category("farm", "農業");
		Category weather = new Category("weather", "天氣");
		Category enviroment = new Category("enviroment", "環境");
		Category finance = new Category("finance", "金融");
		Category peopleware = new Category("peopleware", "人力");
		Category food = new Category("food", "食品");
		categoryService.insert(business);
		categoryService.insert(travel);
		categoryService.insert(farm);
		categoryService.insert(weather);
		categoryService.insert(enviroment);
		categoryService.insert(finance);
		categoryService.insert(peopleware);
		categoryService.insert(food);
		Assert.assertNotNull(business.getId());
		Assert.assertNotNull(travel.getId());
		Assert.assertNotNull(farm.getId());
		Assert.assertNotNull(weather.getId());
		Assert.assertNotNull(enviroment.getId());
		Assert.assertNotNull(finance.getId());
		Assert.assertNotNull(peopleware.getId());
		Assert.assertNotNull(food.getId());
	}

	private void clearCollections() {
		metaDataService.removeAll();
		viewTypeService.removeAll();
		categoryService.removeAll();
		userService.removeAll();
		subscribeService.removeAll();
	}

	private void prepareViewTypes() {
		ViewType map = new ViewType("map", "地圖");
		ViewType table = new ViewType("table", "表格");
		ViewType bar = new ViewType("bar_chart", "長條圖");
		ViewType line = new ViewType("line_chart", "折線圖");
		viewTypeService.insert(map);
		viewTypeService.insert(table);
		viewTypeService.insert(bar);
		viewTypeService.insert(line);
		Assert.assertNotNull(map.getId());
		Assert.assertNotNull(table.getId());
		Assert.assertNotNull(bar.getId());
		Assert.assertNotNull(line.getId());
	}
	
	@Test(enabled=false)
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
	
	@Test
	public void testMetaDataService(){
		createBusinessIndicator();
		createAgriculturalTrade();
		createUltraViolet();
		createStockPublish();
		createHealthFood();
		createGDPYear();
		createGDPQuarter();
		createAirPollution();
		createCreditCard();
	}
	
	private void createCreditCard() {
		List categoryList = new ArrayList();
		List typeList = new ArrayList();
		List fieldList = new ArrayList();
		fieldList.add(new Field("date", "日期"));
		fieldList.add(new Field("cardCount", "流通卡數"));
		fieldList.add(new Field("totalCredit", "簽帳金額合計"));
		fieldList.add(new Field("localCredit", "國內簽帳金額"));
		fieldList.add(new Field("foreignCredit", "國外簽帳金額"));
		fieldList.add(new Field("totalBorrow", "預借現金合計"));
		fieldList.add(new Field("localBorrow", "國內預借現金"));
		fieldList.add(new Field("foreignBorrow", "國外預借現金"));
		fieldList.add(new Field("recursionCredit", "循環信用金額"));
		typeList.add(viewTypeService.getDBRef(viewTypeService.findById("table").getId()));
		typeList.add(viewTypeService.getDBRef(viewTypeService.findById("line_chart").getId()));
		categoryList.add(categoryService.getDBRef(categoryService.findById("finance").getId()));
		categoryList.add(categoryService.getDBRef(categoryService.findById("business").getId()));
		MetaData obj = new MetaData("信用卡業務統計", "信用卡流通卡數、簽帳金額、預借現金、循環信用金額之統計", 
									fieldList, typeList, viewTypeService.getDBRef(viewTypeService.findById("table").getId()), 
									"credit_card.xml", categoryList, "每月", 
									"金融監督管理委員會", "http://data.gov.tw/node/6972");
		metaDataService.insert(obj);
		Assert.assertNotNull(obj.getId());
	}

	private void createAirPollution() {
		List categoryList = new ArrayList();
		List typeList = new ArrayList();
		List fieldList = new ArrayList();
		fieldList.add(new Field("siteName", "測站名稱"));
		fieldList.add(new Field("county", "縣市"));
		fieldList.add(new Field("PSI", "空氣污染指標"));
		fieldList.add(new Field("majorPollutant", "指標污染物"));
		fieldList.add(new Field("status", "狀態"));
		fieldList.add(new Field("SO2", "二氧化硫濃度"));
		fieldList.add(new Field("CO", "ㄧ氧化碳濃度"));
		fieldList.add(new Field("O3", "臭氧濃度"));
		fieldList.add(new Field("PM10", "懸浮微粒濃度"));
		fieldList.add(new Field("PM25", "細懸浮微粒濃度"));
		fieldList.add(new Field("NO2", "二氧化氮濃度"));
		fieldList.add(new Field("windSpeed", "風速"));
		fieldList.add(new Field("windDirec", "風向"));
		fieldList.add(new Field("publishTime", "發佈時間"));
		typeList.add(viewTypeService.getDBRef(viewTypeService.findById("table").getId()));
		typeList.add(viewTypeService.getDBRef(viewTypeService.findById("line_chart").getId()));
		typeList.add(viewTypeService.getDBRef(viewTypeService.findById("map").getId()));
		categoryList.add(categoryService.getDBRef(categoryService.findById("weather").getId()));
		categoryList.add(categoryService.getDBRef(categoryService.findById("enviroment").getId()));
		MetaData obj = new MetaData("空氣品質即時污染指標", "環保署設於全國測站每小時發布之即時空氣品質監測資料", 
									fieldList, typeList, viewTypeService.getDBRef(viewTypeService.findById("table").getId()), 
									"air_pollution.xml", categoryList, "每小時更新", 
									"行政院環境保護署", "http://opendata.epa.gov.tw/Data/Contents/AQX/");
		metaDataService.insert(obj);
		Assert.assertNotNull(obj.getId());
	}

	private void createGDPQuarter() {
		List categoryList = new ArrayList();
		List typeList = new ArrayList();
		List fieldList = new ArrayList();
		fieldList.add(new Field("date", "季"));
		fieldList.add(new Field("GDP", "GDP"));
		typeList.add(viewTypeService.getDBRef(viewTypeService.findById("bar_chart").getId()));
		typeList.add(viewTypeService.getDBRef(viewTypeService.findById("line_chart").getId()));
		categoryList.add(categoryService.getDBRef(categoryService.findById("finance").getId()));
		categoryList.add(categoryService.getDBRef(categoryService.findById("business").getId()));
		MetaData obj = new MetaData("GDP季資料", "按季公布國內生產毛額依支出分(名目金額)統計", 
									fieldList, typeList, viewTypeService.getDBRef(viewTypeService.findById("bar_chart").getId()), 
									"gdp_quarter_indicator.xml", categoryList, "每季", 
									"行政院主計總處", "http://data.gov.tw/node/6800");
		metaDataService.insert(obj);
		Assert.assertNotNull(obj.getId());
	}

	private void createGDPYear() {
		List categoryList = new ArrayList();
		List typeList = new ArrayList();
		List fieldList = new ArrayList();
		fieldList.add(new Field("date", "年份"));
		fieldList.add(new Field("GDP", "GDP"));
		typeList.add(viewTypeService.getDBRef(viewTypeService.findById("bar_chart").getId()));
		typeList.add(viewTypeService.getDBRef(viewTypeService.findById("line_chart").getId()));
		categoryList.add(categoryService.getDBRef(categoryService.findById("finance").getId()));
		categoryList.add(categoryService.getDBRef(categoryService.findById("business").getId()));
		MetaData obj = new MetaData("GDP年資料", "按年公布國內生產毛額依支出分(名目金額)統計", 
									fieldList, typeList, viewTypeService.getDBRef(viewTypeService.findById("bar_chart").getId()), 
									"gdp_year_indicator.xml", categoryList, "每季", 
									"行政院主計總處", "http://data.gov.tw/node/6800");
		metaDataService.insert(obj);
		Assert.assertNotNull(obj.getId());
	}

	private void createHealthFood() {
		List categoryList = new ArrayList();
		List typeList = new ArrayList();
		List fieldList = new ArrayList();
		fieldList.add(new Field("id", "許可證字號"));
		fieldList.add(new Field("name", "中文品名"));
		fieldList.add(new Field("ingredient", "保健功效相關成分"));
		fieldList.add(new Field("effect", "保健功效"));
		fieldList.add(new Field("comment", "備註說明"));
		fieldList.add(new Field("checkDate", "核可日期"));
		fieldList.add(new Field("company", "申請商"));
		fieldList.add(new Field("factory", "製造商"));
		fieldList.add(new Field("checkType", "證況"));
		fieldList.add(new Field("relateURL1", "相關連結1"));
		fieldList.add(new Field("relateURL2", "相關連結2"));
		typeList.add(viewTypeService.getDBRef(viewTypeService.findById("table").getId()));
		categoryList.add(categoryService.getDBRef(categoryService.findById("food").getId()));
		MetaData obj = new MetaData("審核通過之健康食品資料", "審核通過之健康食品資料集", 
									fieldList, typeList, viewTypeService.getDBRef(viewTypeService.findById("table").getId()), 
									"health_food.xml", categoryList, "不定期", 
									"衛生福利部食品藥物管理署", "http://data.gov.tw/node/6951");
		metaDataService.insert(obj);
		Assert.assertNotNull(obj.getId());
	}

	private void createStockPublish() {
		List categoryList = new ArrayList();
		List typeList = new ArrayList();
		List fieldList = new ArrayList();
		fieldList.add(new Field("date", "日期"));
		fieldList.add(new Field("listedCount", "上市公司家數"));
		fieldList.add(new Field("listedCapital", "上市公司資本額"));
		fieldList.add(new Field("listedIncrease", "上市公司資本額成長率"));
		fieldList.add(new Field("listedAmount", "上市公司面值"));
		fieldList.add(new Field("listedValue", "上市公司市值"));
		fieldList.add(new Field("publiclyHoldCount", "上櫃公司家數"));
		fieldList.add(new Field("publiclyHoldCapital", "上櫃公司資本額"));
		fieldList.add(new Field("publiclyHoldIncrease", "上櫃公司資本額成長率"));
		fieldList.add(new Field("publiclyHoldAmount", "上櫃公司面值"));
		fieldList.add(new Field("publiclyHoldValue", "上櫃公司市值"));
		fieldList.add(new Field("unlistedCount", "未上市未上櫃公司家數"));
		fieldList.add(new Field("unlistedCapital", "未上市未上櫃公司資本額"));
		typeList.add(viewTypeService.getDBRef(viewTypeService.findById("table").getId()));
		categoryList.add(categoryService.getDBRef(categoryService.findById("finance").getId()));
		categoryList.add(categoryService.getDBRef(categoryService.findById("business").getId()));
		MetaData obj = new MetaData("公開發行公司股票發行概況", "公開發行上市、上櫃公司股票發行家數、資本額、市值、面值等概況統計表", 
									fieldList, typeList, viewTypeService.getDBRef(viewTypeService.findById("table").getId()), 
									"stock_publish.xml", categoryList, "每月", 
									"金融監督管理委員會", "http://data.gov.tw/node/6649");
		metaDataService.insert(obj);
		Assert.assertNotNull(obj.getId());
	}

	private void createUltraViolet() {
		List categoryList = new ArrayList();
		List typeList = new ArrayList();
		List fieldList = new ArrayList();
		fieldList.add(new Field("siteName", "測站名稱"));
		fieldList.add(new Field("UVI", "紫外線指數"));
		fieldList.add(new Field("publishAgency", "發布機關"));
		fieldList.add(new Field("county", "縣市"));
		fieldList.add(new Field("TWD97Lon", "經度"));
		fieldList.add(new Field("TWD97Lat", "緯度"));
		fieldList.add(new Field("publishTime", "發佈時間"));
		typeList.add(viewTypeService.getDBRef(viewTypeService.findById("map").getId()));
		categoryList.add(categoryService.getDBRef(categoryService.findById("weather").getId()));
		categoryList.add(categoryService.getDBRef(categoryService.findById("enviroment").getId()));
		MetaData obj = new MetaData("紫外線即時監測資料", "環保署和中央氣象局設於全國紫外線測站每小時發布之紫外線監測資料", 
									fieldList, typeList, viewTypeService.getDBRef(viewTypeService.findById("map").getId()), 
									"ultra_violet.xml", categoryList, "每小時更新", 
									"行政院環境保護署", "http://data.gov.tw/node/6076");
		metaDataService.insert(obj);
		Assert.assertNotNull(obj.getId());
	}

	private void createAgriculturalTrade() {
		List categoryList = new ArrayList();
		List typeList = new ArrayList();
		List fieldList = new ArrayList();
		fieldList.add(new Field("date", "交易日期"));
		fieldList.add(new Field("code", "作物代號"));
		fieldList.add(new Field("name", "作物名稱"));
		fieldList.add(new Field("marketCode", "市場代號"));
		fieldList.add(new Field("marketName", "市場名稱"));
		fieldList.add(new Field("upper", "上價"));
		fieldList.add(new Field("middle", "中價"));
		fieldList.add(new Field("down", "下價"));
		fieldList.add(new Field("average", "平均價"));
		fieldList.add(new Field("amount", "交易量"));
		typeList.add(viewTypeService.getDBRef(viewTypeService.findById("table").getId()));
		typeList.add(viewTypeService.getDBRef(viewTypeService.findById("line_chart").getId()));
		categoryList.add(categoryService.getDBRef(categoryService.findById("farm").getId()));
		categoryList.add(categoryService.getDBRef(categoryService.findById("finance").getId()));
		MetaData obj = new MetaData("農產品交易行情", "提供資料包含：交易日期、作物代號、作物名稱、市場代號、市場名稱、上價(元/公斤)、中價(元/公斤)、下價(元/公斤)、平均價(元/公斤) 、交易量(公斤)等欄位資料。", 
									fieldList, typeList, viewTypeService.getDBRef(viewTypeService.findById("table").getId()), 
									"agricultural_trade.xml", categoryList, "每天", 
									"行政院農業委員會", "http://data.gov.tw/node/8066");
		metaDataService.insert(obj);
		Assert.assertNotNull(obj.getId());
	}

	private void createBusinessIndicator() {
		List categoryList = new ArrayList();
		List typeList = new ArrayList();
		List fieldList = new ArrayList();
		fieldList.add(new Field("date", "日期"));
		fieldList.add(new Field("leadGeneralIndicator", "領先指標綜合指數"));
		fieldList.add(new Field("leadGeneralIndicatorWithoutTrend", "領先指標不含趨勢指數"));
		fieldList.add(new Field("sameGeneralIndicator", "同時指標綜合指數"));
		fieldList.add(new Field("sameGeneralIndicatorWithoutTrend", "同時指標不含趨勢指數"));
		fieldList.add(new Field("behindGeneralIndicator", "落後指標綜合指數"));
		fieldList.add(new Field("behindGeneralIndicatorWithoutTrend", "落後指標不含趨勢指數"));
		typeList.add(viewTypeService.getDBRef(viewTypeService.findById("table").getId()));
		typeList.add(viewTypeService.getDBRef(viewTypeService.findById("line_chart").getId()));
		categoryList.add(categoryService.getDBRef(categoryService.findById("business").getId()));
		categoryList.add(categoryService.getDBRef(categoryService.findById("finance").getId()));
		MetaData obj = new MetaData("景氣指標", "領先指標綜合指數、同時指標綜合指數、落後指標綜合指數之數列資料、景氣對策信號數列資料", 
									fieldList, typeList, viewTypeService.getDBRef(viewTypeService.findById("table").getId()), 
									"business_indicator.xml", categoryList, "每月", 
									"國家發展委員會", "http://data.gov.tw/node/6099");
		metaDataService.insert(obj);
		Assert.assertNotNull(obj.getId());
	}

	@Test(dependsOnMethods={"testMetaDataService"})
	public void testUserService(){
		createMockUser("weichen@hyweb.com.tw", "1111");
		createMockUser("sunnywu@hyweb.com.tw", "2222");
		Assert.assertTrue(userService.find().size() > 0);
	}
	
	private void createMockUser(String email, String password) {
		List<MetaData> metas = metaDataService.find();
		List subscribeList = new ArrayList();
		Random dice = new Random();
		int subscribeCount = dice.nextInt(metas.size()) + 1;
		while(subscribeCount > 0){
			int metaDataIndex = dice.nextInt(metas.size());
			MetaData metaData = metas.get(metaDataIndex);
			List<DBRef> viewTypes = metaData.getViewTypes();
			int viewTypeIndex = dice.nextInt(viewTypes.size());
			Subscribe subscribe = new Subscribe(viewTypes.get(viewTypeIndex), metaDataService.getDBRef(metas.get(metaDataIndex).getId()));
			subscribeService.insert(subscribe);
			subscribeList.add(subscribeService.getDBRef(subscribe.getId()));
			metas.remove(metaDataIndex);
			subscribeCount = subscribeCount - 1;
		}
		User user = new User(email, password, subscribeList);
		userService.insert(user);
	}

	@AfterClass
	public void done(){
		//clearCollections();
		MongoDBConnection.getInstance().close();
	}

}
