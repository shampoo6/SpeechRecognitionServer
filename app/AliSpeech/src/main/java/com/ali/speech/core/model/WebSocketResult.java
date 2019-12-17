package com.ali.speech.core.model;

import com.ali.speech.core.protocol.NoticeState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketResult {
    private String sessionId;
    private NoticeState state;
    private String message;
    private Map data;

    public WebSocketResult(String sessionId, NoticeState state, String message){
        this(sessionId, state, message, null);
    }
}
