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
 * 网易有道智云中文图片作文批改服务api调用demo
 * api接口: https://openapi.youdao.com/correct_writing_cn_image
 */
public class ZhImageCorrectWritingDemo {

    private static final String APP_KEY = "";     // 您的应用ID
    private static final String APP_SECRET = "";  // 您的应用密钥

    // 作文图片路径, 例windows路径：PATH = "C:\\youdao\\media.jpg";
    private static final String PATH = "";

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        // 添加请求参数
        Map<String, String[]> params = createRequestParams();
        // 添加鉴权相关参数
        AuthV3Util.addAuthParams(APP_KEY, APP_SECRET, params);
        // 请求api服务
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/correct_writing_cn_image", null, params, "application/json");
        // 打印返回结果
        if (result != null) {
            System.out.println(new String(result, StandardCharsets.UTF_8));
        }
        System.exit(1);
    }

    private static Map<String, String[]> createRequestParams() throws IOException {
        /*
         * note: 将下列变量替换为需要请求的参数
         * 取值参考文档: https://ai.youdao.com/DOCSIRMA/html/%E4%BD%9C%E6%96%87%E6%89%B9%E6%94%B9/API%E6%96%87%E6%A1%A3/%E4%B8%AD%E6%96%87%E4%BD%9C%E6%96%87%E6%89%B9%E6%94%B9%EF%BC%88%E5%9B%BE%E5%83%8F%E8%AF%86%E5%88%AB%EF%BC%89/%E4%B8%AD%E6%96%87%E4%BD%9C%E6%96%87%E6%89%B9%E6%94%B9%EF%BC%88%E5%9B%BE%E5%83%8F%E8%AF%86%E5%88%AB%EF%BC%89-API%E6%96%87%E6%A1%A3.html
         */
        String grade = "作文等级";
        String title = "作文标题";
        String requirement = "题目要求";

        // 数据的base64编码
        String q = FileUtil.loadMediaAsBase64(PATH);
        return new HashMap<String, String[]>() {{
            put("q", new String[]{q});
            put("grade", new String[]{grade});
            put("title", new String[]{title});
            put("requirement", new String[]{requirement});
        }};
    }
}
