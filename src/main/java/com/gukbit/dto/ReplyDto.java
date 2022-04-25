package com.gukbit.dto;

import com.gukbit.domain.Reply;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@RequiredArgsConstructor
@ToString
public class ReplyDto {
    private final Reply reply;
    private List<Reply> rReplyList = new ArrayList<>();
}
