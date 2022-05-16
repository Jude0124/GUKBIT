package com.gukbit.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
// AcademyVO
@Getter
@Setter
@ToString
public class AcademyData {
    private String academyName = null;
    private String academyId = null; //공통
    private String trainingId = null; //공통
    private String region = null;
    private String addr = null;
    private String tel = null;
    private String hpAddr = null;
    private String dummysession = "1";

    public AcademyData(String academyName, String academyId, String trainingId, String region) {
        this.academyName = academyName;
        this.academyId = academyId;
        this.trainingId = trainingId;
        this.region = region;
    }
}
