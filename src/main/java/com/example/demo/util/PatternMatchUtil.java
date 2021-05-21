package com.example.demo.util;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PatternMatchUtil {

    /**
     * 检查字符串是否符合邮箱格式
     * @param content
     * @return 匹配结果
     */
    public static boolean isMatchingMail(String content){
        boolean result = false;
        Pattern regex = Pattern.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
        Matcher matcher = regex.matcher(content);
        result = matcher.matches();
        return result;
    }

    /**
     * 检查字符串是否符合手机号格式
     * @param content
     * @return 匹配结果
     */
    public static boolean isMatchingTel(String content){
        boolean result = false;
        Pattern regex = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(17[0-9])|(18[0,5-9]))\\d{8}$");
        Matcher m = regex .matcher(content);
        result = m.matches();
        return result;

    }

}
