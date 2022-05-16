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

    private String userId;
    private String academyCode;
    private String chatContent;
    private LocalDateTime chatDate;
    private String academyName;

    public Chat toEntity() {
        Chat build = Chat.builder()
                .userId(userId)
                .academyCode(academyCode)
                .chatContent(chatContent)
                .chatDate(chatDate)
                .build();

        return build;
    }
}
