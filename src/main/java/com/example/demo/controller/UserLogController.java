package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.entity.UserLog;
import com.example.demo.mapper.UserLogMapper;
import com.example.demo.vo.BaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.util.List;

@Controller
public class UserLogController {

    @Autowired
    private UserLogMapper userLogMapper;

    private final String localPath = "log.csv";

    @GetMapping("/ImportCsv")
    public String uploadPage() {
        return "ImportCsv";
    }

    @PostMapping("/ImportCsv")
    @ResponseBody
    public BaseVo ImportCsv(@RequestPart("file") MultipartFile file) {
        BaseVo result = new BaseVo();

        String fileName = file.getOriginalFilename();
        String filePath = fileName;

        File dest = new File(filePath);
        try {
            Files.copy(file.getInputStream(), dest.toPath());
        }
        catch (IOException e) {
            result.setCode(1);
            result.setMessage("上传文件失败");
        }

        BufferedReader bufferedReader = null;
        File csv = new File(filePath);
        String line = "";
        final String spliter = ",";

        try{
            bufferedReader = new BufferedReader(new FileReader(csv));

            while ((line = bufferedReader.readLine()) != null){
                String[] blocks = line.split(spliter);

                UserLog userLog = new UserLog(
                        blocks[0],
                        blocks[1],
                        blocks[2],
                        blocks[3],
                        blocks[4],
                        blocks[5],
                        blocks[6],
                        blocks[7],
                        blocks[8],
                        blocks[9],
                        blocks[10]
                );

                userLogMapper.insert(userLog);

                result.setCode(0);
                result.setMessage("插入信息成功");

            }

            bufferedReader.close();
        }catch (FileNotFoundException e){
            result.setCode(1);
            result.setMessage("找不到文件");
        }catch (IOException e){
            result.setCode(1);
            result.setMessage("文件读写出错");
        }
        finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                }
                catch (IOException e) {
                    result.setCode(1);
                    result.setMessage("文件关闭出错");
                }
            }
            csv.delete();
        }

        return result;
    }

    @RequestMapping("/ExportCsv")
    @ResponseBody
    public BaseVo ExportCsv(HttpServletRequest request, HttpServletResponse response){
        BaseVo result = new BaseVo();

        File writeFile = new File("log.csv");

        QueryWrapper<UserLog> userLogQueryWrapper = Wrappers.query();
        List<UserLog> queryLogs = userLogMapper.selectList(userLogQueryWrapper);

        try{
            BufferedWriter writeText = new BufferedWriter(new FileWriter(writeFile));

            for(int i = 0; i < queryLogs.size(); i++){
                writeText.write(queryLogs.get(i).getUserId() + "," +
                        queryLogs.get(i).getItemId() + "," +
                        queryLogs.get(i).getCatId() + "," +
                        queryLogs.get(i).getMerchantId() + "," +
                        queryLogs.get(i).getBrandId() + "," +
                        queryLogs.get(i).getMonth() + "," +
                        queryLogs.get(i).getDay() + "," +
                        queryLogs.get(i).getAction() + "," +
                        queryLogs.get(i).getAgeRange() + "," +
                        queryLogs.get(i).getGender() + "," +
                        queryLogs.get(i).getProvince());
                writeText.newLine();
            }

            writeText.flush();
            writeText.close();
        }catch (FileNotFoundException e){
            result.setCode(1);
            result.setMessage("没有找到指定文件");
        }catch (IOException e){
            result.setCode(1);
            result.setMessage("文件读写出错");
        }

        String fileName = "log.csv";
        //设置文件路径
        File file = new File(localPath);
        if (file.exists()) {
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
            byte[] buffer = new byte[1024];
            FileInputStream fileInputStream = null;
            BufferedInputStream bufferedInputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
                bufferedInputStream = new BufferedInputStream(fileInputStream);
                OutputStream outputStream = response.getOutputStream();
                int i = bufferedInputStream.read(buffer);
                while (i != -1) {
                    outputStream.write(buffer, 0, i);
                    i = bufferedInputStream.read(buffer);
                }
                result.setCode(0);
                result.setMessage("下载成功");
            } catch (Exception e) {
                result.setCode(1);
                result.setMessage("下载失败");
            } finally {
                if (bufferedInputStream != null) {
                    try {
                        bufferedInputStream.close();
                    }
                    catch (IOException e) {
                        result.setCode(0);
                        result.setMessage("Buffer流未正确关闭");
                    }
                }
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    }
                    catch (IOException e) {
                        result.setCode(0);
                        result.setMessage("文件流未正确关闭");
                    }
                }
                file.delete();
            }
        }

        return result;

    }

}
