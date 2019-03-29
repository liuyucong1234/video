package com.lyc.controller;

import com.lyc.service.BgmService;
import com.lyc.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bgm")
@Api(value = "bgm相关业务的接口",tags = {"bgm相关业务的controller"})
public class BgmController {

    @Autowired
    private BgmService bgmService;

    @PostMapping("/list")
    @ApiOperation(value = "查询bgm的列表",notes = "查询bgm列表的接口")
    public JSONResult bgmList(){
        return JSONResult.ok(bgmService.bgmList());
    }
}
