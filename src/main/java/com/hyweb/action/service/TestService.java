package com.hyweb.action.service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.jdom.Element;
import org.mybatis.spring.SqlSessionTemplate;

import tw.com.useful.runner.util.LogicalService;
import tw.com.useful.runner.util.SpringContext;

public class TestService extends LogicalService
{
	public String myOperation1()
	{
		/* 可以取得的隱含物件如下：
		 * Request object : _request
		 * HttpSession    : _session
		 * JdbcTemplate   : _jdbcTemplate
		 * RmLogger       : _log
		 * 
		 * 其他：
		 * Get bean from Application Context ->  SpringContext.getBean($beanId);
		 */
		
		//get input parameter by parameter name
		String position = (String)this.getInputParameter("position","");
		
		/*
		SqlSessionFactory sqlMapper = (SqlSessionFactory)SpringContext.getBean("sqlSessionFactory");
        SqlSession sqlSession = sqlMapper.openSession();
        
        
		try
		{
			Class targetClass = Class.forName("com.hyweb.nii.dao.sybase.ApplyCaseMapper");
			Object targetObj = targetClass.newInstance();
		} catch (ClassNotFoundException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try
        {  
        	
        	ApplicantMapper mapper = sqlSession.getMapper(targetClass);  
        	
        	//ApplicantMapper mapper = sqlSession.getMapper(ApplicantMapper.class);  
            List<Applicant> allRecords = mapper.selectByExample(null);  
            for (Applicant s : allRecords)
            {
                System.out.println(s.getFirstreceiveno());
            }
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        } finally {  
            sqlSession.close();  
        }
		*/
		
		
        
        SqlSessionTemplate sqlSessionTemplate = (SqlSessionTemplate)SpringContext.getBean("sqlSession");
        
        //sqlSessionTemplate.select
        //sqlSessionTemplate.selectOne(statement, parameter)
        
        //ApplicantExample pet = new ApplicantExample();  
         
        
		
		return position;
	}
	
	public String myOperation2()
	{
		_log.debug("Hello Operation2");
		return "Hello~";
	}
	
	public Map readData()
	{
		Map map = new Hashtable();
		
		return map;
	}
}
