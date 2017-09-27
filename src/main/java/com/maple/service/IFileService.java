package com.maple.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Maple.Ran on 2017/5/25.
 */
public interface IFileService {
    String upload(MultipartFile file, String path,String folder);
}
