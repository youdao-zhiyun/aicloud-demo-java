package com.youdao.aicloud.translate.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public class JSONUtil {

    public static String mapToJSONString(Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            jsonObject.put(entry.getKey(), entry.getValue());
        }
        return jsonObject.toJSONString();
    }
}
