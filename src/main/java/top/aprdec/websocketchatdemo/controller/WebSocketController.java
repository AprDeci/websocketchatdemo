package top.aprdec.websocketchatdemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.aprdec.websocketchatdemo.handler.CustomWebSocketHandler;

@RestController
@RequestMapping("/api/websocket")
@Slf4j
public class WebSocketController {

    private final CustomWebSocketHandler webSocketHandler;

    public WebSocketController(CustomWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }


    @PostMapping("/broadcast")
    public ResponseEntity<String> broadcastMessage(@RequestParam String message){
        webSocketHandler.broadcastMessage(message);
        return ResponseEntity.ok("消息广播成功");
    }

}
