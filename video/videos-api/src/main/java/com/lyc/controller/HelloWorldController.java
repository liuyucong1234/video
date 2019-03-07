package com.lyc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "测试好不好使的接口",tags = {"测试好不好使的controller"})
public class HelloWorldController {
	
	@RequestMapping("/hello")
	@ApiOperation(value="返回hello",notes = "返回hello的接口")
	public String Hello() {
		return "Hello Spring Boot~";
	}
	
}
