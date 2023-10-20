package com.youdao.aicloud.translate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.youdao.aicloud.translate.utils.AuthV3Util;
import com.youdao.aicloud.translate.utils.FileUtil;
import com.youdao.aicloud.translate.utils.HttpUtil;
import com.youdao.aicloud.translate.utils.JSONUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Scanner;

/**
 * 网易有道智云AI口语老师api调用demo
 * 请求地址： http://openapi.youdao.com
 */
public class AiDialogDemo {

    private static final String APP_KEY = "";     // 您的应用ID
    private static final String APP_SECRET = "";  // 您的应用密钥

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        // 1、 请求获取默认场景接口
        System.out.println("request get default topics:");
        String topic = getDefaultTopicHelper();
        if (null == topic || topic.length() <= 0) {
            System.out.println("get default topics result error");
            System.exit(1);
        }
        System.out.println();

        // 2、 请求生成场景接口
        System.out.println("request generate topic:");
        JSONObject generateTopicResult = generateTopicHelper(topic);
        if (null == generateTopicResult) {
            System.out.println("generate topic result error");
            System.exit(1);
        }
        System.out.println();

        // 3.1、 记录场景参数
        String taskId = generateTopicResult.getString("taskId");
        JSONObject scene = generateTopicResult.getJSONObject("scene");
        /*
         * note: 将下列变量替换为需要请求的参数
         * 取值参考文档: https://ai.youdao.com/DOCSIRMA/html/aigc/api/AIkyls/index.html
         */
        String userLevel = "0";
        RequestDTO requestDTO = new RequestDTO(taskId, userLevel, scene, new JSONArray());

        // 3.2、 请求生成对话接口
        String sysWords = null;
        String userWords = null;
        Scanner scanner = new Scanner(System.in);
        System.out.println("request generate dialog:");
        int dialogIndex = 0;
        do {
            sysWords = generateDialogHelper(requestDTO, sysWords, userWords);
            if (null == sysWords) {
                System.out.println("generate dialog result error");
                System.exit(1);
            }
            System.out.println("系统： " + sysWords);
            System.out.println("用户： ");
            userWords = scanner.nextLine();
            ++dialogIndex;
            System.out.println();
        } while (dialogIndex < 3);

        // 4、 请求生成推荐接口
        System.out.println("request generate recommendation: ");
        JSONArray recommendationResult = generateRecommendationHelper(requestDTO);
        if (null == recommendationResult) {
            System.out.println("generate recommendation result error");
        }
        System.out.println("result: " + recommendationResult);
        System.out.println();

