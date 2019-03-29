package com.lyc.mapper;

import com.lyc.pojo.VO.VideosVO;
import com.lyc.pojo.Videos;
import com.lyc.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideosMapperCustom extends MyMapper<Videos> {

    /**
     * @Description:查询所有的视频
     * @param videoDesc
     * @param userId
     * @return
     */
    public List<VideosVO> findAllVideos(@Param("videoDesc") String videoDesc,
                                         @Param("userId") String userId);

    /**
     * @Description:被用户喜欢数累加
     * @param videoId
     */
    public void addUserLike(@Param("videoId") String videoId);

    /**
     * @Description:被用户喜欢数累减
     * @param videoId
     */
    public void deleteUserLike(@Param("videoId") String videoId);

    /**
     * @Description:查询用户喜欢的视频
     * @param userId
     */
    public List<VideosVO> findMyLikeVideo(@Param("userId") String userId);


    public List<VideosVO> findMyFollowVideo(@Param("userId") String userId);
}