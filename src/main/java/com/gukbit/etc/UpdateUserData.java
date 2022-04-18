package com.gukbit.etc;

import com.gukbit.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateUserData {
    private User user;

    private String changePassword;
    private String changePasswordCheck;

    public UpdateUserData(User user){
        this.user = user;
    }
}
