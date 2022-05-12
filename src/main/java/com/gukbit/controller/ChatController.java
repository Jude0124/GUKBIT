package com.gukbit.controller;

import com.gukbit.chat.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    // @MessageMapping에 정의된 엔드포인트로 접근된 메시징 요청은 내부 처리 후 @SendTo 어노테이션을 통해 정의된 "/topic/public" 대상의 모든 가입자들에게 전송
    // WebSocket으로 들어오는 메시지 발행을 처리
    @MessageMapping("/chat.addUser") // @MessageMapping("/chat.addUser") -> /app/chat.addUser
    // 클라이언트는 prefix를 붙여서 [/app/chat.addUser]로 발행을 요청하면 MsgController가 해당 메시지를 받아서 처리
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // 페이로드(payload) : 헤더와 메타데이터를 제외한 실제 사용에 있어서 필요한 데이터
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

    // 채팅방 리스트 출력 (전체, 학원 채팅방)


}
