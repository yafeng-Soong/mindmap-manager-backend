package com.syf.papermanager.controller;

import com.syf.papermanager.base.BaseController;
import com.syf.papermanager.bean.entity.ResponseEntity;
import com.syf.papermanager.bean.entity.User;
import com.syf.papermanager.bean.enums.ResponseEnums;
import com.syf.papermanager.bean.vo.user.UserResponseVo;
import com.syf.papermanager.constant.Constant;
import com.syf.papermanager.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.controller
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/17 14:27
 */
@Api(tags = "文件上传接口")
@Slf4j
@RestController
@RequestMapping("/fileUpload")
public class FileController extends BaseController {
    //限制图片格式
    private List<String> imgTypes = Arrays.asList("image/jpeg","image/gif","image/png","image/bmp");
    //限制文件格式为doc、docx和pdf
    private List<String> fileTypes = Arrays.asList("application/msword", "application/pdf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

    private static final String ROOT_PATH = System.getProperty("user.dir");
    private static final String HEADER_PATH = ROOT_PATH + "/imgs/header/";
    private static final String FILE_PATH = ROOT_PATH + "/files/";
    private static final String DEFAULT_HEADER = ROOT_PATH + "/imgs/header/default.jpg";

    @Resource
    UserService userService;

    @ApiOperation("上传用户头像接口")
    @ApiImplicitParam(name = "img", value = "图片文件", required = true)
    @PostMapping("/header")
    public ResponseEntity uploadHeader(@RequestParam("img") MultipartFile img){
        ResponseEntity response = new ResponseEntity();
        String fileType = img.getContentType();
        //检查是否是图片
        if (!imgTypes.contains(fileType)){
            response.setCode(ResponseEnums.FILE_TYPE_ERROR.getCode());
            response.setMsg(ResponseEnums.FILE_TYPE_ERROR.getMsg());
            return response;
        }
        Subject subject = SecurityUtils.getSubject();
        User currentUser = (User) subject.getSession().getAttribute("currentUser");
        //删除旧头像
        String oldPath = ROOT_PATH + currentUser.getAvatar();
        File oldImg = new File(oldPath);
        if (!oldPath.equals(DEFAULT_HEADER) && oldImg.exists())
            oldImg.delete();
        //重命名图片为邮箱加后缀
        String[] nameArray = img.getOriginalFilename().split("\\.");
        String imgType = nameArray[nameArray.length - 1];
        String newName =currentUser.getEmail() + "(" + new Date().getTime() + ")" + "." + imgType;
        try {
            img.transferTo(new File(HEADER_PATH + newName));
            //更新用户信息
            currentUser.setAvatar("/imgs/header/" + newName);
            subject.getSession().setAttribute("currentUser", currentUser);
            userService.updateAvatar(currentUser);
            UserResponseVo responseVo = new UserResponseVo();
            BeanUtils.copyProperties(currentUser, responseVo);
            response.setData(responseVo);
        }catch (IOException e){
            e.printStackTrace();
            log.error("文件上传错误：" + e.getMessage());
            response.setCode(ResponseEnums.FILE_UPLOAD_ERROR.getCode());
            response.setMsg(ResponseEnums.FILE_UPLOAD_ERROR.getMsg());
        }catch (Exception e){
            e.printStackTrace();
            response.setCode(ResponseEnums.INNER_ERROR.getCode());
            response.setMsg(ResponseEnums.INNER_ERROR.getMsg());
        }finally {
            return  response;
        }
    }

    @ApiOperation("上传文件接口")
    @ApiImplicitParam(name = "file", value = "文件", required = true)
    @PostMapping("/file")
    public ResponseEntity uploadFile(@RequestParam(value = "file") MultipartFile file) {
        ResponseEntity response = new ResponseEntity();
        if (file == null || file.isEmpty()){
            response.setCode(ResponseEnums.FILE_UPLOAD_ERROR.getCode());
            response.setMsg(ResponseEnums.FILE_UPLOAD_ERROR.getMsg());
            return response;
        }
        String fileType = file.getContentType();
        //检查是否是图片
        if (!fileTypes.contains(fileType)){
            response.setCode(ResponseEnums.FILE_TYPE_ERROR.getCode());
            response.setMsg(ResponseEnums.FILE_TYPE_ERROR.getMsg());
            return response;
        }
        // 判断当前项目路径下是否包含上传文件的路径 不包含则创建
        File fileDir = new File(Constant.FILE_PATH);
        File newFile = null;
        if (!fileDir.isDirectory()){
            fileDir.mkdirs();
        }
        try {
            String originalFilename = file.getOriginalFilename();
            // 上传到服务器上文件的名称 为/当前项目路径/files/原文件名+当前时间+文件后缀
            newFile = new File(Constant.FILE_PATH + "(" + System.currentTimeMillis() + ")" + originalFilename);
            file.transferTo(newFile);
            response.setData("/files/" + newFile.getName());
        } catch (IOException e) {
            log.error("文件上传异常");
            e.printStackTrace();
            response.setCode(ResponseEnums.FILE_UPLOAD_ERROR.getCode());
            response.setMsg(ResponseEnums.FILE_UPLOAD_ERROR.getMsg());
        } finally {
            return response;
        }
    }
}
