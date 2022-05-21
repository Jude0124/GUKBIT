package com.gukbit.dto;

import com.gukbit.domain.Board;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BoardDto {
  private Integer bid;
  private String author;
  private String date;
  private Integer view;
  private String title;
  private String content;
  private String bAcademyCode;
  private String bAcademyName;
  private String bCourseName;
  private String bCourseCode;
  private Boolean visible;
  private Integer recommend;

  public void setDateNow() {
    this.date = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    //this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }

  // Dto에서 필요한 부분을 빌더패턴을 통해 entity로 만드는 역할
  public Board toEntity(){
    Board build = Board.builder()
        .bid(bid)
        .author(author)
        .date(date)
        .view(view)
        .title(title)
        .content(content)
        .bAcademyName(bAcademyName)
        .bAcademyCode(bAcademyCode)
        .bCourseName(bCourseName)
        .bCourseCode(bCourseCode)
        .visible(visible)
        .recommend(recommend)
        .build();
    return build;
  }

}
