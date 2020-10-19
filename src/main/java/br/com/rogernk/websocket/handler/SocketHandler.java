package br.com.rogernk.websocket.handler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.rogernk.websocket.domain.PayloadMessage;

@Component
public class SocketHandler extends TextWebSocketHandler {

	@Value("${app.websocket-broadcast-session}")
	private boolean isBroadcastSession;

	List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		//the messages will be broadcasted to all users.
		System.out.println("#############################################");
		System.out.println("Connection Established");

		System.out.println("Manipulation Headers");
		HttpHeaders headers;
		if (session.getHandshakeHeaders() == null) {
			System.out.println("Empty Headers!!!");
		} else {
			headers = HttpHeaders.writableHttpHeaders(session.getHandshakeHeaders());
			headers.add("X-CUSTOM-HEADER", "CustomHeaderValue: " + LocalDateTime.now(ZoneId.systemDefault()).toString());	
		}

		if (isBroadcastSession) {
			System.out.println("Add Session to Broadcast Sessions");
			sessions.add(session);	
		}
	}

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {
		System.out.println("#############################################");

		if (isBroadcastSession) {
			for (WebSocketSession webSocketSession : sessions) {
				sendMessage(message, webSocketSession);
			}
		} else {
			sendMessage(message, session);
		}
	}

	private void sendMessage(TextMessage message, WebSocketSession session)
			throws JsonProcessingException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		PayloadMessage payloadMessage;
		System.out.println("#############################################");
		System.out.println("Handle Message");
		System.out.println("Read HttpHeaders:");
		HttpHeaders headers = session.getHandshakeHeaders();
		headers.forEach((key, value) -> {
			System.out.println("    Key: "+ key);
			System.out.println("    Value: "+ value);
		});
		
		TextMessage textMessage;
		System.out.println("#############################################");
		System.out.println("Read Payload Message:");
		System.out.println(message.getPayload());
		payloadMessage = mapper.readValue(message.getPayload(), PayloadMessage.class);
		textMessage = new TextMessage("Hello, " + payloadMessage.getName() + "!");
		
		System.out.println("#############################################");
		System.out.println("Publish Payload Message of return:");
		System.out.println(textMessage.getPayload());
		session.sendMessage(textMessage);
	}

}
