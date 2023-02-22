package com.kclm.xsap.utils.file;

import com.kclm.xsap.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class UploadImg {


    public static String uploadImg(MultipartFile file, String uploadImagesTeacherImg) {
        ApplicationHome applicationHome = new ApplicationHome(UploadImg.class);
        File dir = applicationHome.getDir();
        File realPath = new File(dir, uploadImagesTeacherImg);
        if (!realPath.exists()) {
            log.info("创建上传文件目录结构");
            realPath.mkdirs();
        }
        log.info(realPath.getPath());
        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + substring;
        log.info(realPath.getPath() + fileName);
        try {
            file.transferTo(new File(realPath, fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

}
