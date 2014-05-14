package com.hyweb.pushserver;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.hyweb.runner.util.SpringContext;

public class DeviceTokenHolder {
	
	public static void register(String rid, String platform){
		JdbcTemplate jdbcTemplate = (JdbcTemplate)SpringContext.getBean("jdbcTemplate");
		DataSource dsPush = (DataSource)SpringContext.getBean("dataSource");
		jdbcTemplate.setDataSource(dsPush);
		StringBuffer sqlSb = new StringBuffer();
		String targetTable = (platform.equals("ios"))?"iOSToken":"AndroidToken";
		sqlSb.append("SELECT token_id FROM " + targetTable + " WHERE token_id = '" + rid + "' ");
		List resultData = jdbcTemplate.queryForList(sqlSb.toString());
		if(resultData.size() == 0){
			StringBuffer sqlSb_Insert = new StringBuffer();
			sqlSb_Insert.append("INSERT INTO " + targetTable + " (token_id) VALUES('" + rid + "') ");
			jdbcTemplate.execute(sqlSb_Insert.toString());
		}
	}
	
	public static void unregister(String rid, String platform){
		JdbcTemplate jdbcTemplate = (JdbcTemplate)SpringContext.getBean("jdbcTemplate");
		DataSource dsPush = (DataSource)SpringContext.getBean("dataSource");
		jdbcTemplate.setDataSource(dsPush);
		StringBuffer sqlSb = new StringBuffer();
		String targetTable = (platform.equals("ios"))?"iOSToken":"AndroidToken";
		sqlSb.append("DELETE FROM " + targetTable + " WHERE token_id = '" + rid + "' ");
		jdbcTemplate.execute(sqlSb.toString());
	}
	
	public static void clear(String platform){
		JdbcTemplate jdbcTemplate = (JdbcTemplate)SpringContext.getBean("jdbcTemplate");
		DataSource dsPush = (DataSource)SpringContext.getBean("dataSource");
		jdbcTemplate.setDataSource(dsPush);
		StringBuffer sqlSb = new StringBuffer();
		String targetTable = (platform.equals("ios"))?"iOSToken":"AndroidToken";
		sqlSb.append("DELETE FROM " + targetTable);
		jdbcTemplate.execute(sqlSb.toString());
	}
	
	public static Object[] getTokens(String platform){
		JdbcTemplate jdbcTemplate = (JdbcTemplate)SpringContext.getBean("jdbcTemplate");
		DataSource dsPush = (DataSource)SpringContext.getBean("dataSource");
		jdbcTemplate.setDataSource(dsPush);
		StringBuffer sqlSb = new StringBuffer();
		String targetTable = (platform.equals("ios"))?"iOSToken":"AndroidToken";
		Set tokenList = new HashSet();
		sqlSb.append("SELECT token_id FROM " + targetTable);
		List resultData = jdbcTemplate.queryForList(sqlSb.toString());
		for(int i=0;i<resultData.size();i++){
			Map obj = (Map)resultData.get(i);
			tokenList.add(obj.get("token_id").toString());
		}
		return tokenList.toArray();
	}
}
