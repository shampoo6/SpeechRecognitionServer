package com.ali.speech.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TranscriberConfig {
    // 是否返回中间识别结果
    private Boolean enableIntermediateResult;
    // 是否生成并返回标点符号
    private Boolean enablePunctuation;
    // 是否将返回结果规整化,比如将一百返回为100
    private Boolean enableITN;

    public TranscriberConfig() {
        this.enableIntermediateResult = false;
        this.enablePunctuation = false;
        this.enableITN = false;
    }

    /**
     * 将queryString转换成TranscriberConfig对象
     *
     * @return TranscriberConfig对象
     */
    public static TranscriberConfig qsToObj(String qs) {
        String[] qsSplit = qs.split("&");
        if (qsSplit.length == 0) {
            return new TranscriberConfig();
        }
        TranscriberConfig config = new TranscriberConfig();
        for (String param : qsSplit) {
            if (param.contains("enableIntermediateResult")) {
                config.setEnableIntermediateResult(TranscriberConfig.getValue(param));
            }
            else if (param.contains("enablePunctuation")) {
                config.setEnablePunctuation(TranscriberConfig.getValue(param));
            }
            else if (param.contains("enableITN")) {
                config.setEnableITN(TranscriberConfig.getValue(param));
            }
        }
        return config;
    }

    private static Boolean getValue(String param) {
        String[] split = param.split("=");
        if (split.length == 2) {
            return Boolean.valueOf(split[1]);
        } else {
            return false;
        }
    }
}
