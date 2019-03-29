package com.lyc.mapper;

import com.lyc.pojo.Comments;
import com.lyc.pojo.VO.CommentVO;
import com.lyc.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentsMapperCustom extends MyMapper<Comments> {
	
	//查找评论
	public List<CommentVO> findComments(@Param("videoId")String videoId);
}