        // 5、生成报告接口
        System.out.println("request generate report: ");
        String report = generateReportHelper(requestDTO);
        System.out.println("result: " + report);
        System.exit(0);
    }

    /**
     * 生成报告接口
     *
     * @param requestDTO
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static String generateReportHelper(RequestDTO requestDTO) throws NoSuchAlgorithmException, IOException {
        // 添加鉴权相关的参数
        Map<String, Object> paramsMap = AuthV3Util.addAuthParams(APP_KEY, APP_SECRET, requestDTO.getTaskId());
        // 添加音频参数示例
        JSONObject historyItem = requestDTO.getHistory().getJSONObject(1);
        String voice = FileUtil.loadMediaAsBase64("音频路径");
        historyItem.put("voice", voice);
        // 添加请求参数
        paramsMap.put("taskId", requestDTO.getTaskId());
        paramsMap.put("userLevel", requestDTO.getUserLevel());
        paramsMap.put("scene", requestDTO.getScene());
        paramsMap.put("history", requestDTO.getHistory());
        String params = JSONUtil.mapToJSONString(paramsMap);

        // 请求api服务
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/ai_dialog/generate_report", null, params, "application/json");
        // 打印返回结果
        if (null == result) {
            return null;
        }
        return new String(result, StandardCharsets.UTF_8);
    }

    /**
     * 生成推荐接口
     *
     * @param requestDTO
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static JSONArray generateRecommendationHelper(RequestDTO requestDTO) throws NoSuchAlgorithmException {
        // 添加鉴权相关的参数
        Map<String, Object> paramsMap = AuthV3Util.addAuthParams(APP_KEY, APP_SECRET, requestDTO.getTaskId());

        /*
         * note: 将下列变量替换为需要请求的参数
         * 取值参考文档: https://ai.youdao.com/DOCSIRMA/html/aigc/api/AIkyls/index.html
         */
        String index = "1"; // 请求系统生成推荐表达例句的索引（对应history数组），取值为 0 - history.length, 如果取值 = history.length,则会生成对话历史的下一句话
        String count = "1";

        paramsMap.put("taskId", requestDTO.getTaskId());
        paramsMap.put("userLevel", requestDTO.getUserLevel());
        paramsMap.put("scene", requestDTO.getScene());
        paramsMap.put("history", requestDTO.getHistory());
        paramsMap.put("indexArr", new String[]{index});
        paramsMap.put("count", count);

        String params = JSONUtil.mapToJSONString(paramsMap);
        System.out.println("request params: " + params);
        // 请求api服务
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/ai_dialog/generate_recommendation", null, params, "application/json");

        // 打印返回结果
        if (null == result) {
            return null;
        }
        String res = new String(result, StandardCharsets.UTF_8);
        System.out.println("result : " + res);
        try {
            JSONObject jsonResult = (JSONObject) JSONObject.parse(res);
            if (!"0".equals(jsonResult.getString("code"))) {
                return null;
            }
            JSONObject data = jsonResult.getJSONObject("data");
            JSONArray resultArr = data.getJSONArray("resultArr");
            return resultArr;
        } catch (Exception e) {
            System.out.println("parse generate recommendation result error");
            return null;
        }
    }

    /**
     * 生成对话接口
     *
     * @param requestDTO
     * @param sysWords
     * @param userWords
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static String generateDialogHelper(RequestDTO requestDTO, String sysWords, String userWords) throws NoSuchAlgorithmException {
        // 添加鉴权相关的参数
        Map<String, Object> paramsMap = AuthV3Util.addAuthParams(APP_KEY, APP_SECRET, requestDTO.getTaskId());
        // 添加请求参数
        paramsMap.put("taskId", requestDTO.getTaskId());
        paramsMap.put("userLevel", requestDTO.getUserLevel());
        paramsMap.put("scene", requestDTO.getScene());
        JSONArray history = requestDTO.getHistory();
        // history参数：第一句话由系统生成
        if (sysWords != null || userWords != null) {
            JSONObject sys = new JSONObject();
            sys.put("speaker", "System");
            sys.put("content", sysWords);
            JSONObject user = new JSONObject();
            user.put("speaker", "User");
            user.put("content", userWords);
            requestDTO.addHistoryItem(sys);
            requestDTO.addHistoryItem(user);
        }
        paramsMap.put("history", history);

        String params = JSONUtil.mapToJSONString(paramsMap);
        System.out.println("request params: " + params);
        // 请求api服务
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/ai_dialog/generate_dialog", null, params, "application/json");

        // 打印返回结果
        if (null == result) {
            return null;
        }
        String res = new String(result, StandardCharsets.UTF_8);
        System.out.println("result: " + res);
        try {
            JSONObject jsonObject = (JSONObject) JSONObject.parse(res);
            if (!"0".equals(jsonObject.getString("code"))) {
                return null;
            }
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray resultArr = data.getJSONArray("resultArr");
            JSONArray resultWords = ((JSONObject) resultArr.get(0)).getJSONArray("result");
            return resultWords.getString(0);
        } catch (Exception e) {
            System.out.println("parse generate dialog result error");
            return null;
        }
    }

    /**
     * 生成场景接口
     *
     * @param topic
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static JSONObject generateTopicHelper(String topic) throws NoSuchAlgorithmException {
        // 添加鉴权相关参数
        Map<String, Object> paramsMap = AuthV3Util.addAuthParams(APP_KEY, APP_SECRET, topic);
        // 添加请求参数
        paramsMap.put("topic", topic);
        String params = JSONUtil.mapToJSONString(paramsMap);
        System.out.println("request params: " + params);
        // 请求api服务
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/ai_dialog/generate_topic", null, params, "application/json");
        // 打印返回结果
        if (null == result) {
            return null;
        }
        String res = new String(result, StandardCharsets.UTF_8);
        System.out.println("result: " + res);
        try {
            JSONObject jsonObject = (JSONObject) JSONObject.parse(res);
            if (!"0".equals(jsonObject.getString("code"))) {
                return null;
            }
            return jsonObject.getJSONObject("data");
        } catch (Exception e) {
            System.out.println("parse generate topic result error");
            return null;
        }
    }

    /**
     * 获取默认场景接口
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static String getDefaultTopicHelper() throws NoSuchAlgorithmException {
        String q = "topics";  // q取固定值即可，主要是用于生成签名，限制长度 10字符
        // 添加鉴权相关参数
        Map<String, Object> paramsMap = AuthV3Util.addAuthParams(APP_KEY, APP_SECRET, q);
        // 添加请求参数
        paramsMap.put("q", q);
        String params = JSONUtil.mapToJSONString(paramsMap);
        System.out.println("request params: " + params);
        // 请求api服务
        byte[] result = HttpUtil.doPost("http://openapi.youdao.com/ai_dialog/get_default_topic", null, params, "application/json");
        // 打印返回结果
        if (null == result) {
            return null;
        }
        String res = new String(result, StandardCharsets.UTF_8);
        System.out.println("result: " + res);
        try {
            JSONObject jsonObject = (JSONObject) JSONObject.parse(res);
            if (!"0".equals(jsonObject.getString("code"))) {
                return null;
            }
            // 解析返回结果, 返回第一个话题的内容
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray jsonArray = data.getJSONArray("topicList");
            JSONObject firstLevel = jsonArray.getJSONObject(0);
            JSONArray firstLevelTopics = firstLevel.getJSONArray("topics");
            JSONObject targetTopic = firstLevelTopics.getJSONObject(0);
            return targetTopic.getString("enName");
        } catch (Exception e) {
            System.out.println("parse get default topics result error");
            return null;
        }
    }

    public static class RequestDTO {
        private String taskId;

        private String userLevel;

        private JSONObject scene;

        private JSONArray history;

        public void addHistoryItem(JSONObject historyItem) {
            this.history.add(historyItem);
        }

        public RequestDTO(String taskId, String userLevel, JSONObject scene, JSONArray history) {
            this.taskId = taskId;
            this.userLevel = userLevel;
            this.scene = scene;
            this.history = history;
        }

        public String getTaskId() {
            return taskId;
        }

        public String getUserLevel() {
            return userLevel;
        }

        public JSONObject getScene() {
            return scene;
        }

        public JSONArray getHistory() {
            return history;
        }
    }
}
