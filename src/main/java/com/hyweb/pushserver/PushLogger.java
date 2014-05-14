package com.hyweb.pushserver;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.hyweb.runner.util.SpringContext;

public class PushLogger {
	public static void log(String token, String message, String platform){
		JdbcTemplate jdbcTemplate = (JdbcTemplate)SpringContext.getBean("jdbcTemplate");
		DataSource dsPush = (DataSource)SpringContext.getBean("dataSource");
		jdbcTemplate.setDataSource(dsPush);
		StringBuffer sqlSb = new StringBuffer();
		sqlSb.append(" INSERT INTO PushLog (token, message, platform) VALUES ('" + token + "', '" + message + "', '" + platform + "') ");
		jdbcTemplate.execute(sqlSb.toString());
	}
}
