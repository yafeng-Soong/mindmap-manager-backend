package com.syf.papermanager.service;

import com.syf.papermanager.exception.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.service
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/21 20:44
 */
public interface FileService {
    String uploadFile(MultipartFile file) throws FileUploadException;
    public void deleteFile(String filePath);
}
