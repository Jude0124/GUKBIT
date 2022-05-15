package com.gukbit.service;

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

    // 메세지 저장하고 전송
    public void sendMessage(ChatDto message) {
        // 메세지를 db에 저장하고 메시지 전송
        chatRepository.save(message.toEntity());
        simpMessagingTemplate.convertAndSend("/topic/message/" + message.getAcademyCode(), message);
    }

    // 채팅방 메세지 리스트 출력
    public List<ChatDto> getMessageList(String academyCode) {
        // 유저가 들어간 채팅방의 채팅 기록을 불러오는 메서드
        return chatRepository.findByAcademyCode(academyCode);
    }

    // 채팅방 목록 출력
    public List<String> getMyChatroomList(String userId) {
        // 자기가 채팅한 기록이 있는 채팅방 리스트를 나오게 해야되나?
        // 나의 채팅방 이런 버튼 누르면 나오게 해야할듯
        return chatRepository.findByUserId(userId); // 아이디로 검색한 결과를 학원코드 distinct로 출력 - 채팅 한적 없으면 오류나옴
    }

    // 채팅방 검색 기능 -> 굳이 넣을 필요 있나? 그냥 자기 학원 채팅방만 하나 보여주면 되는거 아님?
    public void searchChatRoom(String keyword) {
        // 학원명 검색을 해서(키워드검색) -> 학원 게시판 모달 처럼 학원 리스트를 띄우고 -> 결과를 클릭하면 채팅방 입장하도록
        List<AcademyDto> academyDtoList = academyService.searchAcademy(keyword);
    }


}
