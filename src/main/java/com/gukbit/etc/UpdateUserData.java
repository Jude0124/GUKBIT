package com.gukbit.etc;

import com.gukbit.domain.AuthUserData;
import com.gukbit.domain.Rate;
import com.gukbit.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
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
}
