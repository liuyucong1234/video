package com.lyc.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FFMpeg {

    private String ffmExe;

    public FFMpeg(String ffmExe){
        super();
        this.ffmExe=ffmExe;
    }

    /**
     * @Description:将视频与音频合并后储存
     * @param videoPath
     * @param mp3path
     * @param time
     * @param outPath
     * @throws Exception
     */
    public void converter(String videoPath,String mp3path,
                          double time,String outPath)throws Exception{

        //写命令
        List<String> commond=new ArrayList();
        commond.add(ffmExe);
        commond.add("-i");
        commond.add(videoPath);
        commond.add("-i");
        commond.add(mp3path);
        commond.add("-t");
        commond.add(String.valueOf(time));
        commond.add("-y");
        commond.add(outPath);

        //执行命令
        ProcessBuilder builder = new ProcessBuilder(commond);
        Process process = builder.start();

        //等待，确保命令已经完成
        InputStream errorStream = process.getErrorStream();
        InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
        BufferedReader br = new BufferedReader(inputStreamReader);

        String line = "";
        while ( (line = br.readLine()) != null ) {
        }


        //关闭
        if (br != null) {
            br.close();
        }
        if (inputStreamReader != null) {
            inputStreamReader.close();
        }
        if (errorStream != null) {
            errorStream.close();
        }

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

    //之前用来测试的
    public static void main(String[] args) {
        FFMpeg ffmpeg = new FFMpeg("D:\\util\\ffmpeg\\bin\\ffmpeg.exe");
        try {
            ffmpeg.converter("D:\\video_20170423_123004.mp4", "D:\\program\\appfile\\bgm\\meijianxue.mp3", 19, "D:\\3.mp4");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
