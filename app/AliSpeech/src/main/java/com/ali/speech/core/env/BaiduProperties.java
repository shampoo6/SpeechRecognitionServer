package com.ali.speech.core.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 百度云服务基础配置
 */
@Data
@ConfigurationProperties(prefix = "baidu-props")
@Component
public class BaiduProperties {
    private String appId;
    private String apiKey;
    private String secretKey;
}
