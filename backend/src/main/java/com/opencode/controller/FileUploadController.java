package com.opencode.controller;

import com.opencode.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class FileUploadController {
    
    @Value("${upload.path:uploads/}")
    private String uploadPath;
    
    @PostMapping("/image")
    public Result<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        return uploadFile(file, "images", new String[]{"jpg", "jpeg", "png", "gif", "webp"});
    }
    
    @PostMapping("/document")
    public Result<Map<String, String>> uploadDocument(@RequestParam("file") MultipartFile file) {
        return uploadFile(file, "documents", new String[]{"pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "md"});
    }
    
    @PostMapping("/avatar")
    public Result<Map<String, String>> uploadAvatar(@RequestParam("file") MultipartFile file) {
        return uploadFile(file, "avatars", new String[]{"jpg", "jpeg", "png", "gif"});
    }
    
    private Result<Map<String, String>> uploadFile(MultipartFile file, String folder, String[] allowedExtensions) {
        if (file.isEmpty()) {
            return Result.error("请选择要上传的文件");
        }
        
        // 获取文件扩展名
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        }
        
        // 检查文件类型
        boolean allowed = false;
        for (String ext : allowedExtensions) {
            if (ext.equals(extension)) {
                allowed = true;
                break;
            }
        }
        
        if (!allowed) {
            return Result.error("不支持的文件类型");
        }
        
        try {
            // 创建上传目录
            Path uploadDir = Paths.get(uploadPath, folder);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            
            // 生成唯一文件名
            String filename = UUID.randomUUID().toString() + "." + extension;
            Path filePath = uploadDir.resolve(filename);
            
            // 保存文件
            Files.copy(file.getInputStream(), filePath);
            
            // 返回文件信息
            Map<String, String> data = new HashMap<>();
            data.put("filename", filename);
            data.put("originalName", originalFilename);
            data.put("url", "/uploads/" + folder + "/" + filename);
            data.put("size", String.valueOf(file.getSize()));
            
            return Result.success(data);
        } catch (IOException e) {
            return Result.error("文件上传失败: " + e.getMessage());
        }
    }
}
