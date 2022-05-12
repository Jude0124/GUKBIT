package com.gukbit.service;

import com.gukbit.domain.Chat;
import com.gukbit.dto.ChatDto;
import com.gukbit.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    @Autowired
    private final ChatRepository chatRepository;
    @Autowired
    private final SimpMessagingTemplate simpMessagingTemplate;

    // 메세지 저장하고 전송
    public void sendMessage(ChatDto message) {
        chatRepository.save(message.toEntity());
        simpMessagingTemplate.convertAndSend("/topic/message/" + message.getAcademyCode(), message);
    }

    // 메세지 리스트 출력
    public List<ChatDto> getMessageList(String academyCode) {
          return chatRepository.findByAcademyCode(academyCode);
    }

    // 채팅방 목록 출력
    public List<ChatDto> getChatroomList(String userId) {
        return chatRepository.findByUserId(userId);
    }


}
