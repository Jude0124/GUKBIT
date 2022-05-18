package com.gukbit.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chat {

    @Id
    @Column(name = "chat_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int chatIdx;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "academy_code")
    private String academyCode;

    @Column(name = "chat_content")
    private String chatContent;

    @CreatedDate
    @Column(name = "chat_date")
    private LocalDateTime chatDate;
}
