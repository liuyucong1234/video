package com.lyc.service;

import com.lyc.pojo.Comments;
import com.lyc.pojo.Videos;
import com.lyc.utils.PagedResult;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface VideoService {

    /**
     * @Description:将视频保存到数据库
     * @param video
     */
    public String saveVideo(Videos video);


    /**
     * @Description:分页查询视频
     */
    public PagedResult findAllVideo(Videos videos,Integer save,Integer page,Integer pageSize);


    /**
     * @Description:查询热搜词
     * @return
     */
    public List<String> getHotwords();

    /**
     * @Description:用户喜欢视频
     */
    public void userLikeVidoes(String userId,String videoId,String createrId);

    /**
     * @Description:用户取消喜欢视频
     */
    public void usernotLikeVideos(String userId,String videoId,String createrId);


    /**
     * @Description:查询用户喜欢的视频
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public PagedResult findMyLikeVideos(String userId,Integer page,Integer pageSize);

    /**
     * @Description:查询用户关注的人的视频
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public PagedResult findMyFollowVideos(String userId,Integer page,Integer pageSize);

    /**
     * @Description:保存留言
     * @param comment
     */
    public void saveComment(Comments comment);

    /**
     * @Description:查询所有留言
     * @param videoId
     * @param page
     * @param pageSize
     * @return
     */
    public PagedResult findComment(String videoId,Integer page,Integer pageSize);
}
