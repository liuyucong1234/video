package com.lyc.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface middleService {
    /**
     * 添加文件
     * @param userId
     * @param file
     * @return
     */
    public String addFile(String userId, MultipartFile file)throws IOException;

    /**
     * 添加文件
     * @param userId
     * @param files
     * @return
     */
    public String addFile(String userId,MultipartFile[] files)throws IOException;

    /**
     * 添加临时文件，不会存入数据库
     * @param userId
     * @param file
     * @return
     * @throws IOException
     */
    public String addTempFile(String userId, MultipartFile file)throws IOException;

    /**
     * 添加临时文件，不会存入数据库
     * @param userId
     * @param files
     * @return
     * @throws IOException
     */
    public String addTempFile(String userId,MultipartFile[] files)throws IOException;

    /**
     * 合并视频与bgm
     * @param userId
     * @param tempPath
     * @param bgmId
     * @param videoSecond
     * @throws IOException
     */
    public String addVideoWithBgm(String userId,String tempPath,String bgmId,double videoSecond) throws Exception;

    /**
     * 获取截图
     * @param userId
     * @param videoPath
     * @return
     */
    public String getCover(String userId,String videoPath)throws Exception;

}
