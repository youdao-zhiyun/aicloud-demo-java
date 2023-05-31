package com.youdao.aicloud.translate;

import okio.ByteString;
import com.youdao.aicloud.translate.utils.AuthV4Util;
import com.youdao.aicloud.translate.utils.WebSocketUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AsrliteDemo {

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
        WebSocketUtil.initConnection("wss://openapi.youdao.com/stream_asropenapi", params);
        // 发送流式数据
        sendData(PATH, 6400);
    }

    private static Map<String, String[]> createRequestParams() {
        /*
         * note: 将下列变量替换为需要请求的参数
         * 取值参考文档: https://ai.youdao.com/DOCSIRMA/html/tts/api/ssyysb/index.html
        */
        String langType = "语种";
        String rate = "采样率 推荐16000";

        return new HashMap<String, String[]>() {{
            put("langType", new String[]{langType});
            put("rate", new String[]{rate});
            put("format", new String[]{"wav"});
            put("channel", new String[]{"1"});
            put("version", new String[]{"v1"});
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
