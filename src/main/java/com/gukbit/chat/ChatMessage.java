package com.gukbit.chat;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessage {

    private MessageType type;
    private String content;
    private String sender;
    private String academyCode;
    private String academyName;
    private LocalDateTime sendTime;

}
