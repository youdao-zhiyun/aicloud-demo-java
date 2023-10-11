package com.youdao.aicloud.translate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.youdao.aicloud.translate.utils.AuthV4Util;
import com.youdao.aicloud.translate.utils.HttpUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * 网易有道智云长语音转写服务api调用demo
 * 请求地址： http://openapi.youdao.com
 */
public class LongFormAsrDemo {

    private static final String APP_KEY = "";     // 您的应用ID
    private static final String APP_SECRET = "";  // 您的应用密钥

    // 待转写的音频路径, 例windows路径：PATH = "C:\\youdao\\media.wav";
    private static final String PATH = "";

    // 单个分片大小
    private static final int SLICE_SIZE = 10485760; // 10M

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        File file = new File(PATH);

        // 1、 预处理
        String taskId = prepareHelper(file);
        if(null == taskId) {
            System.out.println("预处理失败");
            System.exit(1);
        }
        System.out.println("taskId: " + taskId);

        // 2、 对文件进行分片并上传
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] slice = new byte[SLICE_SIZE];
            int len = 0;
            int sliceId = 1;
            while((len = fileInputStream.read(slice)) > 0) {
                FileOutputStream fos = new FileOutputStream("D:\\" + sliceId + ".wav");
                fos.write(slice, 0, len);
                fos.close();
                String uploadErrorCode = uploadHelper(taskId, sliceId, new File("D:\\" + sliceId + ".wav"));
                if(!"0".equals(uploadErrorCode)) {
                    System.exit(1);
                }
                System.out.println("分片" + sliceId + "上传成功");
                sliceId++;
            }
        }

        // 3、 合并文件
        String mergeErrorCode = mergeHelper(taskId);
        if (!"0".equals(mergeErrorCode)) {
            System.exit(1);
        }

        // 4、 查看转写进度
        while (true) {
            try {
                System.out.println("sleep a while ... ");
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String result = getProcessHelper(taskId);
            JSONObject jsonObject = JSONObject.parseObject(result);
            String getProgressErrorCode = jsonObject.getString("errorCode");
            if ("0".equals(getProgressErrorCode)) {
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                Integer taskStatus = jsonArray.getJSONObject(0).getInteger("status");
                if (taskStatus == 9) {
                    System.out.println("任务完成！");
                    break;
                }
                System.out.println("任务处理进度：" + taskId);
            } else {
                System.out.println("获取任务处理进度失败！");
            }
        }

        // 5、 获取结果
        String result = getResultHelper(taskId);
        System.out.println("转写结果：" + result);
    }

    private static String getResultHelper(String taskId) throws NoSuchAlgorithmException {
        Map<String, String[]> params = new HashMap<String, String[]>() {{
            put("q", new String[]{taskId});
        }};
        // 添加鉴权相关参数
        AuthV4Util.addAuthParams(APP_KEY, APP_SECRET, params);
        // 请求 api 服务
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/api/audio/get_result", null, params, "application/json");
        if (result != null) {
            String resultStr = new String(result, StandardCharsets.UTF_8);
            JSONObject jsonObject = JSONObject.parseObject(resultStr);
            String errorCode = jsonObject.getString("errorCode");
            if (!"0".equals(errorCode)) {
                System.out.println("获取结果失败：" + resultStr);
            }
            return resultStr;
        }
        return null;
    }

    private static String getProcessHelper(String taskId) throws NoSuchAlgorithmException {
        Map<String, String[]> params = new HashMap<String, String[]>() {{
            put("q", new String[]{taskId});
        }};

        // 添加鉴权相关参数
        AuthV4Util.addAuthParams(APP_KEY, APP_SECRET, params);
        // 请求 api 服务
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/api/audio/get_progress", null, params, "application/json");
        if (result != null) {
            return new String(result, StandardCharsets.UTF_8);
        }
        return null;
    }

    private static String mergeHelper(String taskId) throws NoSuchAlgorithmException {
        Map<String, String[]> params = new HashMap<String, String[]>() {{
            put("q", new String[]{taskId});
        }};

        // 添加鉴权相关参数
        AuthV4Util.addAuthParams(APP_KEY, APP_SECRET, params);
        // 请求 api 服务
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/api/audio/merge", null, params, "application/json");
        // 打印返回结果
        if (result != null) {
            JSONObject jsonObject = JSONObject.parseObject(new String(result, StandardCharsets.UTF_8));
            String errorCode = jsonObject.getString("errorCode");
            if (!"0".equals(errorCode)) {
                System.out.println("合并文件失败: " + taskId + " 错误码：" + errorCode);
            }
            return errorCode;
        }
        return "-1";
    }

    /**
     * 分片上传接口
     *
     * @param taskId
     * @param sliceId
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static String uploadHelper(String taskId, int sliceId, File file) throws NoSuchAlgorithmException {
        Map<String, String[]> params = new HashMap<String, String[]>() {{
            put("q", new String[]{taskId});
            put("sliceId", new String[]{Integer.toString(sliceId)});
            put("type", new String[]{"1"});
        }};

        // 添加鉴权相关参数
        AuthV4Util.addAuthParams(APP_KEY, APP_SECRET, params);
        // 请求api服务
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/api/audio/upload", null, params, "file", file, "application/json");
        // 打印返回结果
        if (result != null) {
            JSONObject jsonObject = JSONObject.parseObject(new String(result, StandardCharsets.UTF_8));
            String errorCode = jsonObject.getString("errorCode");
            String msg = jsonObject.getString("msg");
            if (!"0".equals(errorCode)) {
                System.out.println("上传分片失败，errorCode: " + errorCode + ",msg: " + msg);
            }
            return errorCode;
        }
        return "-1";
    }

    /**
     * 预处理接口：/api/audio/prepare
     *
     * @param file
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static String prepareHelper(File file) throws NoSuchAlgorithmException {
        Map<String, String[]> params = createPrepareRequestParams(file);
        // 添加鉴权相关参数
        AuthV4Util.addAuthParams(APP_KEY, APP_SECRET, params);
        // 请求api服务
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/api/audio/prepare", null, params, "application/json");
        // 返回结果
        if (result != null) {
            JSONObject jsonObject = JSONObject.parseObject(new String(result, StandardCharsets.UTF_8));
            return jsonObject.getString("result");
        }
        return null;
    }

    private static Map<String, String[]> createPrepareRequestParams(File file) {
        // 要识别的语种
        String langType = "en";

        String name = file.getName();
        // 通过文件名后缀获取文件格式
        String format = name.substring(name.indexOf(".") + 1);
        long fileSize = file.length();
        int sliceNum = (int) Math.ceil(fileSize * 1.0 / SLICE_SIZE);

        return new HashMap<String, String[]>() {{
            put("name", new String[]{name});
            put("format", new String[]{format});
            put("langType", new String[]{langType});
            put("sliceNum", new String[]{String.valueOf(sliceNum)});
            put("fileSize", new String[]{String.valueOf(fileSize)});
            put("type", new String[]{"1"});
        }};
    }
}
