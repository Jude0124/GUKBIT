package com.gukbit.exception;

import org.springframework.security.core.AuthenticationException;

public class UserLockException extends AuthenticationException {
    public UserLockException(String msg){// 문자열을 매개변수로 받는 생성자
        super(msg);// 조상인 Exception 클래스의 생성자를 호출한다.
    }

    public UserLockException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
