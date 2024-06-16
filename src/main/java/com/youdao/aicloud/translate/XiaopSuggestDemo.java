package com.youdao.aicloud.translate;


import com.youdao.aicloud.translate.utils.AuthV3Util;
import com.youdao.aicloud.translate.utils.HttpUtil;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * 网易有道猜你想问调用demo
 * api接口: https://openapi.youdao.com/llmserver/plugin/suggest
 */
public class XiaopSuggestDemo {

    /**
     * 您的应用ID
     */
    private static final String APP_KEY = "";

    /**
     * 您的应用密钥
     */
    private static final String APP_SECRET = "";

    public static void main(String[] args) throws NoSuchAlgorithmException {
        // 添加请求参数
//        Map<String, String[]> params = createSuggestParams();
        // 一个请求示例
        Map<String, String[]> params = createRequestParamExample();
        // 添加鉴权相关参数
        AuthV3Util.addXiaopAuthParams(APP_KEY, APP_SECRET, params);
        String urlStr = "https://openapi.youdao.com/llmserver/plugin/suggest";
        String response = new String(HttpUtil.doPost(urlStr, null, params, "application/json"));
        System.out.println(response);
    }

    /**
     * 示例
     * @return
     */
    private static Map<String, String[]> createRequestParamExample() {
        return new HashMap<String, String[]>() {{
            put("user_id", new String[]{"user_test"});
            put("query", new String[]{"李白是哪个朝代的诗人？"});
            put("answer", new String[]{"李白是唐朝的浪漫主义诗人。"});
        }};
    }

    private static Map<String, String[]> createSuggestParamsById() {
        String userId = "【公共参数】用户id，不为空串，100个字符以内";
        String taskId = "【业务参数】任务id，请求对话接口的begin事件中返回";
        String chatId = "【业务参数】对话id，请求对话接口的begin事件中返回";

        return new HashMap<String, String[]>() {{
            put("user_id", new String[]{userId});
            put("task_id", new String[]{taskId});
            put("chat_id", new String[]{chatId});
        }};
    }

    private static Map<String, String[]> createSuggestParams() {
        String userId = "【公共参数】用户id，不为空串，100个字符以内";
        String query = "【业务参数】需要推荐问题的原始问题";
        String answer = "【业务参数】原始问题的回答";

        return new HashMap<String, String[]>() {{
            put("user_id", new String[]{userId});
            put("query", new String[]{query});
            put("answer", new String[]{answer});
        }};
    }
}
