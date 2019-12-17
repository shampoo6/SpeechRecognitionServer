package com.ali.speech.core.controller;

import com.ali.speech.core.components.AliSpeechApi;
import com.ali.speech.core.components.Transcriber;
import com.ali.speech.core.env.LocalAppContext;
import com.ali.speech.core.model.TranscriberConfig;
import com.ali.speech.core.model.WebSocketResult;
import com.ali.speech.core.protocol.NoticeState;
import com.fast.dev.core.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ServerEndpoint(value = "/speech")
@Controller
public class WebSocketSpeech {

    // 阿里api对象
    private AliSpeechApi api;

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    // 存储当前连接中的session，key是sessionId
    private Map<String, Transcriber> transcriberMap = new ConcurrentHashMap<>();

    /**
     * 获取阿里云语音api对象
     *
     * @return api对象
     */
    private AliSpeechApi getApi() {
        if (null != api) return api;
        this.api = (AliSpeechApi) LocalAppContext.appContext.getBean("aliSpeechApi");
        return this.api;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        // 创建成功的话就加入sessionMap
        String sessionId = session.getId();

        // 创建ali token，不成功就发送失败消息
        String token = getApi().createToken();
        if (null == token) {
            sendMessage(sessionId, NoticeState.Error, "获取阿里token失败");
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String qs = session.getQueryString();
            log.info(qs);
            TranscriberConfig transcriberConfig = TranscriberConfig.qsToObj(qs);
            // todo 应该封装createTranscriber方法，有空的时候封装
            // 创建和阿里的连接对象
            Transcriber transcriber = new Transcriber(this, session, token, getApi().getProps().getAppKey(), transcriberConfig);
            transcriberMap.put(sessionId, transcriber);
            sendMessage(sessionId, "连接成功");
            // 连接成功，在线数加1
            addOnlineCount();
            log.info("有新连接加入！当前在线人数为" + getOnlineCount());
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        //在线数减1
        subOnlineCount();
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
        // 从在线map中移除
        Transcriber trans = transcriberMap.remove(session.getId());
        // 关闭与阿里的连接
        trans.close();
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param buffer 客户端发送过来的音频信息
     */
    @OnMessage
    public void onMessage(byte[] buffer, Session session) {
        Transcriber transcriber = transcriberMap.get(session.getId());
        if (null != transcriber) {
            transcriber.send(buffer);
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    public void sendMessage(String sessionId, String message) {
        sendMessage(sessionId, NoticeState.Ok, message);
    }

    public void sendMessage(String sessionId, NoticeState state, String message) {
        sendMessage(new WebSocketResult(sessionId, state, message));
    }

    public void sendMessage(WebSocketResult result) {
        try {
            String json = JsonUtil.toJson(result);
            log.info(json);
            /** 这里有可能websocket断开连接了，但是阿里云的回调调到这里了，所以已经找不到sessionId了会报错 */
//            transcriberMap.get(result.getSessionId()).getSession().getBasicRemote().sendText(json);
            Transcriber transcriber = transcriberMap.get(result.getSessionId());
            if(null != transcriber){
                transcriber.getSession().getBasicRemote().sendText(json);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        WebSocketSpeech.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        WebSocketSpeech.onlineCount--;
    }

}
