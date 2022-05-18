package com.gukbit.service;

import com.gukbit.chat.ChatMessage;
import com.gukbit.domain.Chat;
import com.gukbit.dto.AcademyDto;
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

    private final ChatRepository chatRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final AcademyService academyService;


    public void saveMessage(ChatDto message) {
        // 메세지를 db에 저장
        chatRepository.save(message.toEntity());
    }

    // 채팅방 메세지 리스트 출력
    public List<ChatDto> getMessageList(String academyCode) {
        // 유저가 들어간 채팅방의 채팅 기록을 불러오는 메서드
        return chatRepository.findByAcademyCode(academyCode);
    }

    // 채팅방 목록 출력
    public List<String> getMyChatroomList(String userId) {
        // 자기가 채팅한 기록이 있는 채팅방 리스트를 나오게...
        return chatRepository.findByUserId(userId); // 아이디로 검색한 결과를 학원코드 distinct로 출력
    }
}
