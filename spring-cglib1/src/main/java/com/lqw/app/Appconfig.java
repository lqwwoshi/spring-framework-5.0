package com.lqw.app;

import com.lqw.service.ImportRegistrarTest;
import com.lqw.service.IndexService;
import org.springframework.context.annotation.*;

@Configuration
//@Import(ImportRegistrarTest.class)
@Import(IndexService.class)
@ComponentScan("com.lqw")
@EnableAspectJAutoProxy
//@ImportResource("classpath:spring.xml")
public class Appconfig {


}
