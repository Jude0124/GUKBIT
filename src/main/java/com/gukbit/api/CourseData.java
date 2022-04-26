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
<<<<<<< HEAD
    private String dFieldS = null;
=======
    private String dFieldSs = null;
>>>>>>> 77a910574f2365fa17153906771213fd8df3ebd7
    private String session = null;
    private String start = null;
    private String end = null;
    private String title = null;

    private String field_m = null;
    private String field_s = null;

<<<<<<< HEAD
    public CourseData(String academyId, String courseId, String dFieldS, String session, String start, String end,String title) {
        this.academyId = academyId;
        this.courseId = courseId;
        this.dFieldS = dFieldS;
=======
    public CourseData(String academyId, String courseId, String dFieldSs, String session, String start, String end,String title) {
        this.academyId = academyId;
        this.courseId = courseId;
        this.dFieldSs = dFieldSs;
>>>>>>> 77a910574f2365fa17153906771213fd8df3ebd7
        this.session = session;
        this.start = start;
        this.end = end;
        this.title = title;
<<<<<<< HEAD
        this.field_m = dFieldS.substring(0,4);
        this.field_s = dFieldS.substring(0,6);
=======
        this.field_m = dFieldSs.substring(0,4);
        this.field_s = dFieldSs.substring(0,6);
>>>>>>> 77a910574f2365fa17153906771213fd8df3ebd7
    }
}
