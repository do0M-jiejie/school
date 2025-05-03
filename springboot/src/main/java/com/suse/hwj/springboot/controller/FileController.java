package com.suse.hwj.springboot.controller;


import cn.hutool.core.io.FileUtil;
import com.suse.hwj.springboot.common.Result;
import com.suse.hwj.springboot.exception.CustomException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/files")
public class FileController {

    // System.getProperty("user.dir") 获取当前项目的根路径
    // 文件上传的目录的路径
    private static final String filePath = System.getProperty("user.dir") + "/files/";

    @PostMapping("/upload")
    public Result upload(MultipartFile file){ //文件流的形式接收前端发送来的文件
        String originalFilename = file.getOriginalFilename(); //xxx.jpg
        if (!FileUtil.isDirectory(filePath)) {
            FileUtil.mkdir(filePath);  //如果目录不存在 创建一个files目录
        }
        // 给文件名添加一个唯一标识 时间戳
        String filename = System.currentTimeMillis() + "_" + originalFilename;
        String realPath = filePath + filename; // 完整的文件路径
        try {
            FileUtil.writeBytes(file.getBytes(),realPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("500","文件上传失败");
        }
        // 返回一个网络连接
        String url = "http://localhost:8080/files/download/" + filename;
        // https://localhost:8080/files/download/xxx.jpg
        return Result.success(url);
    }

    @GetMapping("/download/{filename}")
    public void download(@PathVariable String filename, HttpServletResponse response){
        try {
           response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8));
           response.setContentType("application/octet-stream");
           OutputStream os = response.getOutputStream();
           String realPath = filePath + filename;
           // 获取到文件的字节数组
           byte[] bytes = FileUtil.readBytes(realPath);
            os.write(bytes);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("500","文件下载失败");
        }
    }
}
