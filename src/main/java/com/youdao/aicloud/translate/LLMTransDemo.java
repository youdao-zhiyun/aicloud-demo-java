package com.youdao.aicloud.translate;

import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import com.youdao.aicloud.translate.utils.AuthV3Util;
import com.youdao.aicloud.translate.utils.HttpUtil;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * 网易有道智云大模型翻译服务api调用demo
 * api接口: https://openapi.youdao.com/llm_trans
 */
public class LLMTransDemo {

    private static final String APP_KEY = "";     // 您的应用ID
    private static final String APP_SECRET = "";  // 您的应用密钥

    public static void main(String[] args) throws NoSuchAlgorithmException {
        // 添加请求参数
        Map<String, String[]> params = createRequestParams();
        // 添加鉴权相关参数
        AuthV3Util.addAuthParams(APP_KEY, APP_SECRET, params);

        EventSourceListener listener = new EventSourceListener() {
            @Override
            public void onOpen(EventSource eventSource, Response response) {
                System.out.println("建立连接成功：");
            }

            @Override
            public void onEvent(EventSource eventSource, String id, String type, String data) {
                System.out.println(data);
            }

            @Override
            public void onClosed(EventSource eventSource) {
                System.out.println("连接关闭");
                System.exit(1);
            }

            @Override
            public void onFailure(EventSource eventSource, Throwable t, Response response) {
                System.out.println("连接失败");
                System.exit(1);
            }
        };
        HttpUtil.doPost("https://openapi.youdao.com/llm_trans", null, params, listener);
    }

    private static Map<String, String[]> createRequestParams() {
        /*
         * note: 将下列变量替换为需要请求的参数
         * 取值参考文档:https://ai.youdao.com/DOCSIRMA/html/trans/api/dmxfy/index.html
         */
        String i = "待翻译文本";
        String from = "源语种";
        String to = "目标语种";

        return new HashMap<String, String[]>() {{
            put("i", new String[]{i});
            put("q", new String[]{i});
            put("from", new String[]{from});
            put("to", new String[]{to});
        }};
    }
}