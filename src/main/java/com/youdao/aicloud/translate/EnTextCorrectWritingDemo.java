package com.youdao.aicloud.translate;

import com.youdao.aicloud.translate.utils.AuthV3Util;
import com.youdao.aicloud.translate.utils.HttpUtil;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * 网易有道智云英文作文批改服务api调用demo
 * api接口: https://openapi.youdao.com/v2/correct_writing_text
 */
public class EnTextCorrectWritingDemo {

    private static final String APP_KEY = "";     // 您的应用ID
    private static final String APP_SECRET = "";  // 您的应用密钥

    public static void main(String[] args) throws NoSuchAlgorithmException {
        // 添加请求参数
        Map<String, String[]> params = createRequestParams();
        // 添加鉴权相关参数
        AuthV3Util.addAuthParams(APP_KEY, APP_SECRET, params);
        // 请求api服务
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/v2/correct_writing_text", null, params, "application/json");
        // 打印返回结果
        if (result != null) {
            System.out.println(new String(result, StandardCharsets.UTF_8));
        }
        System.exit(1);
    }

    private static Map<String, String[]> createRequestParams() {
        /*
         * note: 将下列变量替换为需要请求的参数
         * 取值参考文档: https://ai.youdao.com/DOCSIRMA/html/%E4%BD%9C%E6%96%87%E6%89%B9%E6%94%B9/API%E6%96%87%E6%A1%A3/%E8%8B%B1%E8%AF%AD%E4%BD%9C%E6%96%87%E6%89%B9%E6%94%B9%EF%BC%88%E6%96%87%E6%9C%AC%E8%BE%93%E5%85%A5%EF%BC%89/%E8%8B%B1%E8%AF%AD%E4%BD%9C%E6%96%87%E6%89%B9%E6%94%B9%EF%BC%88%E6%96%87%E6%9C%AC%E8%BE%93%E5%85%A5%EF%BC%89-API%E6%96%87%E6%A1%A3.html
         */
        String q = "正文文本";
        String grade = "作文等级";
        String title = "作文标题";
        String modelContent = "作文参考范文";
        String isNeedSynonyms = "是否查询同义词";
        String correctVersion = "作文批改版本：基础，高级";
        String isNeedEssayReport = "是否返回写作报告";

        return new HashMap<String, String[]>() {{
            put("q", new String[]{q});
            put("grade", new String[]{grade});
            put("title", new String[]{title});
            put("modelContent", new String[]{modelContent});
            put("isNeedSynonyms", new String[]{isNeedSynonyms});
            put("correctVersion", new String[]{correctVersion});
            put("isNeedEssayReport", new String[]{isNeedEssayReport});
        }};
    }
}
