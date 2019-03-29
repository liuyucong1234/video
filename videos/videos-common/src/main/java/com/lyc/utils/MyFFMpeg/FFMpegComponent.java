package com.lyc.utils.MyFFMpeg;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FFMpegComponent implements FFMpeg {

    private String ffmExe;

    public FFMpegComponent(String ffmExe){
        super();
        this.ffmExe=ffmExe;
    }

    /**
     * 将视频与音频合并后储存
     * @param bgmPath
     * @param tempPath
     * @param finalPath
     * @param videoSecond
     * @throws Exception
     */
    public void amalgamate(String bgmPath, String tempPath, String finalPath, double videoSecond)throws Exception{

        //写命令
        List<String> command=new ArrayList();
        command.add(ffmExe);
        command.add("-i");
        command.add(tempPath);
        command.add("-i");
        command.add(bgmPath);
        command.add("-t");
        command.add(String.valueOf(videoSecond));
        command.add("-y");
        command.add(finalPath);

        excute(command);
    }

    /**
     * @Description:将视频的一帧截图当做封面保存
     * @param videoPath
     * @param outPath
     * @throws Exception
     */
    public void cover(String videoPath,String outPath)throws Exception{

        //写命令
        List<String> command=new ArrayList<>();
        command.add(ffmExe);
        command.add("-ss");
        command.add("00:00:01");
        command.add("-y");
        command.add("-i");
        command.add(videoPath);
        command.add("-vframes");
        command.add("1");
        command.add(outPath);

        excute(command);
    }


    private void excute(List<String> command) throws Exception{
        //执行命令
        ProcessBuilder processBuilder=new ProcessBuilder(command);
        Process process= processBuilder.start();

        //等待，确保命令已经完成
        InputStream inputStream=process.getErrorStream();
        InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
        BufferedReader bufferedReader=new BufferedReader(inputStreamReader);

        String line="";
        while((line=bufferedReader.readLine())!=null){
        }

        //关闭
        if(bufferedReader!=null){
            bufferedReader.close();
        }

        if(inputStreamReader!=null){
            inputStreamReader.close();
        }

        if(inputStream!=null){
            inputStream.close();
        }
    }




}
