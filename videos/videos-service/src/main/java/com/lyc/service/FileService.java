package com.lyc.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    /**
     * 添加文件
     * @param files
     * @param mysqlPath
     * @return
     * @throws IOException
     */
    public void addFile(MultipartFile[] files,String mysqlPath)throws IOException;

    /**
     * 删除文件
     * @param mysqlPath
     * @throws IOException
     */
    public void deleteFile(String mysqlPath);

    /**
     * 合并视频与bgm
     * @param bgmPath
     * @param tempPath
     * @param finalPath
     * @param videoSecond
     * @throws IOException
     */
    public void addVideoWithBgm(String bgmPath,String tempPath,String finalPath,double videoSecond)throws Exception;

    /**
     * 获取截图
     * @param coverPath
     * @param videoPath
     */
    public void getCover(String coverPath,String videoPath)throws Exception;
}
