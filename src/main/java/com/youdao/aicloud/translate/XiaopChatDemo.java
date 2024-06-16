package com.youdao.aicloud.translate;

import com.youdao.aicloud.translate.utils.AuthV3Util;
import com.youdao.aicloud.translate.utils.HttpUtil;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * 小p老师服务api调用demo
 * api接口: https://openapi.youdao.com/llmserver/ai/teacher/dialogue/chat
 */
public class XiaopChatDemo {

    /**
     * 您的应用ID
     */
    private static final String APP_KEY = "";

    /**
     * 您的应用密钥
     */
    private static final String APP_SECRET = "";

    public static void main(String[] args) throws NoSuchAlgorithmException {
        // 添加请求参数
        // 首轮对话参数说明
        //        Map<String, String[]> params = createFirstChatParams();
        // 非首轮对话参数说明
        //        Map<String, String[]> params = createNextChatParams();
        // 一个首轮对话的请求示例
        Map<String, String[]> params = createRequestParamExample();
        // 添加鉴权相关参数
        AuthV3Util.addXiaopAuthParams(APP_KEY, APP_SECRET, params);
        String urlStr = "https://openapi.youdao.com/llmserver/ai/teacher/dialogue/chat";

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
        HttpUtil.doPost(urlStr, null, params, listener);
    }

    /**
     * 示例
     *
     * @return
     */
    private static Map<String, String[]> createRequestParamExample() {
        return new HashMap<String, String[]>() {{
            put("user_id", new String[] {"user_test"});
            put("task_name", new String[] {"你好"});
            // 文本
            put("chat_info", new String[] {"[{\"type\":\"text\",\"content\":\"你好\"}]"});
            // 图片base64
//            put("chat_info", new String[] {
//                "[{\"type\":\"image\",\"content\":\"" + FileUtil.loadMediaAsBase64("D://1.png") + "\"}]"
//            });
        }};
    }

    private static Map<String, String[]> createFirstChatParams() {
        String userId = "【公共参数】用户id，不为空串，100个字符以内";
        String taskName = "【业务参数】任务名称，最多20个字符，首轮对话创建任务使用，为空时系统生成。";
        /**
         * 【业务参数】输入内容，格式为chat_item的列表（目前只支持一个chat_item），chat_item包含两个字段：
         * 一、参数说明
         * type：输入类型枚举【text、image、image_url】
         * content：输入内容，text文本/image图片ocr识别的结果有 token 4096 长度限制
         *
         * 二、type详细说明
         * text：文本输入UTF-8
         * 当 type = text 时，chat_info = [{"type":"text","content":"文本输入内容"}]
         *
         * image：图片base64编码：智云OCR支持的图片格式：支持27种语言的自动识别！支持图片格式：.bmp、.jpg、.png，图片大小Base64后≤2M
         * 当 type = image 时，chat_info = [{"type":"image","content":"图片base64编码"}]
         *
         * image_url：整张图片的URL：智云OCR支持的图片格式：支持27种语言的自动识别！支持图片格式：.bmp、.jpg、.png，图片大小Base64后≤2M
         * 当 type = image_url 时，chat_info = [{"type":"image_url","content":"图片的URL链接（需要公网能访问下载）"}]
         */
        String chatInfo = "【业务参数】对话内容";

        String templateId = "【业务参数】prompt模版id，可为空，取值与业务相关，请和开发人员联系";

        /**
         * sse流固定返回begin、message、end、error事件。对于其他想要的事件，需要调用方主动传递此参数订阅，多个订阅事件传值以英文逗号分隔，默认是空-无事件订阅。
         * 可以订阅的事件：
         * query_suggestion :插件能力，订阅才执行——猜你想问
         */
        String subscribe = "【业务参数】订阅事件";

        return new HashMap<String, String[]>() {{
            put("user_id", new String[] {userId});
            put("task_name", new String[] {taskName});
            put("chat_info", new String[] {chatInfo});
            put("template_id", new String[] {templateId});
            put("subscribe", new String[] {subscribe});
        }};
    }

    private static Map<String, String[]> createNextChatParams() {
        String userId = "【公共参数】用户id，不为空串，100个字符以内";
        String taskId = "【业务参数】任务id，用来标识用户一次会话session（关联一组对话历史），取接口返回的返回值";
        String parentChatId = "【业务参数】父对话id，用来标识唯一输入或输出，非首轮对话以接口返回的上一次对话为准";
        /**
         * 【业务参数】输入内容，格式为chat_item的列表（目前只支持一个chat_item），chat_item包含两个字段：
         * 一、参数说明
         * type：输入类型枚举【text、image、image_url】
         * content：输入内容，text文本/image图片ocr识别的结果有 token 4096 长度限制
         *
         * 二、type详细说明
         * text：文本输入UTF-8
         * 当 type = text 时，chat_info = [{"type":"text","content":"文本输入内容"}]
         *
         * image：图片base64编码：智云OCR支持的图片格式：支持27种语言的自动识别！支持图片格式：.bmp、.jpg、.png，图片大小Base64后≤2M
         * 当 type = image 时，chat_info = [{"type":"image","content":"图片base64编码"}]
         *
         * image_url：整张图片的URL：智云OCR支持的图片格式：支持27种语言的自动识别！支持图片格式：.bmp、.jpg、.png，图片大小Base64后≤2M
         * 当 type = image_url 时，chat_info = [{"type":"image_url","content":"图片的URL链接（需要公网能访问下载）"}]
         */
        String chatInfo = "【业务参数】对话内容";

        String templateId = "【业务参数】prompt模版id，可为空，取值与业务相关，请和开发人员联系";

        /**
         * sse流固定返回begin、message、end、error事件。对于其他想要的事件，需要调用方主动传递此参数订阅，多个订阅事件传值以英文逗号分隔，默认是空-无事件订阅。
         * 可以订阅的事件：
         * query_suggestion :插件能力，订阅才执行——猜你想问
         */
        String subscribe = "【业务参数】订阅事件";

        return new HashMap<String, String[]>() {{
            put("user_id", new String[] {userId});
            put("task_id", new String[] {taskId});
            put("parent_chat_id", new String[] {parentChatId});
            put("chat_info", new String[] {chatInfo});
            put("template_id", new String[] {templateId});
            put("subscribe", new String[] {subscribe});
        }};
    }
}
