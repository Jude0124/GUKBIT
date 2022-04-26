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
    private String dFieldSs = null;
    private String session = null;
    private String start = null;
    private String end = null;
    private String title = null;

    private String field_m = null;
    private String field_s = null;

    public CourseData(String academyId, String courseId, String dFieldSs, String session, String start, String end,String title) {
        this.academyId = academyId;
        this.courseId = courseId;
        this.dFieldSs = dFieldSs;
        this.session = session;
        this.start = start;
        this.end = end;
        this.title = title;
        this.field_m = dFieldSs.substring(0,4);
        this.field_s = dFieldSs.substring(0,6);
    }
}
