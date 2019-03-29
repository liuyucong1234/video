package com.lyc.service.impl;

import com.lyc.pojo.Users;
import com.lyc.service.BgmService;
import com.lyc.service.middleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class middleServiceImpl implements middleService {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private FileServiceImpl fileService;

    @Autowired
    private BgmService bgmService;

    @Override
    public String addFile(String userId, MultipartFile file) throws IOException {
        String mysqlPath=addTempFile(userId,file);
        putinMysql(userId,mysqlPath);
        return mysqlPath;
    }

    @Override
    public String addFile(String userId, MultipartFile[] files) throws IOException{
        String mysqlPath=addTempFile(userId,files);
        putinMysql(userId,mysqlPath);
        return mysqlPath;
    }

    @Override
    public String addTempFile(String userId, MultipartFile file) throws IOException {
        String mysqlPath=getVideoMysqlPath(userId,file);
        MultipartFile[] files=getMultipartFileArray(file);
        fileService.addFile(files,mysqlPath);
        return mysqlPath;
    }

    @Override
    public String addTempFile(String userId, MultipartFile[] files) throws IOException {
        String mysqlPath=getFaceMysqlPath(userId,files);
        fileService.addFile(files,mysqlPath);
        return mysqlPath;
    }

    @Override
    public String addVideoWithBgm(String userId,String tempPath, String bgmId, double videoSecond) throws Exception {
        String videoName= UUID.randomUUID().toString()+".mp4";
        String MysqlPath="/file/"+userId+"/video/"+videoName;
        String bgmPath= bgmService.findBgm(bgmId).getPath();
        fileService.addVideoWithBgm(bgmPath,tempPath,MysqlPath,videoSecond);
        fileService.deleteFile(tempPath);
        return MysqlPath;
    }

    @Override
    public String getCover(String userId, String videoPath) throws Exception{
        String coverPath="/file/"+userId+"/video/"+ UUID.randomUUID()+".jpg";
        fileService.getCover(coverPath,videoPath);
        return coverPath;
    }

    /**
     * 拼接头像文件的路径
     * @param userId
     * @param files
     * @return
     */
    private String getFaceMysqlPath(String userId,MultipartFile[] files) {
        String fileName=files[0].getOriginalFilename();
        return "/file" + userId + "/face/" + fileName;
    }

    /**
     * 拼接视频文件的路径
     * @param userId
     * @param file
     * @return
     */
    private String getVideoMysqlPath(String userId,MultipartFile file) {
        String fileName=file.getOriginalFilename();
        return "/file/" + userId + "/video/" + fileName;
    }

    /**
     * 将文件包装成文件数组
     * @param file
     * @return
     */
    private MultipartFile[] getMultipartFileArray(MultipartFile file){
        MultipartFile[] files=new MultipartFile[1];
        files[0]=file;
        return files;
    }

    /**
     * 将路径保存到数据库
     * @param userId
     * @param mysqlPath
     */
    private void putinMysql(String userId,String mysqlPath){
        Users user=new Users();
        user.setId(userId);
        user.setFaceImage(mysqlPath);
        userService.updateUserInfo(user);
    }
}
