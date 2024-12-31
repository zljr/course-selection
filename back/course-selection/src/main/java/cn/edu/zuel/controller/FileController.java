package cn.edu.zuel.controller;

import cn.edu.zuel.annotation.AuthAccess;
import cn.edu.zuel.common.Result;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {
    @Value("${ip}")
    private String ip;
    @Value("${server.port}")
    private String port;

    /*
     * 要存放文件的路径*/
    private static final String ROOT_PATH=System.getProperty("user.dir")+File.separator+"files";
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();//获取文件原始名称
        if(!FileUtil.exist(ROOT_PATH)){
            FileUtil.mkdir(ROOT_PATH);//如果不存在该路径，就创建
        }
        if(FileUtil.exist(ROOT_PATH+File.separator+originalFilename)){
            originalFilename=System.currentTimeMillis()+"_"+originalFilename;//如果有重复的文件名称，就修改当前文件名称，防止相同文件名不同文件内容覆盖
        }
        File saveFile=new File(ROOT_PATH+File.separator+originalFilename);//File.separator文件分隔符:"\"
        file.transferTo(saveFile);
        String url="http://"+ip+":"+port+"/file/download/"+originalFilename;
        return Result.success(url);//返回文件的链接，该链接为文件下载地址，这个地址为后端提供
    }
    @AuthAccess
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        String filePath=ROOT_PATH+File.separator+fileName;
        if(!FileUtil.exist(filePath)){
            return;
        }
        byte[] bytes = FileUtil.readBytes(filePath);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }
    /*
     *
     * 编辑器中上传图片的接口*/
    @PostMapping("/editor/upload")
    public Dict editorUpload(@RequestParam MultipartFile file,@RequestParam String type) throws IOException {
        String originalFilename = file.getOriginalFilename();//获取文件原始名称
        if(!FileUtil.exist(ROOT_PATH)){
            FileUtil.mkdir(ROOT_PATH);//如果不存在该路径，就创建
        }
        if(FileUtil.exist(ROOT_PATH+File.separator+originalFilename)){
            originalFilename=System.currentTimeMillis()+"_"+originalFilename;//如果有重复的文件名称，就修改当前文件名称，防止相同文件名不同文件内容覆盖
        }
        File saveFile=new File(ROOT_PATH+File.separator+originalFilename);//File.separator文件分隔符:"\"
        file.transferTo(saveFile);
        String url="http://"+ip+":"+port+"/file/download/"+originalFilename;

        if("img".equals(type)){
            return Dict.create().set("errno", 0).set("data", CollUtil.newArrayList(Dict.create().set("url",url)));
        }else if("video".equals(type)){
            return Dict.create().set("errno",0).set("data",Dict.create().set("url",url));
        }
        return Dict.create().set("errno", 0);
    }
}
