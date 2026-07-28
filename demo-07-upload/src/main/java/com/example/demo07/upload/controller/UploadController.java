package com.example.demo07.upload.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 文件上传控制器
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-28 14:38
 */
@RestController
@RequestMapping("/upload")
public class UploadController {

    // 从 application.yml 中读取自定义的保存路径
    @Value("${upload.path}")
    private String uploadPath;

    @PostMapping("/single")
    public Map<String, Object> uploadSingleFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        
        if (file.isEmpty()) {
            result.put("code", 400);
            result.put("message", "哎呀，请选择一个文件！");
            return result;
        }

        try {
            // 获取前端传过来的原始文件名
            String originalFilename = file.getOriginalFilename();
            // 获取文件后缀名 (比如 png, txt, docx)
            String extension = StringUtils.getFilenameExtension(originalFilename);
            
            // 使用 UUID 生成一个新的随机文件名，防止同名文件互相覆盖
            String newFileName = UUID.randomUUID().toString().replace("-", "") + "." + extension;

            // 检查 D:/upload/ 目录是否存在，不存在则创建
            File folder = new File(uploadPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // 执行真正的保存动作
            Path path = Paths.get(uploadPath + newFileName);
            Files.write(path, file.getBytes());

            result.put("code", 200);
            result.put("message", "恭喜，文件上传成功！");
            result.put("originalName", originalFilename);
            result.put("savedFileName", newFileName);
            result.put("savedPath", path.toString());

        } catch (IOException e) {
            result.put("code", 500);
            result.put("message", "文件上传失败：" + e.getMessage());
        }

        return result;
    }
}
