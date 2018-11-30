package com.example.demo.controller;

import com.example.demo.utils.MessageDigestUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/***
 *
 * 上传文件
 *
 * <p>@author: Ives.l
 * <p>@date: 2018/11/28
 * <p>@time: 13:22
 *
 */
@Controller
public class UploadController {

    @Value("${upload.location.path}")
    private String uploadFolder;

    private String fileName;

    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam(value = "file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "文件为空, 请选择非空文件上传~");
            return "redirect:/uploadStatus";
        }

        try {
            byte[] bytes = file.getBytes();
            fileName = this.generateFileName(file.getOriginalFilename(), file.getInputStream());
            File dest = new File(uploadFolder + fileName);
            if (!dest.getParentFile().exists()) {
                boolean result = dest.getParentFile().mkdirs();
                System.out.println("创建文件夹成功? " + result);
            }

            if (dest.exists()) {
                System.out.println("文件已经存在");
                redirectAttributes.addFlashAttribute("message",
                        "文件已经存在 '" + fileName);
            } else {
                file.transferTo(dest);
                redirectAttributes.addFlashAttribute("message",
                        "您已成功上传 '" + fileName + "', 该文件大小约为: " + bytes.length / 1024 + " kb.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/uploadStatus";
    }

    @PostMapping("/uploads")
    public String filesUpload(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        MultipartFile file;
        BufferedOutputStream stream = null;
        for (int i = 0; i < files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    fileName = this.generateFileName(file.getOriginalFilename(), file.getInputStream());
                    File dest = new File(uploadFolder + fileName);

                    boolean result = true;

                    if (!dest.getParentFile().exists()) {
                        result = dest.getParentFile().mkdirs();
                    }

                    if (!result || dest.exists()) {
                        System.out.println("文件已经存在");
                        continue;
                    }

                    byte[] bytes = file.getBytes();
                    stream = new BufferedOutputStream(new FileOutputStream(dest));
                    stream.write(bytes);
                } catch (Exception e) {
                    return "You failed to upload " + i + " => " + e.getMessage();
                } finally {
                    try {
                        if (null != stream) {
                            stream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                System.out.println("You failed to upload " + i
                        + " because the file was empty.");
            }
        }
        return "redirect:/review";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "/uploadStatus";
    }

    @GetMapping("/review")
    public String review(Map<String, Object> map) {

        map.put("fileName", fileName);
        String fileType = fileName.split("\\.")[1];
        map.put("fileType", fileType);
        System.out.println(fileName);

        if (fileType.equals("csv")) {
            ReadCSV readCsv = new ReadCSV(uploadFolder + fileName);
            List<String> result = readCsv.read();
            map.put("result", result);
        }

        return "/review";
    }

    private String generateFileName(String originalFileName, InputStream inputStream) throws IOException {
        String suffixName = getFileType(originalFileName);
        String fileMd5 = MessageDigestUtil.fileToMd5(inputStream);

        System.out.println("文件MD5签名: " + fileMd5);

        return (null == fileMd5 ? UUID.randomUUID().toString() : fileMd5.toLowerCase()) + suffixName;
    }

    /**
     * 判断文件是否为图片文件
     */
    private Boolean isImageFile(String fileName) {
        String[] imgType = new String[]{".jpg", ".jpeg", ".png", ".gif", ".bmp"};
        if (fileName == null) {
            return false;
        }
        fileName = fileName.toLowerCase();
        for (String type : imgType) {
            if (fileName.endsWith(type)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件后缀名
     */
    private String getFileType(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }
}
