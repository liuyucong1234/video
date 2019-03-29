package com.lyc.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyc.mapper.*;
import com.lyc.pojo.Comments;
import com.lyc.pojo.SearchRecords;
import com.lyc.pojo.UsersLikeVideos;
import com.lyc.pojo.VO.CommentVO;
import com.lyc.pojo.VO.VideosVO;
import com.lyc.pojo.Videos;
import com.lyc.service.VideoService;
import com.lyc.utils.PagedResult;
import com.lyc.utils.TimeAgoUtils;
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
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideosMapper videosMapper;

    @Autowired
    private VideosMapperCustom videosMapperCustom;

    @Autowired
    private SearchRecordsMapper searchRecordsMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private UsersLikeVideosMapper usersLikeVideosMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private CommentsMapperCustom commentsMapperCustom;

    @Autowired
    private CommentsMapper commentsMapper;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String saveVideo(Videos video) {
        String id=sid.nextShort();
        video.setId(id);
        videosMapper.insertSelective(video);
        return id;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public PagedResult findAllVideo(Videos videos,Integer save,Integer page, Integer pageSize) {

        String desc=null;
        String userId=null;
        if(videos!=null){
            desc=videos.getVideoDesc();
            userId=videos.getUserId();

            if(save!=null && save==1){
                SearchRecords searchRecords=new SearchRecords();
                String id=sid.nextShort();
                searchRecords.setId(id);
                searchRecords.setContent(desc);
            }

        }

        //创建pagehelper对象并查询
        PageHelper.startPage(page,pageSize);
        List<VideosVO> videosVOList=videosMapperCustom.findAllVideos(desc,userId);

        //用pageInfo处理结果
        PageInfo<VideosVO> pageInfo=new PageInfo<VideosVO>(videosVOList);

        //将结果封装到pagedResult中
        PagedResult pagedResult=new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setRows(videosVOList);
        pagedResult.setTotal(pageInfo.getPages());
        pagedResult.setRecords(pageInfo.getTotal());

        return pagedResult;
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<String> getHotwords() {
        return searchRecordsMapper.getHotwords();
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void userLikeVidoes(String userId,String videoId,String createrId) {
        //1.将数据存入喜欢表
        UsersLikeVideos usersLikeVideos=new UsersLikeVideos();
        String id=sid.nextShort();
        usersLikeVideos.setId(id);
        usersLikeVideos.setUserId(userId);
        usersLikeVideos.setVideoId(videoId);
        usersLikeVideosMapper.insert(usersLikeVideos);

        //2.更新用户表
        usersMapper.addReceiveLikeCount(createrId);

        //3.更新视频表
        videosMapperCustom.addUserLike(videoId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void usernotLikeVideos(String userId,String videoId,String createrId) {
        //1.将数据从喜欢表中删除
        Example example=new Example(UsersLikeVideos.class);
        Criteria criteria=example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("videoId",videoId);
        usersLikeVideosMapper.deleteByExample(example);

        //2.更新用户表
        usersMapper.reduceReceiveLikeCount(createrId);

        //3.更新视频表
        videosMapperCustom.deleteUserLike(videoId);
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult findMyLikeVideos(String userId, Integer page, Integer pageSize) {

        PageHelper.startPage(page,pageSize);
        List<VideosVO> list=videosMapperCustom.findMyLikeVideo(userId);
        PageInfo<VideosVO> result=new PageInfo<>(list);

        PagedResult pagedResult=new PagedResult();
        pagedResult.setRows(list);
        pagedResult.setRecords(result.getTotal());
        pagedResult.setTotal(result.getPages());
        pagedResult.setPage(page);
        return pagedResult;
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult findMyFollowVideos(String userId, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<VideosVO> list=videosMapperCustom.findMyFollowVideo(userId);
        PageInfo<VideosVO> result=new PageInfo(list);

        PagedResult pagedResult=new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setTotal(result.getPages());
        pagedResult.setRecords(result.getTotal());
        pagedResult.setRows(list);

        return pagedResult;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveComment(Comments comment) {
        String id=sid.nextShort();
        comment.setId(id);
        comment.setCreateTime(new Date());
        commentsMapper.insert(comment);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult findComment(String videoId, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<CommentVO> list=commentsMapperCustom.findComments(videoId);
        for(CommentVO c:list){
            String time= TimeAgoUtils.format(c.getCreateTime());
            c.setTimeAgoStr(time);
        }
        PageInfo<CommentVO> pageInfo = new PageInfo<>(list);

        PagedResult result = new PagedResult();
        result.setTotal(pageInfo.getPages());
        result.setRows(list);
        result.setPage(page);
        result.setRecords(pageInfo.getTotal());

        return result;
    }
}
