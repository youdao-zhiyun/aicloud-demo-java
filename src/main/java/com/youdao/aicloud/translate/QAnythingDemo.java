package com.youdao.aicloud.translate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.youdao.aicloud.translate.utils.AuthV3Util;
import com.youdao.aicloud.translate.utils.HttpUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 网易有道智云QAnything服务api调用demo
 * 请求地址： https://openapi.youdao.com/q_anything/paas
 */
public class QAnythingDemo {

    private static final String APP_KEY = "";     // 您的应用ID

    private static final String APP_SECRET = "";  // 您的应用密钥

    // 文档路径, 例windows路径：PATH = "C:\\youdao\\doc.pdf";
    private static final String PATH = "";

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        /*
         * note: 将下列变量替换为需要请求的参数
         */
        String kbId = "知识库id";
        String kbName = "知识库名称";
        String url = "资源url地址";
        String fileId = "知识库中文档id";

        // NOTE: 1、知识库管理
        // 1.1、创建知识库
        createKB(kbName);
        // 1.2、删除知识库
        deleteKB(kbId);
        // 1.3、上传文档
        File doc = new File(PATH);
        uploadDoc(kbId, doc);
        // 1.4、上传url
        uploadUrl(kbId, url);
        // 1.5、删除文档
        deleteFile(kbId, fileId);
        // 1.6、查询知识库列表
        kbList();
        // 1.7、查询文档列表
        fileList(kbId);

        /*
         * note: 将下列变量替换为需要请求的参数
         */
        String q = "提问问题";

