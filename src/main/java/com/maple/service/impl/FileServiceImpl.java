package com.maple.service.impl;

import com.google.common.collect.Lists;
import com.maple.service.IFileService;
import com.maple.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Maple.Ran on 2017/5/25.
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {
    private final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file, String path,String folder) {
        String fileName = file.getOriginalFilename();
        //获取扩展名
        String fileExName = fileName.substring(fileName.lastIndexOf(".")+1);
        //生成随机不重复的文件名
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExName;
        logger.info("开始上传文件:文件名:{},路径:{},新文件名:{}", fileName, path, uploadFileName);
        //2.判断路径文件夹是否存在,不存在则创建
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);//设置读写权限
            fileDir.mkdirs();
        }
        //3.生成web项目中的文件地址及文件名
        File targetFile = new File(path, uploadFileName);
        try {
            //4.利用MultipartFile的transferTo方法转存文件到指定路径
            file.transferTo(targetFile);
            //上传至FTP服务器
            FTPUtil.uploadFile(Lists.newArrayList(targetFile),folder);
            targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件异常", e);
            return null;
        }
        return folder+"/"+targetFile.getName();
    }
}
