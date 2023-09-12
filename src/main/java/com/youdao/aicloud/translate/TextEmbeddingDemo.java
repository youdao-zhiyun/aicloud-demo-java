package com.youdao.aicloud.translate;

import com.youdao.aicloud.translate.utils.AuthV3Util;
import com.youdao.aicloud.translate.utils.HttpUtil;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * 网易有道智云文本embedding服务api调用demo
 * api接口:
 * 1、https://openapi.youdao.com/textEmbedding/queryTextEmbeddings
 * 2、https://openapi.youdao.com/textEmbedding/queryTextEmbeddingVersion
 */
public class TextEmbeddingDemo {

    private static final String APP_KEY = "";     // 您的应用ID
    private static final String APP_SECRET = "";  // 您的应用密钥

    public static void main(String[] args) throws NoSuchAlgorithmException {
        // 1、请求version服务
        // 添加请求参数
        Map<String, String[]> params1 = createRequestParams1();
        // 添加鉴权相关参数
        AuthV3Util.addAuthParams(APP_KEY, APP_SECRET, params1);
        byte[] version = HttpUtil.doGet("https://openapi.youdao.com/textEmbedding/queryTextEmbeddingVersion", null, params1, "application/json");
        // 打印返回结果
        if (version != null) {
            System.out.println("version: " + new String(version, StandardCharsets.UTF_8));
        }

        // 2、请求embedding服务
        // 添加请求参数
        Map<String, String[]> params2 = createRequestParams2();
        // 添加鉴权相关参数
        AuthV3Util.addAuthParams(APP_KEY, APP_SECRET, params2);
        byte[] embedding = HttpUtil.doPost("https://openapi.youdao.com/textEmbedding/queryTextEmbeddings", null, params2, "application/json");
        // 打印返回结果
        if (embedding != null) {
            System.out.println("embedding: " + new String(embedding, StandardCharsets.UTF_8));
        }
        System.exit(1);
    }

    private static Map<String, String[]> createRequestParams1() {
        /*
         * note: 将下列变量替换为需要请求的参数
         * 注: q参数本身非必传, 但是计算签名时需要用作空字符串处理""
         */
        String q = "";

        return new HashMap<String, String[]>() {{
            put("q", new String[]{q});
        }};
    }

    private static Map<String, String[]> createRequestParams2() {
        /*
         * note: 将下列变量替换为需要请求的参数
         * 注: 包含16个q时效果最佳, 每个q不要超过500个token
         */
        String q1 = "待输入文本1";
        String q2 = "待输入文本2";
        String q3 = "待输入文本3";
        // q4...

        return new HashMap<String, String[]>() {{
            put("q", new String[]{q1, q2, q3});
        }};
    }
}
