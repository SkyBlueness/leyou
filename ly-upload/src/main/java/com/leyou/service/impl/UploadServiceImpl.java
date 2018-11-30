package com.leyou.service.impl;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

    private static final List<String> imagesType = Arrays.asList("image/jpeg","image/png");

    //Fdfs客户端
    @Autowired
    private FastFileStorageClient storageClient;

    @Override
    public String upload(MultipartFile file) {
        //判断file是否为文件
        try {
            String type = file.getContentType();
            //判断文件类型是否匹配
            if(!imagesType.contains(type)){
                System.out.println("文件类型不匹配");
                return null;
            }
            //判断文件内容是否为图片
            BufferedImage image = ImageIO.read(file.getInputStream());
            if(image == null){
                System.out.println("文件上传失败");
                return null;
            }
            //获取文件后缀名
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            //上传文件
            StorePath storePath = storageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), extension, null);
            System.out.println(storePath);
            return "http://image.leyou.com/" + storePath.getFullPath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
