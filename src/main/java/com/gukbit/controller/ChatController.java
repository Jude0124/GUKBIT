package com.gukbit.controller;

import com.gukbit.chat.ChatMessage;
import com.gukbit.domain.AuthUserData;
import com.gukbit.domain.User;
import com.gukbit.dto.ChatDto;
import com.gukbit.security.config.auth.CustomUserDetails;
import com.gukbit.service.AcademyService;
import com.gukbit.service.AuthUserDataService;
import com.gukbit.service.ChatService;
import com.gukbit.session.SessionConst;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final AcademyService academyService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final AuthUserDataService authUserDataService;


//    // 익명채팅(데이터 저장 안됨)
//    @MessageMapping("/chat.sendMessage")
//    @SendTo("/topic/public")
//    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
//        return chatMessage;
//    }
//
//    // @MessageMapping에 정의된 엔드포인트로 접근된 메시징 요청은 내부 처리 후 @SendTo 어노테이션을 통해 정의된 "/topic/public" 대상의 모든 가입자들에게 전송
//    // WebSocket으로 들어오는 메시지 발행을 처리
//    @MessageMapping("/chat.addUser") // @MessageMapping("/chat.addUser") -> /app/chat.addUser
//    // 클라이언트는 prefix를 붙여서 [/app/chat.addUser]로 발행을 요청하면 MsgController가 해당 메시지를 받아서 처리
//    @SendTo("/topic/public")
//    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
//        // 페이로드(payload) : 헤더와 메타데이터를 제외한 실제 사용에 있어서 필요한 데이터
//        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
//        return chatMessage;
//    }

//    @GetMapping("/chat")
//    String chat(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
//        // 테스트용
//        String userId = customUserDetails.getUser().getUserId();
//        System.out.println("loginUser = " + userId);
//        return "view/chat/chat-anonymous";
//    }

    // 데이터 저장되는 채팅
    // 채팅방 리스트 출력 (전체, 학원 채팅방)
    @GetMapping("/chat/roomlist")
    public String chatRoomlist(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        String userId = customUserDetails.getUser().getUserId();
        System.out.println("loginUser = " + userId);

//        List<String> roomlist = chatService.getMyChatroomList(userId);
        List<String> roomlist = authUserDataService.getAcademyCode(userId);
        System.out.println("roomlist = " + roomlist);


        List<String> roomName = new ArrayList<>();
        for(String room : roomlist){
            roomName.add(academyService.getAcademyInfo(room).getName());
        }
        System.out.println("roomName = " + roomName);

        model.addAttribute("roomlist", roomlist);
        model.addAttribute("roomName", roomName);
        return "view/chat/chat-roomlist";
    }

    // 채팅창 연결주소
    // /chat/room/123 이라고 호출하면 roomNum에 123 들어감
    @GetMapping("/chat/room/{roomNum}")
    public String room(@PathVariable String roomNum, @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        String userId = customUserDetails.getUser().getUserId();
        String roomName = academyService.getAcademyInfo(roomNum).getName();

        model.addAttribute("roomName", roomName); // 방 이름
        model.addAttribute("roomNum", roomNum); // 방 번호
        model.addAttribute("userId", userId); // 회원 이름

        //연결될때 이전 채팅 내역을 가져와야한다.
//        List<ChatDto> chatDtos = chatService.getMessageList(roomNum);
//        model.addAttribute("chatData", chatDtos); // 해당 채팅방의 채팅 데이터

        return "view/chat/chat"; // 채팅창 반환
    }

    @MessageMapping(value = "/chat/enter")
    public void enter(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor){
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        simpMessagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getAcademyCode(), chatMessage.getContent());
    }

    @MessageMapping(value = "/chat/message")
    public void message(@Payload ChatMessage chatMessage){
        simpMessagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getAcademyCode(), chatMessage.getContent());
    }


}