        // NOTE: 2、文档问答接口
        // 2.1、文档问答
        chat(kbId, q);
        // 2.2、文档问答(流试)
        chatStream(kbId, q);
    }


    /**
     * 创建知识库接口：/create_kb
     */
    private static void createKB(String kbName) throws NoSuchAlgorithmException {
        Map<String, String[]> params = new HashMap<String, String[]>() {{
            put("q", new String[]{kbName});
        }};
        // 添加鉴权相关参数
        AuthV3Util.addAuthParams(APP_KEY, APP_SECRET, params);
        String paramsStr = parseToJson(params);
        // 请求 api 服务
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/q_anything/paas/create_kb", null, paramsStr, "application/json");
        if (result != null) {
            String resultStr = new String(result, StandardCharsets.UTF_8);
            System.out.println("创建知识库接口: " + resultStr);
        }
    }

    /**
     * 删除知识库接口：/delete_kb
     */
    private static void deleteKB(String kbId) throws NoSuchAlgorithmException {
        Map<String, String[]> params = new HashMap<String, String[]>() {{
            put("q", new String[]{kbId});
        }};
        // 添加鉴权相关参数
        AuthV3Util.addAuthParams(APP_KEY, APP_SECRET, params);
        String paramsStr = parseToJson(params);
        // 请求 api 服务
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/q_anything/paas/delete_kb", null, paramsStr, "application/json");
        if (result != null) {
            String resultStr = new String(result, StandardCharsets.UTF_8);
            System.out.println("删除知识库接口: " + resultStr);
        }
    }

    /**
     * 上传文档接口：/upload_file
     */
    private static void uploadDoc(String kbId, File file) throws NoSuchAlgorithmException {
        Map<String, String[]> params = new HashMap<String, String[]>() {{
            put("q", new String[]{kbId});
        }};
        // 添加鉴权相关参数
        AuthV3Util.addAuthParams(APP_KEY, APP_SECRET, params);
        // 请求 api 服务
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/q_anything/paas/upload_file", null, params, "file", file, "application/json");
        if (result != null) {
            String resultStr = new String(result, StandardCharsets.UTF_8);
            System.out.println("上传文档接口: " + resultStr);
        }
    }

    /**
     * 上传URL资源接口：/upload_url
     */
    private static void uploadUrl(String kbId, String url) throws NoSuchAlgorithmException {
        Map<String, String[]> params = new HashMap<String, String[]>() {{
            put("q", new String[]{kbId});
            put("url", new String[]{url});
        }};
        // 添加鉴权相关参数
        AuthV3Util.addAuthParams(APP_KEY, APP_SECRET, params);
        String paramsStr = parseToJson(params);
        // 请求 api 服务
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/q_anything/paas/upload_url", null, paramsStr, "application/json");
        if (result != null) {
            String resultStr = new String(result, StandardCharsets.UTF_8);
            System.out.println("上传url资源接口: " + resultStr);
        }
    }

    /**
     * 删除文档接口：/delete_file
     */
    private static void deleteFile(String kbId, String fileId) throws NoSuchAlgorithmException {
        Map<String, String[]> params = new HashMap<String, String[]>() {{
            put("q", new String[]{kbId});
            put("fileIds", new String[]{fileId});
        }};
        // 添加鉴权相关参数
        AuthV3Util.addAuthParams(APP_KEY, APP_SECRET, params);
        String paramsStr = parseToJson(params);
        // 请求 api 服务
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/q_anything/paas/delete_file", null, paramsStr, "application/json");
        if (result != null) {
            String resultStr = new String(result, StandardCharsets.UTF_8);
            System.out.println("删除文档接口: " + resultStr);
        }
    }

    /**
     * 查询知识库列表接口：/kb_list
     */
    private static void kbList() throws NoSuchAlgorithmException {
        Map<String, String[]> params = new HashMap<String, String[]>() {{
            put("q", new String[]{""});
        }};
        // 添加鉴权相关参数
        AuthV3Util.addAuthParams(APP_KEY, APP_SECRET, params);
        String paramsStr = parseToJson(params);
        // 请求 api 服务
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/q_anything/paas/kb_list", null, paramsStr, "application/json");
        if (result != null) {
            String resultStr = new String(result, StandardCharsets.UTF_8);
            System.out.println("查询知识库列表接口: " + resultStr);
        }
    }

    /**
     * 查询知识库文档接口：/file_list
     */
    private static void fileList(String kbId) throws NoSuchAlgorithmException {
        Map<String, String[]> params = new HashMap<String, String[]>() {{
            put("q", new String[]{kbId});
        }};
        // 添加鉴权相关参数
        AuthV3Util.addAuthParams(APP_KEY, APP_SECRET, params);
        String paramsStr = parseToJson(params);
        // 请求 api 服务
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/q_anything/paas/file_list", null, paramsStr, "application/json");
        if (result != null) {
            String resultStr = new String(result, StandardCharsets.UTF_8);
            System.out.println("查询知识库文档列表接口: " + resultStr);
        }
    }

    /**
     * 问答接口：/chat
     */
    private static void chat(String kbId, String q) throws NoSuchAlgorithmException {
        Map<String, String[]> params = new HashMap<String, String[]>() {{
            put("q", new String[]{q});
            put("kbIds", new String[]{kbId});
        }};
        // 添加鉴权相关参数
        AuthV3Util.addAuthParams(APP_KEY, APP_SECRET, params);
        String paramsStr = parseToJson(params);
        // 请求 api 服务
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/q_anything/paas/chat", null, paramsStr, "application/json");
        if (result != null) {
            String resultStr = new String(result, StandardCharsets.UTF_8);
            System.out.println("问答: " + resultStr);
        }
    }

    /**
     * 问答接口（流试）：/chat_stream
     */
    private static void chatStream(String kbId, String q) throws NoSuchAlgorithmException {
        Map<String, String[]> params = new HashMap<String, String[]>() {{
            put("q", new String[]{q});
            put("kbIds", new String[]{kbId});
        }};
        // 添加鉴权相关参数
        AuthV3Util.addAuthParams(APP_KEY, APP_SECRET, params);
        String paramsStr = parseToJson(params);
        // 请求 api 服务
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/q_anything/paas/chat_stream", null, paramsStr, "application/json");
        if (result != null) {
            String resultStr = new String(result, StandardCharsets.UTF_8);
            System.out.println("问答（流试）: " + resultStr);
        }
    }

    private static String parseToJson(Map<String, String[]> map) {
        JSONObject json = new JSONObject();
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            if (entry.getValue().length > 1 || entry.getKey().equals("kbIds")) {
                json.put(entry.getKey(), new JSONArray(Arrays.asList(entry.getValue())));
            } else {
                json.put(entry.getKey(), entry.getValue()[0]);
            }
        }
        String paramsStr = json.toJSONString();
        System.out.println("请求参数：" + paramsStr);
        return paramsStr;
    }
}
