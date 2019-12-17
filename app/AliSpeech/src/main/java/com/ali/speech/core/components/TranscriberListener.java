package com.ali.speech.core.components;

import com.ali.speech.core.controller.WebSocketSpeech;
import com.ali.speech.core.model.WebSocketResult;
import com.ali.speech.core.protocol.NoticeState;
import com.alibaba.nls.client.protocol.asr.SpeechTranscriberListener;
import com.alibaba.nls.client.protocol.asr.SpeechTranscriberResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
@Data
@AllArgsConstructor
public class TranscriberListener extends SpeechTranscriberListener {

    private WebSocketSpeech socket;
    private String sessionId;

    // 识别出中间结果.服务端识别出一个字或词时会返回此消息.仅当setEnableIntermediateResult(true)时,才会有此类消息返回
    @Override
    public void onTranscriptionResultChange(SpeechTranscriberResponse response) {
        log.info("name: " + response.getName() +
                // 状态码 20000000 表示正常识别
                ", status: " + response.getStatus() +
                // 句子编号，从1开始递增
                ", index: " + response.getTransSentenceIndex() +
                // 当前句子的中间识别结果
                ", result: " + response.getTransSentenceText() +
                // 当前已处理的音频时长，单位是毫秒
                ", time: " + response.getTransSentenceTime());
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", response.getName());
        map.put("status", response.getStatus());
        map.put("index", response.getTransSentenceIndex());
        map.put("result", response.getTransSentenceText());
        map.put("time", response.getTransSentenceTime());
        map.put("eventType", "onTranscriptionResultChange");
        socket.sendMessage(new WebSocketResult(sessionId, NoticeState.Ok, "识别出中间结果", map));
    }

    // 识别出一句话.服务端会智能断句,当识别到一句话结束时会返回此消息
    @Override
    public void onSentenceEnd(SpeechTranscriberResponse response) {
        log.info("name: " + response.getName() +
                // 状态码 20000000 表示正常识别
                ", status: " + response.getStatus() +
                // 句子编号，从1开始递增
                ", index: " + response.getTransSentenceIndex() +
                // 当前句子的完整识别结果
                ", result: " + response.getTransSentenceText() +
                // 当前已处理的音频时长，单位是毫秒
                ", time: " + response.getTransSentenceTime() +
                // SentenceBegin事件的时间，单位是毫秒
                ", begin time: " + response.getSentenceBeginTime() +
                // 识别结果置信度，取值范围[0.0, 1.0]，值越大表示置信度越高
                ", confidence: " + response.getConfidence());
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", response.getName());
        map.put("status", response.getStatus());
        map.put("index", response.getTransSentenceIndex());
        map.put("result", response.getTransSentenceText());
        map.put("time", response.getTransSentenceTime());
        map.put("beginTime", response.getSentenceBeginTime());
        map.put("confidence", response.getConfidence());
        map.put("eventType", "onSentenceEnd");
        socket.sendMessage(new WebSocketResult(sessionId, NoticeState.Ok, "识别出一句话", map));
    }

    // 识别完毕
    @Override
    public void onTranscriptionComplete(SpeechTranscriberResponse response) {
        log.info("name: " + response.getName() +
                ", status: " + response.getStatus());
    }

}
