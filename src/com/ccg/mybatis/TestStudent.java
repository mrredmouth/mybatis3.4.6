package com.ccg.mybatis;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import com.ccg.mybatis.student.StudentMapper;
import com.ccg.pojo.Student;

public class TestStudent {
	
	public SqlSessionFactory getSessionFactory() throws IOException{

		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		return sqlSessionFactory;
	}
	/**
	 * SqlSession和connection都是非线程安全,每次使用都要获取新的对象。
	 * mybatis底层实现原理：
	 * SqlSessionFactory -> SqlSession->Mapper接口编程生成的代理对象->增删改查
	 * 四大内置对象：ExecutorHandler,parameterHandler,statementHandler,resultHandler，都用到PluginAll生成
	 * @throws IOException
	 */
	@Test
	public void testStudent() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSessionFactory();
		//获取SqlSession，与数据库的一次会话。默认参数是false，不自动提交;用完要关闭
		SqlSession session = sqlSessionFactory.openSession();
		try {
			{//xml的id方式：通过namespace+id,获取mapper.xml中的语句
				Student stu = session.selectOne("com.ccg.mybatis.student.StudentMapper.selectStudent", "12");
				System.out.println(stu);
			}
			{   //动态代理实现类方式：获取StudentMapper接口的实现类，mybatis会生成一个动态代理对象，作为实现类。
				//接口的方法与Mapper.xml的id相对应
				StudentMapper mapper = session.getMapper(StudentMapper.class);
				Student stu = mapper.selectStudent1(12);
				System.out.println(stu);
			}
			{//动态代理实现类方式。接口的方法用注解的形式,Mapper.xml中没有sql语句
				StudentMapper mapper = session.getMapper(StudentMapper.class);
				Student stu = mapper.selectStudent2(12);
				System.out.println(stu);
			}
		} finally {
			session.close();
		}
		
	}
	
	@Test
	public void testMysql() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSessionFactory();
		SqlSession session = sqlSessionFactory.openSession();
		try {
			{/*测试mysql环境，environments default="dev_mysql"
				selectStudentMysql的语句是mysql语法，指定数据库是mysql
			*/				
				StudentMapper mapper = session.getMapper(StudentMapper.class);
				//Student stu = mapper.selectStudentMysql(61);
				Student stu = mapper.selectStudent61(12);
				System.out.println(stu);
			}
		} finally {
			session.close();
		}
	}
	/**
	 *	增删改的接口方法返回值可以为：Integer,Long,boolean
	 * @throws IOException
	 */
	//增
	@Test
	public void test3() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSessionFactory();
		/*openSession默认参数是false，不自动提交，
		 * sqlSessionFactory.openSession(true);自动提交
		 * */
		SqlSession openSession = sqlSessionFactory.openSession();
		try {
			StudentMapper mapper = openSession.getMapper(StudentMapper.class);
			Student stu = new Student(null,"plle",78);
			mapper.addStudent(stu);
			//mapper.addStudent2(stu);
			
			openSession.commit();//需要手动提交
		} finally {
			openSession.close();
		}
	}
	
	/**
	 * 修改id为3的记录
	 * @throws IOException
	 */
	@Test
	public void test4() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSessionFactory();
		/*默认获取的session是不自动提交事务的*/
		SqlSession openSession = sqlSessionFactory.openSession();
		try {
			StudentMapper mapper = openSession.getMapper(StudentMapper.class);
			Student stu = new Student(3,"hello",112);
			System.out.println(mapper.updateStudent(stu));
			openSession.commit();
		} finally {
			openSession.close();
		}
	}
	//删
	@Test
	public void test5() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSessionFactory();
		/*默认获取的session是不自动提交事务的*/
		SqlSession openSession = sqlSessionFactory.openSession();
		try {
			StudentMapper mapper = openSession.getMapper(StudentMapper.class);
			mapper.deleteStudentById(3);
			openSession.commit();
		} finally {
			openSession.close();
		}
	}

	@Test
	public void test6() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();
		try {
			StudentMapper mapper = openSession.getMapper(StudentMapper.class);
			System.out.println(mapper.getStudentByIdAndName(3, "hello"));
			System.out.println(mapper.getStudentByIdAndName2(3, "hello"));
			System.out.println(mapper.getStudentList("ll"));//返回list
			System.out.println(mapper.getStudentMapOne(3));//返回map，一个对象
			System.out.println(mapper.getStudentMap("%ll%"));//返回map，多个对象
			System.out.println(mapper.getStudentMap2("%ll%"));//返回map，多个对象
		} finally {
			openSession.close();
		}
	}
	
}
