package com.example.demo.controller;

import com.example.demo.vo.BaseVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;

@Controller
public class UserLogController {

    @PostMapping("/ImportCsv")
    @ResponseBody
    public BaseVo ImportCsv(@RequestPart("path") String path) {
        BaseVo result = new BaseVo();

        File csv = new File(path);
        String line = "";
        final String spliter = ",";

        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(csv));

            while ((line = bufferedReader.readLine()) != null){
                System.out.println(line);
            }

            bufferedReader.close();
        }catch (FileNotFoundException e){
            System.out.println("没有找到指定文件");
        }catch (IOException e){
            System.out.println("文件读写出错");
        }

        return result;
    }

}
