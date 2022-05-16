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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final AcademyService academyService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final AuthUserDataService authUserDataService;

    // 채팅방 리스트 출력 (전체, 학원 채팅방)
    @GetMapping("/chat/roomlist")
    public String chatRoomlist(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        String userId = customUserDetails.getUser().getUserId();
        System.out.println("loginUser = " + userId);

        // 이전에 채팅 사용한적 있으면 사용했던 채팅방 리스트 없으면 인증된 채팅방 리스트
        List<String> roomNums = new ArrayList<>();
        if (chatService.getMyChatroomList(userId) != null) {
            roomNums = chatService.getMyChatroomList(userId);
        } else {
            roomNums = authUserDataService.getAcademyCode(userId);
        }
        System.out.println("roomNums = " + roomNums);


        List<String> roomNames = new ArrayList<>();
        for(String room : roomNums){
            roomNames.add(academyService.getAcademyInfo(room).getName());
        }
        System.out.println("roomNames = " + roomNames);

        model.addAttribute("roomNums", roomNums);
        model.addAttribute("roomNames", roomNames);
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

        // 연결될때 이전 채팅 내역을 가져옴
        List<ChatDto> chatDtos = chatService.getMessageList(roomNum);
        model.addAttribute("chatData", chatDtos); // 해당 채팅방의 채팅 데이터

        return "view/chat/chat"; // 채팅창 반환
    }

    @MessageMapping(value = "/chat/enter")
    public void enter(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor){
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        simpMessagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getAcademyCode(), chatMessage);
    }

    @MessageMapping(value = "/chat/message")
    public void message(@Payload ChatMessage chatMessage, ChatDto chatDto){
        chatMessage.setSendTime(LocalDateTime.now());

        chatDto.setAcademyCode(chatMessage.getAcademyCode());
        chatDto.setUserId(chatMessage.getSender());
        chatDto.setChatContent(chatMessage.getContent());
        chatDto.setChatDate(chatMessage.getSendTime());
        chatService.saveMessage(chatDto);
        simpMessagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getAcademyCode(), chatMessage);
    }


}
