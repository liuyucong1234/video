package com.lyc.service;

import com.lyc.pojo.Users;
import com.lyc.pojo.UsersReport;

public interface UserService {

    /**
     * @Description: 判断用户名是否存在
     */
    public boolean usernameIsExist(String username);

    /**
     * @Description: 保存用户(用户注册)
     */
    public void saveUser(Users user);

    /**
     * @Description: 登录
     */

    public Users login(Users user);

    /**
     * @Description: 更新用户头像
     */

    public void updateUserInfo(Users user);

    /**
     * @Description:查询用户信息
     */

    public Users findUsers(String userId);

    /**
     * @Description:查询用户是否点赞视频
     * @param userId
     * @param videoId
     * @return
     */
    public boolean userLikeVideoOrNot(String userId,String videoId);


    /**
     * @Description:增加关注
     * @param userId
     * @param fansId
     */
    public void addFans(String userId,String fansId);

    /**
     * @Description:取消关注
     * @param userId
     * @param fansId
     */
    public void deleteFans(String userId,String fansId);

    /**
     * @Description:查询是否为粉丝
     * @param userId
     * @param fansId
     * @return
     */
    public boolean findFansorNot(String userId,String fansId);

    /**
     * @Description: 举报用户
     */
    public void reportUser(UsersReport userReport);
}
