package top.aprdec.websocketchatdemo.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class CustomWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        sessions.put(sessionId,session);
        log.info("WebSocket连接建立成功:{}",sessionId);
        log.info("sessions:{}",sessions);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("收到消息:{}",payload);
        String replyMessage = "服务器收到消息:"+payload;
        session.sendMessage(new TextMessage(replyMessage));
        broadcastMessage(payload);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String sessionId = session.getId();
        sessions.remove(sessionId);
        log.info("Websocket连接关闭:{}",sessionId);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("Websocket传输错误",exception);
    }

    public void broadcastMessage(String message){
        log.info("sessions:{}",sessions);
        sessions.values().forEach(session ->{
            try {
                log.info("广播消息ing");
                session.sendMessage(new TextMessage(message));
            }catch (IOException e){
                log.error("广播消息失败:",e);
            }
        });
    }

    @Scheduled(fixedRate = 10000)
    public void sendHeartbeat(){
        String heartbeat = "heartbeat";
        sessions.values().forEach(session->{
            try{
                session.sendMessage(new TextMessage(heartbeat));
            }catch (IOException e) {
                log.error("发送心跳消息失败", e);
            }
        });
    }
}
