package com.gukbit.service;

import com.gukbit.domain.Reply;
import com.gukbit.dto.ReplyDto;
import com.gukbit.repository.ReplyRepository;
import com.gukbit.security.config.auth.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ReplyService {
    private final ReplyRepository replyRepository;

    @Autowired
    public ReplyService(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    public void saveReply(Map<String, String> map, CustomUserDetails customUserDetails) {
        replyRepository.save(Reply.builder()
            .rBid(Integer.parseInt(map.get("rBid")))
            .rRid(Integer.parseInt(map.get("rRid")))
            .rAuthor(customUserDetails.getUser().getUserId())
            .rContent(map.get("text"))
            .rDate(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))) //db에서 date로 설정해 줄 것 같기 때문에 일단 임시
            .build());
    }

    public List<ReplyDto> getReplyList(int bid) {
        List<Reply> list = replyRepository.findAllByBid(bid);
        List<Reply> removeList = new ArrayList<>();

        List<ReplyDto> dtoList = new ArrayList<>();
        for (Reply reply : list) {
            if (reply.getRRid() == 0) {
                ReplyDto rdto = new ReplyDto(reply);
                dtoList.add(rdto);
                continue;
            }
            removeList.add(reply);
        }
        for (Reply reply : removeList) {
            rReplyDiv(dtoList, reply);
        }

        return dtoList;
    }

    public void rReplyDiv(List<ReplyDto> dtoList, Reply reply) {
        for (ReplyDto replyDto : dtoList) {
            if (replyDto.getReply().getRid().equals(reply.getRRid())) {
                replyDto.getRReplyList().add(reply);
            }
        }
    }

    public int countAllReply(Integer idx) {
        return replyRepository.findAllByBid(idx).size();
    }
}
