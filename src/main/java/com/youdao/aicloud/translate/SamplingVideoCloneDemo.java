package com.youdao.aicloud.translate;

import com.alibaba.fastjson.JSONObject;
import com.youdao.aicloud.translate.utils.AuthV4Util;
import com.youdao.aicloud.translate.utils.HttpUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 网易有道智云小采样数字人形象定制服务api调用demo
 * 请求地址： https://openapi.youdao.com/sampling_digital_human/paas
 */
public class SamplingVideoCloneDemo {

    private static final String APP_KEY = "";     // 您的应用ID

    private static final String APP_SECRET = "";  // 您的应用密钥

    // 视频路径, 例windows路径：PATH = "C:\\youdao\\media.mp4";
    private static final String PATH = "";

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {

        /*
         * note: 将下列变量替换为需要请求的参数
         */
        String name = "";
        String voiceName = "";

        File file = new File(PATH);

        // v2上传文件
        String taskId = uploadV2Helper(name, voiceName, file);
        System.out.println("文件上传成功");

        // 获取结果
        String result = getResultHelper(taskId);
        System.out.println("转写结果地址：" + result);
    }

    /**
     * 查询任务结果接口：/sample_clone/clone_result
     */
    private static String getResultHelper(String taskId) throws NoSuchAlgorithmException {
        Map<String, String[]> params = new HashMap<String, String[]>() {{
            put("taskId", new String[]{taskId});
        }};
        // 添加鉴权相关参数
        AuthV4Util.addAuthParams(APP_KEY, APP_SECRET, params);
        // 请求 api 服务
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/sampling_digital_human/paas/sample_clone/clone_result", null, params, "application/json");
        if (result != null) {
            String resultStr = new String(result, StandardCharsets.UTF_8);
            JSONObject jsonObject = JSONObject.parseObject(resultStr);
            String errorCode = jsonObject.getString("errorCode");
            if (!"0".equals(errorCode)) {
                System.out.println("获取结果失败：" + resultStr);
                return null;
            }
            return jsonObject.toJSONString();
        }
        return null;
    }

    /**
     * 分片上传接口：/sample_clone/v2/upload
     */
    private static String uploadV2Helper(String name, String voiceName, File file) throws NoSuchAlgorithmException {
        Map<String, String[]> params = new HashMap<String, String[]>() {{
            put("name", new String[]{name});
            put("voiceName", new String[]{voiceName});
        }};

        // 添加鉴权相关参数
        AuthV4Util.addAuthParams(APP_KEY, APP_SECRET, params);
        System.out.println("start: " + new Date());
        // 请求api服务
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/sampling_digital_human/paas/sample_clone/v2/upload", null, params, "file", file, "application/json");
        // 打印返回结果
        if (result != null) {
            JSONObject jsonObject = JSONObject.parseObject(new String(result, StandardCharsets.UTF_8));
            String errorCode = jsonObject.getString("errorCode");
            String msg = jsonObject.getString("msg");
            System.out.println(jsonObject);
            if (!"0".equals(errorCode)) {
                System.out.println("上传失败，errorCode: " + errorCode + ", msg: " + msg);
                System.exit(1);
            }
            return jsonObject.getJSONObject("result").getString("taskId");
        }
        return "-1";
    }
}
