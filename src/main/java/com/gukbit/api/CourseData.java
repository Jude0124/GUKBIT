package com.gukbit.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CourseData {
    private String academyId = null; //공통
    private String courseId = null;
    private String dFieldS = null;
    private String session = null;
    private String start = null;
    private String end = null;
    private String title = null;
    private String fieldM = null;
    private String fieldS = null;
    public CourseData(String academyId, String courseId, String dFieldS, String session, String start, String end,String title) {
        this.academyId = academyId;
        this.courseId = courseId;
        this.dFieldS = dFieldS;
        this.session = session;
        this.start = start;
        this.end = end;
        this.title = title;
        this.fieldM = dFieldS.substring(0,4);
        this.fieldS = dFieldS.substring(0,6);
    }
}
