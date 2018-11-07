package com.ccg.mybatis.student;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ccg.pojo.Student;

public interface StudentMapper {

	/**
	 * 方法名与Mapper.xml中的id一致，mybatis生成代理对象实现类，
	 * 	相当于通过接口方法，绑定xml中的sql语句。此方式还可以限制入参类型int
	 * @param id
	 * @return
	 */
	public Student selectStudent(int id);
	
	public Student selectStudent1(int id);
	
	@Select("SELECT * FROM t_student WHERE id = #{id}")
	public Student selectStudent2(int id);
	
	public Student selectStudentMysql(int id);
	public Student selectStudent61(int id);
	/**
	 * 关于参数个数：
	 * 	只有1个时，xml中的sql入参可任意,不作处理:#{id},#{abc}
	 * 	多个时：1)映射的sql里传参用#{param1},#{param2}或者#{arg0},#{arg1}
	 * 			2)在接口方法参数前面加注解@Param("id")@Param("name")，则明确指定名称，sql入参用#{id}#{name}
	 * 			3)使用POJO，则可以用#{id}直接取出
	 * 			4)用map封装，方式同POJO
	 * 			5)用TO，Transfer Object,自己封装一个对象，方式同POJO
	 */
	public Student getStudentByIdAndName(int id,String name);
	public Student getStudentByIdAndName2(@Param("id")int id,@Param("name")String name);
	public List<Student> getStudentList(String name);
	/**
	 * 一条记录封装成Map
	 */
	public Map<String,Object> getStudentMapOne(Integer id);
	/**
	 * 多条记录封装成Map,
	 * @MapKey("id")告诉mybatis返回的map的key使用id
	 */
	@MapKey("name")
	public Map<String,Student> getStudentMap(String name);
	@MapKey("id")
	public Map<Integer,Student> getStudentMap2(String name);
	
	//增删改
	public void addStudent(Student stu);
	public Long addStudent2(Student stu);
	
	public boolean updateStudent(Student stu);
	public void deleteStudentById(int id);
}
