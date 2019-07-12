package com.zlw.commons.validation;

import com.zlw.commons.core.ResultData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;



@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = {"spring-context.xml"})
public class ValidationTest {
	
	@Autowired
	private UserService userService;
	
	@Test
	public void test(){
		User user = new User();
		user.setUsername("付");
		user.setPassword("123");
		user.setAddr("成都");
		user.setEmail("fukui@21cn.com");
		user.setMoble("17012345678");
		user.setTel("3511318");
		user.setAge(15);
		user.setWorks(10.5f);
		user.setScore(20.66d);
		user.setContent("哈哈哈dss<html>#付奎#</html>");
		user.setIdcard("11362419821230430X");
		user.setIp("255.255.255.255");
		user.setNickName("lssskk");
		user.setReg("fu");
		user.setId(1L);
		
		User2 user2= new User2();
		user2.setReg(1);
		user2.setUsername("sss");
		user2.setColor("");
		user2.setId(0L);
//		user2.setReg(0);
		ResultData<Object> data0 = userService.save(null);
		System.out.println("data0===="+data0);

		
		
		ResultData<Object> data = userService.save(user , user2);
		System.out.println(data);
		
//		ResultData<Object> data = userService.save(1l, 1l,1,22);
//		System.out.println(data);
		
		
		ResultData<List<Object> >data2 =userService.getUserNewsByPageList(1l, 1l, 2, 1);
		System.out.println(data2);
		
	}
	
	
	@Test
	public void testPV303(){
		User2 user = new User2();
		user.setInteger(1);
		user.setNumeric("021333");
		user.setUsername("付");
//		String [] onlys = {"username"};
		ResultData<Object >data2 = PV303.validation(user);
		//ResultData<Object >data2 = PV303.validationOnly(user, onlys);
		if( data2!=null ){
			System.out.println(data2.toString());
		}else{
			System.out.println("PV303 validation SUCCESS!");
		}
	}
	
}
