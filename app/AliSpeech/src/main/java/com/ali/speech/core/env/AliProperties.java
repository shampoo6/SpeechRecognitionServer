package com.ali.speech.core.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云服务基础配置
 */
@Data
@ConfigurationProperties(prefix = "ali-props")
@Component
public class AliProperties {
    private String appKey;
    private String accessKeyId;
    private String accessKeySecret;
}
