package com.czk.forum.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * created by srdczk 2019/11/2
 */

public class ForumUtil {
    //生成随机字符串
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    } 
    //MD5加密
    //不能解密, 加盐
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) return null;
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    public static String getJSONString(int code, String msg, Map<String, Object> map) {
        JSONObject json =  new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (map != null) {
            for (String s : map.keySet()) {
                json.put(s, map.get(s));
            }
        }
        return json.toJSONString();
    }

    public static String getJSONString(int code, String msg) {
        JSONObject json =  new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        return json.toJSONString();
    }

    public static String getJSONString(int code) {
        JSONObject json =  new JSONObject();
        json.put("code", code);
        return json.toJSONString();
    }


}
