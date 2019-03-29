package com.lyc.controller;


import com.lyc.pojo.Users;
import com.lyc.pojo.UsersReport;
import com.lyc.pojo.VO.CreaterVO;
import com.lyc.pojo.VO.UsersVO;
import com.lyc.service.UserService;
import com.lyc.utils.JSONResult;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@Api(value="用户相关业务的接口",tags="用户相关业务的controller")
@RequestMapping("/user")
public class UserController extends BasicController{

    @Autowired
    private UserService userService;

    @Autowired
    private com.lyc.service.middleService middleService;

    @ApiOperation(value = "用户上传头像",notes="用户上传头像的接口")
    @ApiImplicitParam(name="userId",value = "用户Id",required=true,
                    dataType="String", paramType="query")

    @PostMapping(value="/uploadFace")
    public JSONResult uploadFace(String userId, @RequestParam("file") MultipartFile[] files) throws IOException {

        String mysqlPath;
        try {
            //判断file是否为空
            if(files!=null || files.length>0) {
                mysqlPath= middleService.addFile(userId,files);
            }else{
                return JSONResult.errorMsg("上传出错");
            }

        } catch (Exception e){
            return JSONResult.errorMsg("上传出错");
        }
        //将路径保存到数据库
        if(mysqlPath==null){
            return JSONResult.errorMsg("上传出错");
        }
        return JSONResult.ok(mysqlPath);
    }


    @PostMapping("/find")
    @ApiOperation(value = "查询用户信息",notes = "查询用户信息的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId",value = "用户Id",required=true,
                    dataType="String", paramType="query"),
            @ApiImplicitParam(name="fanId",value = "粉丝Id",required=true,
                    dataType="String", paramType="query")
    })
    public JSONResult findUsers(String userId, String fanId){

        //判断useId是否为空
        if(userId==null){
            return JSONResult.errorMsg("用户id不能为空");
        }
        //查询用户
        Users user=userService.findUsers(userId);
        //将信息封装到VO
        UsersVO usersVO=new UsersVO();
        BeanUtils.copyProperties(user,usersVO);
        Boolean result=userService.findFansorNot(userId,fanId);
        usersVO.setFollow(result);
        //返回
        return JSONResult.ok(usersVO);
    }

    @PostMapping("/creater")
    @ApiOperation(value = "查询上传者信息",notes = "查询上传者信息的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId",value = "用户Id",required=true,
                    dataType="String", paramType="query"),
            @ApiImplicitParam(name="videoId",value = "视频Id",required=true,
                    dataType="String", paramType="query"),
            @ApiImplicitParam(name="createrId",value = "上传者Id",required=true,
                    dataType="String", paramType="query")
    })
    public JSONResult findCreater(String userId,String videoId,String createrId){
        if(createrId==null){
            return JSONResult.errorMsg("上传者id为空");
        }

        //查询用户
        Users  user=userService.findUsers(createrId);
        UsersVO usersVO=new UsersVO();
        BeanUtils.copyProperties(user,usersVO);

        boolean userLikeVideo = userService.userLikeVideoOrNot(userId, videoId);

        //封装
        CreaterVO result = new CreaterVO();
        result.setUsersVO(usersVO);
        result.setUserLikeVideo(userLikeVideo);

        return JSONResult.ok(result);
    }

    @PostMapping("/addFans")
    @ApiOperation(value = "关注用户",notes = "关注用户的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId",value = "用户Id",required=true,
                    dataType="String", paramType="query"),
            @ApiImplicitParam(name="fansId",value = "粉丝Id",required=true,
                    dataType="String", paramType="query")
    })
    public JSONResult addFans(String userId,String fansId){
        if(userId==null || fansId==null){
            return JSONResult.errorMsg("该写啥？");
        }
        userService.addFans(userId,fansId);
        return JSONResult.ok("关注成功");
    }


    @PostMapping("/deleteFans")
    @ApiOperation(value = "不再关注用户",notes = "不再关注用户的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId",value = "用户Id",required=true,
                    dataType="String", paramType="query"),
            @ApiImplicitParam(name="fansId",value = "粉丝Id",required=true,
                    dataType="String", paramType="query")
    })
    public JSONResult deleteFans(String userId,String fansId){
        if(userId==null||fansId==null){
            return JSONResult.errorMsg("该写点啥？");
        }
        userService.deleteFans(userId, fansId);
        return JSONResult.ok("取消关注成功");
    }

    @PostMapping("/reportUser")

    public JSONResult reportUser(@RequestBody @ApiParam(name = "usersReport",value="举报信息",
            required=true) UsersReport usersReport) throws Exception {

        // 保存举报信息
        userService.reportUser(usersReport);

        return JSONResult.errorMsg("举报成功...");
    }


}
