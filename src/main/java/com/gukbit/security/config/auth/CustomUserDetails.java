package com.gukbit.security.config.auth;

import com.gukbit.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

//시큐리티가 /login주소요청을 낚아채서 로그인을 진행시킨다.
//로그인을 진행이 완료가 되면 시큐리티 session을 만들어 줍니다. (Security ContextHolder)
//오브젝트 타입 => Authentication 타입 객체
//Authentication 안에 User정보가 있어야 됨
//User오브젝트 타입 => UserDetails 타입 객체
//Security Session => Authentication => UserDetails(=PricipalDetails) 에서 User 접근
public class CustomUserDetails implements UserDetails, OAuth2User {
    private User user; //컴포지션
    private Map<String, Object> attributes;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public CustomUserDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    //해당 User의 권한을 리턴하는 메서드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add((GrantedAuthority) () -> user.getRole());
        return collect;
    }

    public User getUser(){return user;}

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserId();
    }

    //계정 만료됐니?
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정 잠겼니?
    @Override
    public boolean isAccountNonLocked() {
        if(user.getLockUser()){
            return true;
        }
        return false;
    }

    //
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정 활성화 되어있니?
    @Override
    public boolean isEnabled() {

        //우리 사이트에서 1년동아 회원이 로그인을 안하면 휴면계정으로 변경하고자 한다면
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }


    @Override
    public String getName() {
        return null;
    }

    @Override
    public int hashCode() {
        return this.getUsername().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CustomUserDetails){
            return this.getUsername().equals(((CustomUserDetails) obj).getUsername());
        }
        return false;
    }
}
