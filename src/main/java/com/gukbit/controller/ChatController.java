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
        List<String> authRoomNum = authUserDataService.getAcademyCode(userId);
        String authRoomName = "";

        if (customUserDetails.getUser().getRole().equals("ROLE_AUTH")) {
            authRoomName = academyService.getAcademyName(authRoomNum.get(0)).getName();
        }

        // 최근 참여방 리스트 : 이전에 채팅 사용한적 있으면 사용했던 채팅방 리스트 시간역순 상위 5개
        List<String> roomNums;
        if (!chatService.getMyChatroomList(userId).isEmpty()) {
            roomNums = chatService.getMyChatroomList(userId);
        } else {
            roomNums = authRoomNum;
        }

        List<String> roomNames = new ArrayList<>();
        for(String room : roomNums){
            roomNames.add(academyService.getAcademyName(room).getName());
        }

        model.addAttribute("roomNums", roomNums); // 참여방 번호
        model.addAttribute("roomNames", roomNames); // 참여방 리스트
        model.addAttribute("authRoomNum", authRoomNum); // 인증방 번호
        model.addAttribute("authRoomName", authRoomName); // 인증방 리스트

        return "view/chat/chat-roomlist";
    }

    // 채팅창 연결주소
    // /chat/room/123 이라고 호출하면 roomNum에 123 들어감
    // @PathVariable(required = false) 설정해두면 값 없을때도 동작
    @GetMapping(value = {"/chat/room/{roomNum}", "/chat/room"})
    public String room(@PathVariable(required = false) String roomNum, @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {

        String roomName;
        String userId = customUserDetails.getUser().getUserId();

        if (roomNum == null) {
            roomNum = "1"; // 전체채팅방의 경우 roomNum이 없음. db에 학원 코드 1로 넣기위해 값 설정.
            roomName = "전체 채팅방";
        } else { // 학원코드가 들어오면 학원 이름 넣어줌
            roomName = academyService.getAcademyName(roomNum).getName();
        }

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
