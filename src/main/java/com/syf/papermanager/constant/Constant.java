package com.syf.papermanager.constant;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.constant
 * @description: 记录一些常量
 * @author: songyafeng
 * @create_time: 2020/11/13 17:06
 */
public class Constant {
    /** 用户提交文件的存储路径*/
    public static final String FILE_PATH = System.getProperty("user.dir") +
            System.getProperty("file.separator") + "files" + System.getProperty("file.separator");

    /** 用户提交图片的存储路径*/
    public static final String IMG_PATH = System.getProperty("user.dir") +
            System.getProperty("file.separator") + "imgs" + System.getProperty("file.separator");

}
