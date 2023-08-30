package com.youdao.aicloud.translate;

import com.youdao.aicloud.translate.utils.AuthV3Util;
import com.youdao.aicloud.translate.utils.FileUtil;
import com.youdao.aicloud.translate.utils.HttpUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * 网易有道智云语音识别服务api调用demo
 * api接口: https://openapi.youdao.com/asrapi
 */
public class AsrDemo {

    private static final String APP_KEY = "";     // 您的应用ID
    private static final String APP_SECRET = "";  // 您的应用密钥

    // 音频路径, 例windows路径：PATH = "C:\\youdao\\media.wav";
    private static final String PATH = "";

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        // 添加请求参数
        Map<String, String[]> params = createRequestParams();
        // 添加鉴权相关参数
        AuthV3Util.addAuthParams(APP_KEY, APP_SECRET, params);
        // 请求api服务
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/asrapi", null, params, "application/json");
        // 打印返回结果
        if (result != null) {
            System.out.println(new String(result, StandardCharsets.UTF_8));
        }
        System.exit(1);
    }

    private static Map<String, String[]> createRequestParams() throws IOException {
        /*
         * note: 将下列变量替换为需要请求的参数
         * 取值参考文档: https://ai.youdao.com/DOCSIRMA/html/tts/api/dyysb/index.html
         */
        String q = FileUtil.loadMediaAsBase64(PATH);
        String rate = "16000";
        String langType = "音频文件的语种";

        return new HashMap<String, String[]>() {{
            put("q", new String[]{q});
            put("rate", new String[]{rate});
            put("langType", new String[]{langType});
            put("format", new String[]{"wav"});
            put("channel", new String[]{"1"});
            put("docType", new String[]{"json"});
            put("type", new String[]{"1"});
        }};
    }
}
