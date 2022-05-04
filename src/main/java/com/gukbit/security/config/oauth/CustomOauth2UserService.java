package com.gukbit.security.config.oauth;

import com.gukbit.domain.User;
import com.gukbit.repository.UserRepository;
import com.gukbit.security.config.auth.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOauth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private UserRepository userRepository;

    //구글로부터 받은 userRequest 데이터에 대해 후처리되는 함수
    //함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        //구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인 완료 -> code를 리턴(OAuth-Client라이브러리) -> AccessToken요청
        //userRequest정보 -> loadUser함수 -> 구글로부터 회원 프로필 받아준다.

        String provider = userRequest.getClientRegistration().getRegistrationId(); //google
        String providerId = oAuth2User.getAttribute("sub");
        String userId = provider + "-" + providerId;
        String password = new BCryptPasswordEncoder().encode("비밀번호");
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUserId(userId);
        if(userEntity == null){
            System.out.println("구글 로그인이 최초입니다.");
            userEntity = User.builder()
                .userId(userId)
                .password(password)
                .email(email)
                .role(role)
                .provider(provider)
                .providerId(providerId)
                .tel("010-0000-0000") //임시값
                .lockUser(false)
                .build();
            System.out.println("userEntity = " + userEntity);
            userRepository.save(userEntity);
        }else{
            System.out.println("구글 로그인을 이미 한적이 있습니다. 당신은 자동회원가입이 되어있습니다.");
        }

        return new CustomUserDetails(userEntity, oAuth2User.getAttributes());
    }
}
