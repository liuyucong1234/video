package com.lyc.controller;

import com.lyc.pojo.Users;
import com.lyc.pojo.VO.UsersVO;
import com.lyc.service.UserService;
import com.lyc.utils.JSONResult;
import com.lyc.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Api(value="用户登录注册的接口", tags= {"用户登录注册的controller"})
public class RegistLoginController extends BasicController{

    @Autowired
    private UserService userService;

    @ApiOperation(value="用户注册", notes="用户注册的接口")
    @PostMapping("/regist")
    public JSONResult regist(@RequestBody @ApiParam(name = "user",value="用户信息",
            required=true)Users user) throws Exception {

        // 1. 判断用户名和密码必须不为空
        if (user.isBlankOrNot()) {
            return JSONResult.errorMsg("用户名和密码不能为空");
        }

        // 2. 判断用户名是否存在
        boolean usernameIsExist = userService.usernameIsExist(user.getUsername());

        // 3. 保存用户，注册信息
        if (!usernameIsExist) {
            user.prepareToSave();
            userService.saveUser(user);
        } else {
            return JSONResult.errorMsg("用户名已经存在，请换一个再试");
        }

        user.setPassword("");
        //设置登录状态，封装
        UsersVO userVO = setUserRedisSessionToken(user);
        return JSONResult.ok(userVO);
    }

    @ApiOperation(value="用户登录", notes="用户登录的接口")
    @PostMapping("/login")
    public JSONResult login(@RequestBody @ApiParam(name = "user",value="用户信息",
            required=true)Users user) throws Exception{

        Users user1=new Users();
        //判断用户名和密码是否为空
        if(user.isBlankOrNot()){
            return JSONResult.errorMsg("用户名与密码不能为空");
        }

        //判断用户是否存在
        boolean res=userService.usernameIsExist(user.getUsername());
        if(!res){
            return JSONResult.errorMsg("用户不存在");
        }else{
            //判断用户和密码是否正确
            user1.setUsername(user.getUsername());
            user1.setPassword(MD5Utils.getMD5Str(user.getPassword()));
            user1=userService.login(user1);
            if(user1==null){
                return JSONResult.errorMsg("用户名或密码错误");
            }else{
                //设置登录状态
                user1.setPassword("");
                UsersVO userVO = setUserRedisSessionToken(user1);
                return JSONResult.ok(userVO);
            }
        }

    }

    /**
     * @Description:将登录的用户状态加载到redis
     * @param user
     * @return
     */
    private UsersVO setUserRedisSessionToken(Users user) {
        String uniqueToken = UUID.randomUUID().toString();
        redisOperator.set(redis_session + ":" + user.getId(), uniqueToken, 1000 * 60 * 30);

        UsersVO userVO = new UsersVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setUserToken(uniqueToken);
        return userVO;
    }


    @ApiOperation(value="用户注销", notes="用户注销的接口")
    @ApiImplicitParam(name="userId", value="用户id", required=true,
            dataType="String", paramType="query")
    @PostMapping("/logout")
    public JSONResult logout(String userId) throws Exception {
        redisOperator.del(redis_session + ":" + userId);
        return JSONResult.ok();
    }

}
