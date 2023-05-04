package com.youdao.aicloud.translate;

import com.youdao.aicloud.translate.utils.AuthV4Util;
import com.youdao.aicloud.translate.utils.WebSocketUtil;
import okio.ByteString;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 网易有道智云流式语音翻译服务api调用demo
 * api接口: wss://openapi.youdao.com/stream_speech_trans
 */
public class StreamSpeechTransDemo {

    private static final String APP_KEY = "";     // 您的应用ID
    private static final String APP_SECRET = "";  // 您的应用密钥

    // 识别音频路径, 例windows路径：PATH = "C:\\youdao\\media.wav";
    private static final String PATH = "";

    public static void main(String[] args) throws Exception {
        // 添加请求参数
        Map<String, String[]> params = createRequestParams();
        // 添加鉴权相关参数
        AuthV4Util.addAuthParams(APP_KEY, APP_SECRET, params);
        // 创建websocket连接
        WebSocketUtil.initConnection("wss://openapi.youdao.com/stream_speech_trans", params);
        // 发送流式数据
        sendData(PATH, 6400);
    }

    private static Map<String, String[]> createRequestParams() {
        /*
         * note: 将下列变量替换为需要请求的参数
         * 取值参考文档: https://ai.youdao.com/DOCSIRMA/html/%E5%AE%9E%E6%97%B6%E8%AF%AD%E9%9F%B3%E7%BF%BB%E8%AF%91/API%E6%96%87%E6%A1%A3/%E5%AE%9E%E6%97%B6%E8%AF%AD%E9%9F%B3%E7%BF%BB%E8%AF%91%E6%9C%8D%E5%8A%A1/%E5%AE%9E%E6%97%B6%E8%AF%AD%E9%9F%B3%E7%BF%BB%E8%AF%91%E6%9C%8D%E5%8A%A1-API%E6%96%87%E6%A1%A3.html
         */
        String from = "源语言语种";
        String to = "目标语言语种";
        String rate = "音频数据采样率, 推荐16000";
        String format = "音频格式, 推荐wav";

        return new HashMap<String, String[]>() {{
            put("from", new String[]{from});
            put("to", new String[]{to});
            put("format", new String[]{format});
            put("channel", new String[]{"1"});
            put("version", new String[]{"v1"});
            put("rate", new String[]{rate});
        }};
    }

    private static void sendData(String path, int step) throws IOException, InterruptedException {
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
