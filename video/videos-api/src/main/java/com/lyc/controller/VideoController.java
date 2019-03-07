package com.lyc.controller;

import com.lyc.pojo.Bgm;
import com.lyc.pojo.Comments;
import com.lyc.pojo.Videos;
import com.lyc.service.BgmService;
import com.lyc.service.VideoService;
import com.lyc.utils.FFMpeg;
import com.lyc.utils.JSONResult;
import com.lyc.utils.PagedResult;
import com.lyc.utils.enums.VideoStatusEnum;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

@RestController
@Api(value="视频相关业务的接口", tags= {"视频相关业务的controller"})
@RequestMapping("/video")
public class VideoController extends BasicController{

    @Autowired
    private BgmService bgmService;

    @Autowired
    private VideoService videoService;


    @ApiOperation(value="上传视频", notes="上传视频的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId", value="用户id", required=true,
                    dataType="String", paramType="form"),
            @ApiImplicitParam(name="bgmId", value="背景音乐id", required=false,
                    dataType="String", paramType="form"),
            @ApiImplicitParam(name="videoSeconds", value="背景音乐播放长度", required=true,
                    dataType="String", paramType="form"),
            @ApiImplicitParam(name="videoWidth", value="视频宽度", required=true,
                    dataType="String", paramType="form"),
            @ApiImplicitParam(name="videoHeight", value="视频高度", required=true,
                    dataType="String", paramType="form"),
            @ApiImplicitParam(name="desc", value="视频描述", required=false,
                    dataType="String", paramType="form")
    })
    @PostMapping(value="/upload", headers="content-type=multipart/form-data")
    public JSONResult upload(String userId,
                                  String bgmId, double videoSeconds,
                                  int videoWidth, int videoHeight,
                                  String desc,
                                  @ApiParam(value="短视频", required=true)
                                          MultipartFile file) throws Exception {
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("用户id不能为空...");
        }

        String coverPath="";

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        // 文件上传的最终保存路径
        String finalVideoPath = "";
        //向数据库保存的路径
        String MysqlPath="";
        try {
            if (file != null) {
                //获取视频的名字
                String fileName = file.getOriginalFilename();

                if (StringUtils.isNotBlank(fileName)) {
                    // 设置数据库保存的路径
                    MysqlPath="/file/"+userId+"/video/"+fileName;
                    //设置实际保存的路径
                    finalVideoPath=path+MysqlPath;

                    //将视频保存到路径
                    File outFile = new File(finalVideoPath);
                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        // 创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = file.getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                }

            } else {
                return JSONResult.errorMsg("上传出错...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.errorMsg("上传出错...");
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        //新建FFmpeg
        FFMpeg ffMpeg=new FFMpeg(ffmpegPath);

        //如果选择加了bgm，则将视频与bgm合并
        if(StringUtils.isNotBlank(bgmId)){
            Bgm bgm= bgmService.findBgm(bgmId);
            String bgmPath=path+bgm.getPath();
            String videoName= UUID.randomUUID().toString()+".mp4";
            MysqlPath="/file/"+userId+"/video/"+videoName;
            String videoOutPath=path+MysqlPath;
            ffMpeg.converter(finalVideoPath,bgmPath,videoSeconds,videoOutPath);
        }

        coverPath="/file/"+userId+"/video/"+ UUID.randomUUID()+".jpg";

        String coverOutPath=path+coverPath;
        ffMpeg.cover(finalVideoPath,coverOutPath);

        //创建一个video对象并将其保存
        Videos video = new Videos();
        video.setAudioId(bgmId);
        video.setUserId(userId);
        video.setVideoSeconds((float)videoSeconds);
        video.setVideoHeight(videoHeight);
        video.setVideoWidth(videoWidth);
        video.setVideoDesc(desc);
        video.setVideoPath(MysqlPath);
        video.setStatus(VideoStatusEnum.SUCCESS.value);
        video.setCreateTime(new Date());
        video.setCoverPath(coverPath);
        videoService.saveVideo(video);
        return JSONResult.ok();
    }

    @PostMapping("/all")
    @ApiOperation(value = "查询所有视频的接口",notes  = "查询所有视频的controller")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "save", value = "是否保存", required = false,
                    dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "现在显示的页数", required = false,
                    dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示视频的数量", required = false,
                    dataType = "Integer", paramType = "query")
    })
    public JSONResult showAllvideos(Integer save, Integer page,
    Integer pageSize,@RequestBody @ApiParam(name = "videos",value="短视频(可以为空)",
            required=true) Videos videos)throws Exception{


        System.out.println(videos);
        //判断page是否为空
        if(page==null){
            page=1;
        }

        //判断pageSize是否为空，为空则设定每页显示5个视频
        if(pageSize==null) {
            pageSize = 5;
        }
        //调用service查询
        PagedResult pagedResult=videoService.findAllVideo(videos,save,page,pageSize);
        return JSONResult.ok(pagedResult);
    }

    @PostMapping(value="/hot")
    @ApiOperation(value = "查询热搜的接口",notes ="查询热搜的controller" )
    public JSONResult hot() throws Exception {
        return JSONResult.ok(videoService.getHotwords());
    }

    @PostMapping(value="/userLikeVideo")
    @ApiOperation(value = "用户喜欢视频的接口",notes ="用户喜欢视频的controller" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", required =true,
                    dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "videoId", value = "视频Id", required = true,
                    dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "createrId", value = "发布者Id", required = true,
                    dataType = "String", paramType = "query")
    })
    public JSONResult userLikeVideo(String userId,String videoId,String createrId) throws Exception {
        videoService.userLikeVidoes(userId, videoId, createrId);
        return JSONResult.ok();
    }

    @PostMapping(value="/userNotLikeVideo")
    @ApiOperation(value = "用户不再喜欢视频的接口",notes ="用户不再喜欢视频的controller" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "发布者Id", required = true,
                    dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "videoId", value = "发布者Id", required = true,
                    dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "createrId", value = "发布者Id", required = true,
                    dataType = "String", paramType = "query")
    })
    public JSONResult userNotLikeVideo(String userId,String videoId,String createrId) throws Exception {
        videoService.usernotLikeVideos(userId, videoId, createrId);
        return JSONResult.ok();
    }


    @PostMapping(value = "/findMyLikeVideos")
    @ApiOperation(value = "查询用户喜欢的视频",notes="查询用户喜欢的视频的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", required = true,
                    dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "现在显示的页数", required = false,
                    dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示视频的数量", required = false,
                    dataType = "Integer", paramType = "query")
    })
    public JSONResult findMyLikeVideos(String userId,Integer page,Integer pageSize){

        if(page==null){
            page=1;
        }
        if(pageSize==null){
            pageSize=5;
        }

        PagedResult pagedResult=videoService.findMyLikeVideos(userId, page, pageSize);
        return JSONResult.ok(pagedResult);
    }

    @PostMapping(value = "/findMyFollowVideos")
    @ApiOperation(value = "查询用户关注的人的视频",notes = "查询用户关注的人的视频的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", required = true,
                    dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "现在显示的页数", required = false,
                    dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示视频的数量", required = false,
                    dataType = "Integer", paramType = "query")
    })
    public JSONResult findMyFollowVideos(String userId,Integer page,Integer pageSize){
        if (page==null){
            page=1;
        }
        if(pageSize==null){
            pageSize=5;
        }
        PagedResult pagedResult=videoService.findMyFollowVideos(userId, page, pageSize);
        return JSONResult.ok(pagedResult);

    }


    @PostMapping("/saveComment")
    @ApiOperation(value = "保存留言",notes = "保存留言的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fatherCommentId", value = "父评论", required = false,
                    dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "toUserId", value = "被评论人", required = false,
                    dataType = "Integer", paramType = "query")
    })
    public JSONResult saveComment(String fatherCommentId, String toUserId,@RequestBody @ApiParam(name = "comments",
            value="评论信息", required=true)Comments comments){

        comments.setFatherCommentId(fatherCommentId);
        comments.setToUserId(toUserId);
        videoService.saveComment(comments);

        return JSONResult.ok();
    }

    @PostMapping("/findComment")
    @ApiOperation(value = "查询所有的评论", notes = "查询所有评论的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "videoId", value = "视频Id", required = true,
                    dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "现在显示的页数", required = false,
                    dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示视频的数量", required = false,
                    dataType = "Integer", paramType = "query")
    })
    public JSONResult findComment(String videoId,Integer page,Integer pageSize){
        if(page==null){
            page=1;
        }
        if(pageSize==null){
            pageSize=5;
        }

        PagedResult pagedResult=videoService.findComment(videoId, page, pageSize);
        return JSONResult.ok(pagedResult);

    }

}
