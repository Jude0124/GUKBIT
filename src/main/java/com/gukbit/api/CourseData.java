package com.gukbit.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CourseData {
    private String academyId; // 공통
    private String courseId;
    private String dFieldS;
    private String session;
    private String start;
    private String end;
    private String title;

    private String fieldM;
    private String fieldS;

    public CourseData(String academyId, String courseId, String dFieldS, String session, String start, String end,
            String title) {
        this.academyId = academyId;
        this.courseId = courseId;
        this.dFieldS = dFieldS;
        this.session = session;
        this.start = start;
        this.end = end;
        this.title = title;
        this.fieldM = dFieldS.substring(0, 4);
        this.fieldS = dFieldS.substring(0, 6);
    }
}
