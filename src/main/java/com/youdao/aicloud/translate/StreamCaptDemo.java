package com.youdao.aicloud.translate;

import okio.ByteString;
import com.alibaba.fastjson.JSONObject;
import com.youdao.aicloud.translate.utils.AuthV4Util;
import com.youdao.aicloud.translate.utils.WebSocketUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 网易有道智云实时语音评测服务api调用demo
 * api接口: wss://openapi.youdao.com/stream_capt
 */
public class StreamCaptDemo {

    private static final String APP_KEY = "";     // 您的应用ID
    private static final String APP_SECRET = "";  // 您的应用密钥

    // 音频路径, 例windows路径：PATH = "C:\\youdao\\media.wav";
    private static final String PATH = "";
    // 评测文本
    private static final String TEXT = "";

    public static void main(String[] args) throws Exception {
        // 添加请求参数
        Map<String, String[]> params = createRequestParams();
        // 添加鉴权相关参数
        AuthV4Util.addAuthParams(APP_KEY, APP_SECRET, params);
        // 创建websocket连接
        WebSocketUtil.initConnection("wss://openapi.youdao.com/stream_capt", params);
        // 发送流式数据
        sendData(TEXT, PATH, 6400);
    }

    private static Map<String, String[]> createRequestParams() {
        /*
         * note: 将下列变量替换为需要请求的参数
         * 取值参考文档: https://ai.youdao.com/DOCSIRMA/html/tts/price/ssyypc/index.html
         */
        String langType = "语种";
        String rate = "16000";
        String format = "wav";

        return new HashMap<String, String[]>() {{
            put("langType", new String[]{langType});
            put("rate", new String[]{rate});
            put("format", new String[]{format});
            put("channel", new String[]{"1"});
            put("version", new String[]{"v1"});
        }};
    }

    private static void sendData(String text, String path, int step) throws IOException, InterruptedException {
        JSONObject textMessage = new JSONObject();
        textMessage.put("text", text);
        WebSocketUtil.sendTextMessage(textMessage.toJSONString());
        InputStream inputStream = new FileInputStream(path);
        byte[] data = new byte[step];
        int readSize = 0;
        while ((readSize = inputStream.read(data)) != -1) {
            WebSocketUtil.sendBinaryMessage(ByteString.of(data));
            Thread.sleep(200);
        }
        byte[] closebytes = "{\"end\": \"true\"}".getBytes();
        WebSocketUtil.sendBinaryMessage(ByteString.of(closebytes));
    }
}
