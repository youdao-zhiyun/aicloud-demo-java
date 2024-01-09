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
 * 网易有道智云图像矫正服务api调用demo
 * api接口: https://openapi.youdao.com/ocr_dewarp
 */
public class OcrDewarpDemo {

    private static final String APP_KEY = "";     // 您的应用ID
    private static final String APP_SECRET = "";  // 您的应用密钥

    // 待识别图片路径, 例windows路径：PATH = "C:\\youdao\\media.jpg";
    private static final String PATH = "";

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        // 添加请求参数
        Map<String, String[]> params = createRequestParams();
        // 添加鉴权相关参数
        AuthV3Util.addAuthParams(APP_KEY, APP_SECRET, params);
        // 请求api服务
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/ocr_dewarp", null, params, "application/json");
        // 打印返回结果
        if (result != null) {
            System.out.println(new String(result, StandardCharsets.UTF_8));
        }
        System.exit(1);
    }

    private static Map<String, String[]> createRequestParams() throws IOException {
        /*
         * note: 将下列变量替换为需要请求的参数
         * 取值参考文档: https://ai.youdao.com/DOCSIRMA/html/ocr/api/txjz/index.html
         */
        String q = FileUtil.loadMediaAsBase64(PATH);
        String angle = "0";            // 是否进行360角度识别 0：不识别，1：识别。
        String enhance = "0";          // 是否进行图像增强预处理
        String docDetect = "1";        // 是否进行图像检测
        String docDewarp = "1";        // 是否进行图像矫正,同时将自动跳过轮廓分割

        return new HashMap<String, String[]>() {{
            put("q", new String[]{q});
            put("angle", new String[]{angle});
            put("enhance", new String[]{enhance});
            put("docDetect", new String[]{docDetect});
            put("docDewarp", new String[]{docDewarp});
        }};
    }
}
