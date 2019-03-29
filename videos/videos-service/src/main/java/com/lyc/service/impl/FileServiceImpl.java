package com.lyc.service.impl;

import com.lyc.service.FileService;
import com.lyc.utils.MyFFMpeg.FFMpegComponent;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FileServiceImpl implements FileService {

    private final static String realPath="C:\\mydata\\appfile";

    public static final String ffmpegPath="C:\\myprogram\\ffmpeg\\bin";

    @Override
    public void addFile(MultipartFile[] files, String mysqlPath) throws IOException {
        String outPath;
        try {
                outPath = realPath + mysqlPath;
                //创建文件
                File outFile=createFile(outPath);
                //输出文件
                outputFile(outFile,files);
        } catch (Exception e) {
            throw e;
        }
    }


    /**
     * 创建文件
     * @param outPath
     * @return
     */
    private File createFile(String outPath) {
        File outFile = new File(outPath);
        if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
            outFile.getParentFile().mkdirs();
        }
        return outFile;
    }

    /**
     * 输出文件
     * @param outFile
     * @param files
     * @throws IOException
     */
    private void outputFile(File outFile,MultipartFile[] files) throws IOException {
        FileOutputStream fileOutputStream = null;
        InputStream fileInputStream = null;
        try{
            fileOutputStream = new FileOutputStream(outFile);
            fileInputStream = files[0].getInputStream();
            IOUtils.copy(fileInputStream, fileOutputStream);
        }catch (IOException e){
            throw e;
        }finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }
    }

    @Override
    public void addVideoWithBgm(String bgmPath, String tempPath, String finalPath, double videoSecond) throws Exception {
        FFMpegComponent ffMpeg=new FFMpegComponent(ffmpegPath);
        ffMpeg.amalgamate(realPath+bgmPath,realPath+tempPath,realPath+finalPath, videoSecond);
    }


    @Override
    public void deleteFile(String mysqlPath) {
        File file=new File(realPath+mysqlPath);
        if(file.exists()&&file.isFile())
            file.delete();
    }

    @Override
    public void getCover(String coverPath, String videoPath) throws Exception{
        FFMpegComponent ffMpeg=new FFMpegComponent(ffmpegPath);
        ffMpeg.cover(realPath+videoPath,realPath+coverPath);
    }
}
