package com.lyc.utils.MyFFMpeg;

/**
 * FFmpeg的对外接口
 */
public interface FFMpeg {
    void amalgamate(String bgmPath, String tempPath, String finalPath, double videoSecond)throws Exception;

    void cover(String videoPath,String outPath)throws Exception;
}
