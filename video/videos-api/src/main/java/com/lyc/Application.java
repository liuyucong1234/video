package com.lyc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages="com.lyc.mapper")
@ComponentScan(basePackages= {"com.lyc", "org.n3r.idworker"})
public class Application {

	//启动类
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
}
