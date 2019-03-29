package com.lyc.utils.MyFFMpeg;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunDecorator implements FFMpegDecorator{

    private ExecutorService pool = Executors.newFixedThreadPool(20);

    private FFMpeg ffMpeg;

    public void RunDecorator(FFMpeg ffMpeg){
        this.ffMpeg=ffMpeg;
    }

    @Override
    public void amalgamate(String bgmPath, String tempPath, String finalPath, double videoSecond) throws Exception {
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    ffMpeg.amalgamate(bgmPath, tempPath, finalPath, videoSecond);
                }catch (Exception e){
                    System.out.println("多线程失败");
                }
            }
        });
    }

    @Override
    public void cover(String videoPath, String outPath) throws Exception {
        pool.submit(new Runnable(){
            @Override
            public void run() {
                try {
                    ffMpeg.cover(videoPath, outPath);
                }catch (Exception e){
                    System.out.println("多线程失败");
                }
            }
        });
    }


}
