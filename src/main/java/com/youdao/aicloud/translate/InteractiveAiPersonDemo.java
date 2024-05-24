package com.youdao.aicloud.translate;

import com.youdao.aicloud.translate.utils.AuthV4Util;
import com.youdao.aicloud.translate.utils.HttpUtil;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * 网易有道智云交互数字人api调用demo
 * api接口: https://openapi.youdao.com/interactiveAiPerson/control
 */
public class InteractiveAiPersonDemo {

    private static final String APP_KEY = "";     // 您的应用ID
    private static final String APP_SECRET = "";  // 您的应用密钥

    public static void main(String[] args) throws NoSuchAlgorithmException {
        // 添加请求参数
        Map<String, String[]> params = createRequestParams();
        // 添加鉴权相关参数
        AuthV4Util.addAuthParams(APP_KEY, APP_SECRET, params);
        // 请求api服务
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/interactiveAiPerson/control", null, params, "application/json");
        // 打印返回结果
        if (result != null) {
            System.out.println(new String(result, StandardCharsets.UTF_8));
        }
        System.exit(1);
    }

    private static Map<String, String[]> createRequestParams() {
        /*
         * note: 将下列变量替换为需要请求的参数
         * 取值参考文档:
         */
        String command = "指令";
        String rid = "房间号";
        String text = "推送文本";
        String anchorId = "nertc";

        return new HashMap<String, String[]>() {{
            put("command", new String[]{command});
            put("rid", new String[]{rid});
            put("text", new String[]{text});
            put("anchorId", new String[]{anchorId});
        }};
    }
}
