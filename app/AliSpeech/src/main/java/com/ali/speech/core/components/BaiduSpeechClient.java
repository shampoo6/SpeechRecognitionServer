//package com.ali.speech.core.components;
//
//import com.ali.speech.core.env.BaiduProperties;
//import com.ali.speech.core.env.LocalAppContext;
//import com.baidu.aip.nlp.AipNlp;
//import lombok.extern.slf4j.Slf4j;
//import org.json.JSONObject;
//
//import java.util.HashMap;
//
//@Slf4j
//class BaiduSpeechClient {
//    private static BaiduSpeechClient instance;
//    private AipNlp client;
//
//    private BaiduSpeechClient() {
//        BaiduProperties props = (BaiduProperties) LocalAppContext.appContext.getBean("baiduProperties");
//        // 初始化一个AipNlp
//        client = new AipNlp(props.getAppId(), props.getApiKey(), props.getSecretKey());
//
//        // 可选：设置网络连接参数
//        client.setConnectionTimeoutInMillis(2000);
//        client.setSocketTimeoutInMillis(60000);
//
//        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        // client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//        // client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理
//
//        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
//        // 也可以直接通过jvm启动参数设置此环境变量
//        // System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");
//
//        // 调用接口
//        // String text = "百度是一家高科技公司";
//        // JSONObject res = client.lexer(text, null);
//        // System.out.println(res.toString(2));
//
//    }
//
//    public static BaiduSpeechClient getClient() {
//        if (null != BaiduSpeechClient.instance) {
//            return BaiduSpeechClient.instance;
//        }
//        BaiduSpeechClient.instance = new BaiduSpeechClient();
//        return BaiduSpeechClient.instance;
//    }
//
//    public Object lexer(String text) {
//        return lexer(text, new HashMap<>());
//    }
//
//    public Object lexer(String text, HashMap<String, Object> options) {
//        JSONObject res = client.lexer(text, options);
//        log.info(res.toString(2));
//        return null;
//    }
//}
