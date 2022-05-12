package com.gukbit.dto;

import com.gukbit.domain.Chat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ChatDto {

    private int chatIdx;
    private String userId;
    private String academyCode;
    private String chatContent;
    private LocalDateTime chatDate;

    public Chat toEntity() {
        Chat build = Chat.builder()
                .chatIdx(chatIdx)
                .userId(userId)
                .academyCode(academyCode)
                .chatContent(chatContent)
                .chatDate(chatDate)
                .build();

        return build;
    }
}
