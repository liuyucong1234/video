package com.lyc.mapper;

import com.lyc.pojo.Users;
import com.lyc.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface UsersMapper extends MyMapper<Users> {
	
	/**
	 * @Description: 用户受喜欢数累加
	 */
	public void addReceiveLikeCount(@Param("userId") String userId);
	
	/**
	 * @Description: 用户受喜欢数累减
	 */
	public void reduceReceiveLikeCount(@Param("userId") String userId);
	
	/**
	 * @Description: 增加粉丝数
	 */
	public void addFansCount(@Param("userId") String userId);
	
	/**
	 * @Description: 增加关注数
	 */
	public void addFollersCount(@Param("userId") String userId);
	
	/**
	 * @Description: 减少粉丝数
	 */
	public void reduceFansCount(@Param("userId") String userId);
	
	/**
	 * @Description: 减少关注数
	 */
	public void reduceFollersCount(@Param("userId") String userId);
}