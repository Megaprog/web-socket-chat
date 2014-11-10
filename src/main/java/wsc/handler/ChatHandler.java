package wsc.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatHandler extends TextWebSocketHandler {

    @Autowired
    private ObjectMapper objectMapper;

    private final Map<WebSocketSession, String> users = new ConcurrentHashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String name = users.get(session);
        if (name == null) {
            name = message.getPayload();
            users.put(session, name);
            send(name + " connected");
        }
        else {
            final Map<String, String> json = new HashMap<>();
            json.put("user", name);
            json.put("text", message.getPayload());
            send(objectMapper.writeValueAsString(json));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        final String name = users.remove(session);
        if (name != null) {
            send(name + " disconnected");
        }
    }

    private void send(String text) throws IOException {
        for (WebSocketSession session : users.keySet()) {
            session.sendMessage(new TextMessage(text));
        }
    }
}
