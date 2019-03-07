package com.lyc.service.impl;

import com.lyc.mapper.UsersFansMapper;
import com.lyc.mapper.UsersLikeVideosMapper;
import com.lyc.mapper.UsersMapper;
import com.lyc.mapper.UsersReportMapper;
import com.lyc.pojo.Users;
import com.lyc.pojo.UsersFans;
import com.lyc.pojo.UsersLikeVideos;
import com.lyc.pojo.UsersReport;
import com.lyc.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper userMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private UsersLikeVideosMapper usersLikeVideosMapper;

    @Autowired
    private UsersFansMapper usersFansMapper;

    @Autowired
    private UsersReportMapper usersReportMapper;



    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean usernameIsExist(String username) {
        Users user = new Users();
        user.setUsername(username);

        Users result = userMapper.selectOne(user);

        return result == null ? false : true;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveUser(Users user) {

        String userId = sid.nextShort();
        user.setId(userId);
        userMapper.insert(user);

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users login(Users user) {
        Users user1=new Users();
        user1.setUsername(user.getUsername());
        user1=userMapper.selectOne(user1);
        if(user.getPassword().equals(user1.getPassword())){
            return user1;
        }else{
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserInfo(Users user) {
        //创建example
        Example userExample = new Example(Users.class);
        Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("id", user.getId());
        //更新
        userMapper.updateByExampleSelective(user, userExample);
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users findUsers(String userId) {
        //创建example
        Example userExample=new Example(Users.class);
        Criteria criteria=userExample.createCriteria();
        criteria.andEqualTo("id",userId);
        //查询
        Users user=userMapper.selectOneByExample(userExample);
        return user;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean userLikeVideoOrNot(String userId, String videoId) {
        if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(videoId)){
            return false;
        }

        Example example=new Example(UsersLikeVideos.class);
        Criteria criteria=example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("videoId",videoId);
        List<UsersLikeVideos> result=usersLikeVideosMapper.selectByExample(example);
        if(result!=null && result.size()>0){
            return true;
        }
        return false;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addFans(String userId, String fansId) {
        String id=sid.nextShort();
        UsersFans usersFans=new UsersFans();
        usersFans.setId(id);
        usersFans.setFanId(fansId);
        usersFans.setUserId(userId);

        usersFansMapper.insert(usersFans);

        userMapper.addFansCount(userId);
        userMapper.addFollersCount(fansId);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteFans(String userId, String fansId) {
        Example example=new Example(UsersFans.class);
        Criteria criteria=example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("fansId",fansId);

        usersFansMapper.deleteByExample(example);

        userMapper.reduceFansCount(userId);
        userMapper.reduceFollersCount(fansId);

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean findFansorNot(String userId, String fansId) {
        Example example=new Example(UsersFans.class);
        Criteria criteria=example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("fanId",fansId);
        List<UsersFans> list=usersFansMapper.selectByExample(example);
        if(list!=null && !list.isEmpty() && list.size()>0){
            return true;
        }

        return false;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void reportUser(UsersReport userReport) {

        String urId = sid.nextShort();
        userReport.setId(urId);
        userReport.setCreateDate(new Date());

        usersReportMapper.insert(userReport);
    }
}
