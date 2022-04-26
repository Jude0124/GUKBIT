package com.gukbit.dto;

import com.gukbit.domain.Board;
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
  private String b_academy_name;
  private String b_academy_code;
  private String b_course_name;
  private String b_course_code;
  private Boolean visible;
  private Integer recommend;


  // Dto에서 필요한 부분을 빌더패턴을 통해 entity로 만드는 역할
  public Board toEntity(){
    Board build = Board.builder()
        .bid(bid)
        .author(author)
        .date(date)
        .view(view)
        .title(title)
        .content(content)
        .b_academy_name(b_academy_name)
        .b_academy_code(b_academy_code)
        .b_course_name(b_course_name)
        .b_course_code(b_course_code)
        .visible(visible)
        .recommend(recommend)
        .build();
    return build;
  }

}
