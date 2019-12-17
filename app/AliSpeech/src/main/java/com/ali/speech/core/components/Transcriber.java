package com.ali.speech.core.components;

import com.ali.speech.core.controller.WebSocketSpeech;
import com.ali.speech.core.model.TranscriberConfig;
import com.alibaba.nls.client.protocol.InputFormatEnum;
import com.alibaba.nls.client.protocol.NlsClient;
import com.alibaba.nls.client.protocol.SampleRateEnum;
import com.alibaba.nls.client.protocol.asr.SpeechTranscriber;
import lombok.Data;

import javax.websocket.Session;

/**
 * 封装阿里的SpeechTranscriber
 */
@Data
public class Transcriber {

    private WebSocketSpeech socket;

    private Session session;
    private String sessionId;
    private String appKey;
    private String accessToken;
    private NlsClient client;
    private SpeechTranscriber transcriber;

    public Transcriber(WebSocketSpeech socket, Session session, String accessToken, String appKey, TranscriberConfig config) {
        this.socket = socket;
        this.session = session;
        this.sessionId = this.session.getId();
        this.appKey = appKey;
        this.accessToken = accessToken;

        try {
            // Step0 创建NlsClient实例,应用全局创建一个即可,默认服务地址为阿里云线上服务地址
            client = new NlsClient(this.accessToken);
            // Step1 创建实例,建立连接
            transcriber = new SpeechTranscriber(client, new TranscriberListener(socket, sessionId));
            transcriber.setAppKey(this.appKey);
            // 输入音频编码方式
            transcriber.setFormat(InputFormatEnum.PCM);
            // 输入音频采样率
            transcriber.setSampleRate(SampleRateEnum.SAMPLE_RATE_16K);
            // 是否返回中间识别结果
            transcriber.setEnableIntermediateResult(config.getEnableIntermediateResult());
            // 是否生成并返回标点符号
            transcriber.setEnablePunctuation(config.getEnablePunctuation());
            // 是否将返回结果规整化,比如将一百返回为100
            transcriber.setEnableITN(config.getEnableITN());

            // Step2 此方法将以上参数设置序列化为json发送给服务端,并等待服务端确认
            transcriber.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送音频数据
     * @param buffer 前端传来的音频数据
     */
    public void send(byte[] buffer) {
        try {
            // Step3 读取麦克风数据
            transcriber.send(buffer);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * 关闭和阿里云的连接
     */
    public void close() {
        try {
            if (null != this.transcriber) {
                this.transcriber.stop();
                this.transcriber.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
