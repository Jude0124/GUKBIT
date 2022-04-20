package com.gukbit.etc;

import com.gukbit.domain.AuthUserData;
import com.gukbit.domain.Rate;
import com.gukbit.domain.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class UpdateUserData {
    private User user;

    private String changePassword;
    private String changePasswordCheck;

    private String academyCode;
    private String courseId;

    private AuthUserData authUserData;
    private Rate rate;

    public UpdateUserData(User user){
        this.user = user;
    }

    public AuthUserData getAuthUserData() {
        return authUserData;
    }

    public Rate getRate() {
        return rate;
    }
}
