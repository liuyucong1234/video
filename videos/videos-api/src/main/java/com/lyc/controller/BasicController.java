package com.lyc.controller;

import com.lyc.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;

public class BasicController {

    @Autowired
    public RedisOperator redisOperator;

    //redis 数据库的名称
    public static final String redis_session="redis-session";

    //文件在硬盘中的实际路径
    public static final String path="C:\\mydata\\appfile";

    //FFmpeg的实际路径
    public static final String ffmpegPath="C:\\myprogram\\ffmpeg\\bin";


}
