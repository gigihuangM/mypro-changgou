package com.changgou.search.controller;

import com.changgou.file.FastFDFSFile;
import com.changgou.util.FastFDFSUtils;
import entity.Result;
import entity.StatusCode;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/upload")
@CrossOrigin
public class FileUploadController {

    @PostMapping
    public Result upload(@RequestParam("file")MultipartFile file)throws  Exception{
        //调用工具类，将文件传入到FastDfs中
        FastFDFSFile fastFDFSFile=new FastFDFSFile(
                file.getOriginalFilename(),
                file.getBytes(),
                StringUtils.getFilenameExtension(file.getOriginalFilename())
        );
        String[] upload = FastFDFSUtils.upload(fastFDFSFile);
        //拼接一个访问地址
        String url= FastFDFSUtils.getTrackerInfo()+"/"+upload[0]+"/"+upload[1];
        return new Result(true, StatusCode.OK,"文件上传成功",url);
    }

}
