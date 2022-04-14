package com.gukbit.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Data {
    private String academyName = null;
    private String academyId = null;
    private String trainingId = null;
    private String region = null;
    private String addr = null;
    private String tel = null;
    private String hpAddr = null;
    private String dummysession = "1";

    public Data(String academyName, String academyId, String trainingId, String region) {
        this.academyName = academyName;
        this.academyId = academyId;
        this.trainingId = trainingId;
        this.region = region;
    }
}
