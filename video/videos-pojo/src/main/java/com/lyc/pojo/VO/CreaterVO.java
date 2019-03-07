package com.lyc.pojo.VO;

public class CreaterVO {
    public UsersVO getUsersVO() {
        return usersVO;
    }

    public void setUsersVO(UsersVO usersVO) {
        this.usersVO = usersVO;
    }

    public boolean isUserLikeVideo() {
        return userLikeVideo;
    }

    public void setUserLikeVideo(boolean userLikeVideo) {
        this.userLikeVideo = userLikeVideo;
    }

    public  UsersVO usersVO;

    public boolean userLikeVideo;


}
