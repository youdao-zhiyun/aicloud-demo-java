package com.youdao.aicloud.translate;

import com.youdao.aicloud.translate.utils.AuthV4Util;
import com.youdao.aicloud.translate.utils.WebSocketUtil;
import okio.ByteString;

import java.util.HashMap;
import java.util.Map;

/**
 * 网易有道智云流式语音合成服务api调用demo
 * api接口: wss://openapi.youdao.com/stream_tts
 */
public class StreamTtsDemo {

    private static final String APP_KEY = "";     // 您的应用ID
    private static final String APP_SECRET = "";  // 您的应用密钥

    // 语音合成文本
    private static final String TEXT = "语音合成文本";

    public static void main(String[] args) throws Exception {
        // 添加请求参数
        Map<String, String[]> params = createRequestParams();
        // 添加鉴权相关参数
        AuthV4Util.addAuthParams(APP_KEY, APP_SECRET, params);
        // 创建websocket连接
        WebSocketUtil.initConnection("wss://openapi.youdao.com/stream_tts", params);
        // 发送合成数据
        sendData();
    }

    private static Map<String, String[]> createRequestParams() {
        /*
         * note: 将下列变量替换为需要请求的参数
         */
        String langType = "语言类型";
        String format = "音频格式";
        String voice = "发言人";
        String volume = "音量, 取值0.1-5";
        String speed = "语速, 取值0.5-2";
        String rate = "音频数据采样率";

        return new HashMap<String, String[]>() {{
            put("langType", new String[]{langType});
            put("format", new String[]{format});
            put("voice", new String[]{voice});
            put("volume", new String[]{volume});
            put("speed", new String[]{speed});
            put("rate", new String[]{rate});
        }};
    }

    private static void sendData() {
        String textMessage = String.format("{\"text\":\"%s\"}", TEXT);
        // 发送合成文本
        WebSocketUtil.sendTextMessage(textMessage);
        byte[] closebytes = "{\"end\": \"true\"}".getBytes();
        // 发送{"end":"true"}标识
        WebSocketUtil.sendBinaryMessage(ByteString.of(closebytes));
    }
}
