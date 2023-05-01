package com.example.epos.controller;
import com.example.epos.common.R;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
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
    //the local file which is abandoned
//    @PostMapping("/upload")
//    public R<String> upload(MultipartFile file)
//    {
//
//        log.info(file.toString());
//        String originalFilename = file.getOriginalFilename();
//        String suffix =originalFilename.substring(originalFilename.lastIndexOf("."));
//        //use UUID to regenerate the file to avoid replacement
//        String fileName = UUID.randomUUID().toString()+'_' + suffix;
//        //StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + fileName);
//
//        File dir = new File(basePath);
//
//        if (!dir.exists())
//        {
//            //directory not exist
//            dir.mkdir();
//        }
//        try {
//            //send temporary file to certain place
//            file.transferTo(new File(basePath+fileName));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return R.success(fileName);
//    }
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("C:/Users/zhanyu guo/Desktop/Sendout/EPOS/src/main/java/com/example/epos/service/impl/serviceAccount.json"));
        // Initialize the Google Cloud Storage client with the Firebase project ID
        Storage storage = StorageOptions.newBuilder()
                .setCredentials(credentials)
                .setProjectId("epos1-d361b")
                .build()
                .getService();
        BlobInfo blobInfo = BlobInfo.newBuilder("epos1-d361b.appspot.com", "sellers/" + file.getOriginalFilename())
                .setContentType(file.getContentType())
                .build();
        Blob blob = storage.create(blobInfo, file.getBytes());
        String publicUrl = String.format("https://storage.googleapis.com/%s/%s", blob.getBucket(), blob.getName());

        return R.success(publicUrl);
    }
    // to fetch the local file
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response)
    {

        //Input stream, get the content of the file
//        try {
//            FileInputStream fileInputStream = new FileInputStream(new File(basePath+name));
//            // according to the output stream, return the img
//            ServletOutputStream outputStream = response.getOutputStream();
//            response.setContentType("image/jpeg");
//            int len = 0;
//            byte[] bytes = new byte[1024];
//            while ((len = fileInputStream.read(bytes))!= -1)
//            {
//                outputStream.write(bytes,0,len);
//                outputStream.flush();
//            }
//            //shut the resource
//            outputStream.close();
//            fileInputStream.close();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

    }
}
