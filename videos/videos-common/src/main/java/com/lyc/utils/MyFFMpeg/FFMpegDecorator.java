package com.lyc.utils.MyFFMpeg;

public interface FFMpegDecorator extends FFMpeg {

    @Override
    void amalgamate(String bgmPath, String tempPath, String finalPath, double videoSecond) throws Exception;

    @Override
    public void cover(String videoPath, String outPath) throws Exception;

}
