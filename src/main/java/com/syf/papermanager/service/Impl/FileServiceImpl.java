package com.syf.papermanager.service.Impl;

import com.syf.papermanager.constant.Constant;
import com.syf.papermanager.exception.PaperException;
import com.syf.papermanager.mapper.PaperMapper;
import com.syf.papermanager.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.service.Impl
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/21 20:45
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {
    //限制文件格式为doc、docx和pdf
    private List<String> fileTypes = Arrays.asList("application/msword", "application/pdf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    @Resource
    PaperMapper paperMapper;
    @Override
    public String uploadFile(MultipartFile file) throws PaperException {
        if (file == null || file.isEmpty()) {
            throw new PaperException("文件上传出错，请检查上传的文件！");
        }
        String fileType = file.getContentType();
        //检查是否是图片
        if (!fileTypes.contains(fileType)){
            throw new PaperException("文件格式错误！支持支doc、docx和pdf");
        }
        // 判断当前项目路径下是否包含上传文件的路径 不包含则创建
        File fileDir = new File(Constant.FILE_PATH);
        File newFile = null;
        if (!fileDir.isDirectory()) {
            fileDir.mkdirs();
        }
        try {
            String originalFilename = file.getOriginalFilename();
            // 上传到服务器上文件的名称 为/当前项目路径/upoladFile/原文件名+当前时间+文件后缀
            newFile = new File(Constant.FILE_PATH + "(" + System.currentTimeMillis() + ")" + originalFilename);
            file.transferTo(newFile);
        } catch (IOException e) {
            log.error("文件上传异常");
            e.printStackTrace();
            throw new PaperException(e.getMessage());
        }
        // 返回文件存储的绝对路径
        return "/files/" + newFile.getName();
    }

    @Override
    public void deleteFile(String filePath) {
        String path = System.getProperty("user.dir") + filePath;
        File file = new File(path);
        if (file.exists())
            file.delete();
    }
}
