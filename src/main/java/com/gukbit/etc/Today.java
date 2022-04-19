package com.gukbit.etc;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.ZoneId;

@Getter
@Setter
public class Today {
    private String today = String.valueOf(LocalDate.now(ZoneId.of("Asia/Seoul")));
}
