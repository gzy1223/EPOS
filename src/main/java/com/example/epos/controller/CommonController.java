package com.example.epos.controller;

import com.example.epos.common.R;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    private String basePath= "C:/Users/zhanyu guo/Desktop/Sendout/img";
    /**
      * @Author: GZY
      * @Description: upload the file
      * @Date: 27/01/2023
      * @Param file:
      * @return: com.example.epos.common.R<java.lang.String>
      **/

    //file is the temporary file and needs to be send to certain position otherwise it will be deleted
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file)
    {

        log.info(file.toString());
        String originalFilename = file.getOriginalFilename();
        String suffix =originalFilename.substring(originalFilename.lastIndexOf("."));
        //use UUID to regenerate the file to avoid replacement
        String fileName = UUID.randomUUID().toString() + suffix;

        File dir = new File(basePath);

        if (!dir.exists())
        {
            //directory not exist
            dir.mkdir();
        }
        try {
            //send temporary file to certain place
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName);
    }
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response)
    {
        //Input stream, get the content of the file
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basePath+name));
            // according to the output stream, return the img
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes))!= -1)
            {
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            //shut the resource
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
