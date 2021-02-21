package com.lqw.app;

import com.lqw.service.ImportRegistrarTest;
import com.lqw.service.IndexService;
import com.lqw.service.IndexService1;
import org.springframework.context.annotation.*;

@Configuration
//@Import(ImportRegistrarTest.class)
//@Import(IndexService.class)
@ComponentScan("com.lqw")
@EnableAspectJAutoProxy
//@ImportResource("classpath:spring.xml")
public class Appconfig {

	@Bean
	public IndexService indexService(){
		return new IndexService();
	}
	@Bean
	public IndexService1 indexService1(){
		indexService();//？？调用几次？？
		return new IndexService1();

	}
}
