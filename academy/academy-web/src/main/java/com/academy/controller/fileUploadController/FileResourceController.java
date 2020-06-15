package com.academy.controller.fileUploadController;

import com.academy.utils.*;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


/**
 * 文件资源处理类
 *
 * @author Administrator
 */
@Controller
@RequestMapping("/fileResource")
public class FileResourceController {

    @Value("${upload.path}")
    private String parentPath;

    /**
     * 文件上传
     *
     * @param file
     * @param folder
     * @return
     */
    @PostMapping(value = "/upload")
    @ResponseBody
    public Object upload(@RequestParam("file") MultipartFile file, @RequestParam("folder") String folder, HttpServletResponse response) {

        response.addHeader("Access-Control-Allow-Origin", "*");//解决跨域问题的方法之一，但有弊端

        if (file.isEmpty()) {
            return "文件为空的";
        }

        String datePath = "";
        String date = DateUtil.getStartardDate();
        String dateArr[] = date.split("-");
        if (dateArr != null && dateArr.length > 1) {
            List<String> dateList = Arrays.asList(dateArr);
            for (String str : dateList) {
                if (StringUtil.isNotEmpty(datePath)) {
                    datePath = datePath + "/" + str;
                } else {
                    datePath = str;
                }
            }
        }
        String stamp = new Date().getTime() + "";
        String path = ""; // 操作文件的物理路径
        String realPath = folder + File.separator + datePath + "/" + stamp; // 写入数据库的路径
        String lastName = file.getOriginalFilename();
        String houzui = lastName.substring(lastName.lastIndexOf(".")); // 截取后缀 例如 .jpg
        lastName = stamp + lastName.substring(lastName.lastIndexOf("."));
        path = parentPath + File.separator + realPath + File.separator + lastName;
        File dest = new File(new File(path).getAbsolutePath());
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            byte[] bytes = file.getBytes();
            OutputStream out = new FileOutputStream(dest);
            out.write(bytes);
//            file.transferTo(dest);
            // file为tomcat配置文件中约定的目录名称
            String backPath = File.separator + realPath + File.separator + lastName;
            path = path.replace("\\", "/");
            backPath = backPath.replace("\\", "/"); // 如果处于WINDOWS环境的反斜杠，则替换为正斜杠
            Map<String, Object> data = new HashMap<>();
            data.put("result", 200);
            data.put("message", "上传成功");
            data.put("object", backPath);
            data.put("allPath", path);
            data.put("originalFilename", file.getOriginalFilename());
            return data;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            String fileType = "";
            if (StringUtil.isNotEmpty(path)) {
                String pathArr[] = path.split("[.]");
                if (pathArr != null && pathArr.length > 1) {
                    fileType = pathArr[1];
                }
            }

            String type = "BMP|bmp|jpg|JPG|wbmp|jpeg|png|PNG|JPEG|WBMP|GIF|gif"; //支持的类型
            if (type.contains(fileType)) {
                ImageUtil imageUtil = new ImageUtil();
                imageUtil.thumbnailImage(path, 100, 100);
            }
        }
        return ResultUtil.error("上传失败");
    }

    /**
     * 多文件上传
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/batch/upload", method = RequestMethod.POST)
    @ResponseBody
    public Object handleFileUpload(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        MultipartFile file = null;
        BufferedOutputStream stream = null;
        String storagePath = "batchFile" + File.separator + UUID.randomUUID() + "";
        String folderPath = parentPath + storagePath;
        if (!new File(folderPath).mkdirs()) {
            return null;
        }
        List<String> pathList = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    String houZui = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                    String originalFilename = StringUtil.isChinese(file.getOriginalFilename())
                            ? UUID.randomUUID().toString() + houZui
                            : file.getOriginalFilename();
                    stream = new BufferedOutputStream(
                            new FileOutputStream(new File(folderPath + File.separator + originalFilename)));
                    stream.write(bytes);
                    stream.close();
                    pathList.add(storagePath + File.separator + originalFilename);
                } catch (Exception e) {
                    stream = null;
                    String message = "有" + i + "份文件上传失败" + e.getMessage();
                    return ResultUtil.success(null);
                }
            } else {
                String message = i + "个文件上传失败";
                return ResultUtil.error(message);
            }
        }
        return ResultUtil.success(storagePath);
    }


    /**
     * 下载文件
     *
     * @param url
     * @param response
     */
    @RequestMapping("/download")
    public void show(String url, HttpServletResponse response) {
        FileInputStream in;
        Path path = Paths.get(parentPath + url);
        response.setContentType("application/octet-stream;charset=UTF-8");
        try {
            // 图片读取路径
            in = new FileInputStream(parentPath + url);
            byte[] buffer = new byte[8192];
            int i = -1;
            OutputStream stream = response.getOutputStream();
            while ((i = in.read(buffer)) != -1) {
                stream.write(buffer, 0, i);
            }
            in.close();
            String contentType = Files.probeContentType(path);
            response.setContentType(contentType);
            stream.flush();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看图片
     *
     * @param url
     * @param response
     */
    @RequestMapping("/showImg")
    public void showImg(String url, HttpServletResponse response) {
        File file = new File(parentPath + url);
        response.setContentType("image/jpeg; charset=GBK");// 设置相应信息的类型
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] buffer = new byte[10240];
            //System.out.println( String.valueOf(file.length()));
            FileInputStream inputStream = new FileInputStream(parentPath + url);
            response.setContentLength((int) file.length());
            response.setHeader("Content-Range", "" + Integer.valueOf((int) file.length() - 1));
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Etag", "W/\"9767057-1323779115364\"");
            int i = -1;
            while ((i = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, i);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/showMp4")
    public void showMp4(String url, HttpServletRequest request, HttpServletResponse response) {
        File file = new File(parentPath + url);
        try {
            RandomAccessFile randomFile = new RandomAccessFile(file, "r");//只读模式
            long contentLength = randomFile.length();
            String range = request.getHeader("Range");
            int start = 0, end = 0;
            if (range != null && range.startsWith("bytes=")) {
                String[] values = range.split("=")[1].split("-");
                start = Integer.parseInt(values[0]);
                if (values.length > 1) {
                    end = Integer.parseInt(values[1]);
                }
            }
            int requestSize = 0;
            if (end != 0 && end > start) {
                requestSize = end - start + 1;
            } else {
                requestSize = Integer.MAX_VALUE;
            }

            byte[] buffer = new byte[4096];
            response.setContentType("video/mp4");
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("ETag", file.getName());
            response.setHeader("Last-Modified", new Date().toString());
            //第一次请求只返回content length来让客户端请求多次实际数据
            if (range == null) {
                response.setHeader("Content-length", contentLength + "");
            } else {
                //以后的多次以断点续传的方式来返回视频数据
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);//206
                long requestStart = 0, requestEnd = 0;
                String[] ranges = range.split("=");
                if (ranges.length > 1) {
                    String[] rangeDatas = ranges[1].split("-");
                    requestStart = Integer.parseInt(rangeDatas[0]);
                    if (rangeDatas.length > 1) {
                        requestEnd = Integer.parseInt(rangeDatas[1]);
                    }
                }
                long length = 0;
                if (requestEnd > 0) {
                    length = requestEnd - requestStart + 1;
                    response.setHeader("Content-length", "" + length);
                    response.setHeader("Content-Range", "bytes " + requestStart + "-" + requestEnd + "/" + contentLength);
                } else {
                    length = contentLength - requestStart;
                    response.setHeader("Content-length", "" + length);
                    response.setHeader("Content-Range", "bytes " + requestStart + "-" + (contentLength - 1) + "/" + contentLength);
                }
            }
            ServletOutputStream out = response.getOutputStream();
            int needSize = requestSize;
            randomFile.seek(start);
            while (needSize > 0) {
                int len = randomFile.read(buffer);
                if (needSize < buffer.length) {
                    out.write(buffer, 0, needSize);
                } else {
                    out.write(buffer, 0, len);
                    if (len < buffer.length) {
                        break;
                    }
                }
                needSize -= buffer.length;
            }
            randomFile.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 查看图片
     *
     * @param url
     * @param response
     */
    @RequestMapping("/showMp3")
    public void showMp3(String url, HttpServletResponse response) {
        File file = new File(parentPath + url);
        response.setContentType("audio/mp3; charset=GBK");
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] buffer = new byte[10240];
            //System.out.println( String.valueOf(file.length()));
            FileInputStream inputStream = new FileInputStream(parentPath + url);
            response.setContentLength((int) file.length());
            response.setHeader("Content-Range", "" + Integer.valueOf((int) file.length() - 1));
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Etag", "W/\"9767057-1323779115364\"");
            int i = -1;
            while ((i = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, i);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除资源及其父文件夹
     */
    @RequestMapping("/delFile")
    @ResponseBody
    public Object delFile(String url) {
        String path = parentPath + url;
        File file = new File(path);
        if (file.exists()) {
            boolean bool = file.delete();
            if (bool) {
                return ResultUtil.success("删除成功");
            }
        }
        return ResultUtil.success("该文件已删除");
    }

    /***
     * 根据文件夹寻找资源
     **/
    @RequestMapping("findFile")
    @ResponseBody
    public Object findFile(String storagePath) {
        File file = new File(parentPath + PathFormat.format(storagePath));
        Set<String> obj = new HashSet<>();
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                return ResultUtil.error("寻找资源失败");
            } else {
                for (File file2 : files) {
                    if (file2 != null) {
                        String name = PathFormat.format(storagePath) + File.separator
                                + file2.getName();
                        obj.add(name);
                    }
                }
            }
        }
        return obj.size() > 0 ? ResultUtil.success(obj) : ResultUtil.error();
    }

    /***
     * @param folderArray 一个父文件夹的集合 用JSONArray表示
     *
     ***/
    @RequestMapping("findFileBatch")
    @ResponseBody
    public Object findFileBatch(String folderArray) {
        try {
            JSONArray folders = JSONArray.parseArray(folderArray);
            if (folders.size() == 0) {
                return ResultUtil.error("寻找资源失败");
            }
            String[] foldersArray = new String[folders.size()];
            folders.toArray(foldersArray);
            Map<String, Set<String>> pathMap = new HashMap<>();
            for (String storagePath : foldersArray) {
                File file = new File(parentPath + PathFormat.format(storagePath));
                Set<String> obj = new HashSet<>();
                if (file.exists() && file.listFiles().length > 0) {
                    File[] files = file.listFiles();
                    for (File file2 : files) {
                        if (file2 != null) {
                            String name = PathFormat.format(storagePath) + File.separator + file2.getName();
                            obj.add(name);
                        }
                    }
                }
                pathMap.put(storagePath, obj);
            }
            return ResultUtil.success(pathMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.error("寻找资源失败");
    }

    /***
     * 把JSON字符串持久化写为JSON文件
     *
     * @param jsonFile JSON字符串
     * @param filePath 写入文件的地址
     ***/
    @RequestMapping("writeFile")
    @ResponseBody
    public Object writeFile(String jsonFile, String filePath, String subClose) {
        boolean writeFlag = true;
        String url = PathFormat.format(filePath);
        Long fileSize = null;
        try {
            File fileDir = new File(parentPath + url);
            // 创建文件夹
            if (!fileDir.exists()) {
                if (!fileDir.mkdirs()) {
                    throw new RuntimeException();
                }
            }
            // 创建文件
            File file = new File(parentPath + url);
            if (file.exists()) {
                if (!file.delete()) {
                    throw new RuntimeException();
                }
            }
            if (!file.createNewFile()) {
                throw new RuntimeException();
            }

            if (subClose == null || "".equals(subClose)) {
                jsonFile = jsonFile.substring(1, jsonFile.length() - 1);
            }
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            BufferedWriter bw = new BufferedWriter(out);
            bw.write(jsonFile);
            bw.flush();
            bw.close();
            out.close();
            fileSize = file.length();
        } catch (Exception e) {
            writeFlag = false;
            e.printStackTrace();
        }
        if (writeFlag) {
            return ResultUtil.success(fileSize);
        } else {
            return ResultUtil.error("写入失败");
        }
    }

}
