package com.ntu.treatment.controller;

import org.apache.catalina.connector.Response;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/images")
public class ImageController {
    @RequestMapping("/getImage")
    public ResponseEntity<byte[]> getImage(String userName) {
        Path imagePath = Paths.get("D:/android_work1/android-chat/server/treatment/src/main/resources/static/images/" + userName + "_avatar.jpg");
        if (!imagePath.toFile().exists()) {
            // 处理图片不存在的情况，返回合适的响应
            return ResponseEntity.notFound().build();
        }
        try {
            byte[] imageBytes = Files.readAllBytes(imagePath);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // or MediaType.IMAGE_PNG, depending on your image type
                    .body(imageBytes);
        } catch (IOException e) {
            // 处理读取文件失败的情况，返回合适的响应
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    @GetMapping("/{userName}")
    public ResponseEntity<byte[]> getImageByUrl(@PathVariable String userName)  {
        System.out.println("getImageByUrl");
        // 这里假设图片存放在项目的 resources 目录下的 static/images 文件夹中
        Path imagePath = Paths.get("D:/android_work1/android-chat/server/treatment/src/main/resources/static/images/" + userName + "_avatar.jpg");
        if (!imagePath.toFile().exists()) {
            // 处理图片不存在的情况，返回合适的响应
            return ResponseEntity.notFound().build();
        }
        try {
            byte[] imageBytes = Files.readAllBytes(imagePath);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // or MediaType.IMAGE_PNG, depending on your image type
                    .body(imageBytes);
        } catch (IOException e) {
            // 处理读取文件失败的情况，返回合适的响应
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    @RequestMapping("/uploadAvatar")
    public Boolean changeAvatar(@RequestParam("file") MultipartFile file, String userName) {
        try {
            // 获取上传目录的路径
            String uploadPath = "D:/android_work1/android-chat/server/treatment/src/main/resources/static/images";
            System.out.println(userName);
            // 如果目录不存在，则创建目录
            java.io.File uploadDir = new java.io.File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 构建文件路径
            String fileName = "/"+userName + "_avatar.jpg"; // 可以根据需要修改文件名
            String filePath = uploadPath + fileName;

            // 使用二进制流写入文件
            try (OutputStream os = new FileOutputStream(filePath);
                 InputStream is = file.getInputStream()) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
