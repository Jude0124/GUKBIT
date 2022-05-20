package com.gukbit.security.config.oauth;

import com.gukbit.domain.User;
import com.gukbit.exception.UserLockException;
import com.gukbit.repository.UserRepository;
import com.gukbit.security.config.auth.CustomUserDetails;
import com.gukbit.security.config.auth.CustomUserDetailsService;
import com.gukbit.security.config.oauth.provider.FacebookUserInfo;
import com.gukbit.security.config.oauth.provider.GoogleUserInfo;
import com.gukbit.security.config.oauth.provider.NaverUserInfo;
import com.gukbit.security.config.oauth.provider.OAuth2UserInfo;
import java.util.Map;
import java.util.UUID;

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

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    //구글로부터 받은 userRequest 데이터에 대해 후처리되는 함수
    //함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("CustomOauth2UserService.loadUser");
        OAuth2User oAuth2User = super.loadUser(userRequest);
        //구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인 완료 -> code를 리턴(OAuth-Client라이브러리) -> AccessToken요청
        //userRequest정보 -> loadUser함수 -> 구글로부터 회원 프로필 받아준다.

        System.out.println("oAuth2User = " + oAuth2User);
        OAuth2UserInfo oAuth2UserInfo = null;
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")){
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")){
            System.out.println("페이스북 로그인 요청");
            oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            System.out.println("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
        }else{
            System.out.println("우리는 구글과 페이스북만 지원해요");
        }

        String provider = oAuth2UserInfo.getProvider(); //google
        String providerId = oAuth2UserInfo.getProviderId();
        String userId = provider + userRepository.getlastUid()+"";
        String password = new BCryptPasswordEncoder().encode("비밀번호");
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_USER";


        User userEntity = userRepository.findByUserId(userId);

        if(userEntity == null){
            if(provider.equals("google")){
                System.out.println("구글 로그인이 최초입니다.");
            }else if(provider.equals("facebook")){
                System.out.println("페이스북 로그인이 최초입니다.");
            }
            else if(provider.equals("naver")){
                System.out.println("네이버 로그인이 최초입니다.");
            }
            userEntity = User.builder()
                .userId(userId)
                .password(password)
                .email(email)
                .role(role)
                .provider(provider)
                .providerId(providerId)
                .tel(UUID.randomUUID().toString()) //임시값
                .lockUser(false)
                .build();
            System.out.println("userEntity = " + userEntity);
            userRepository.save(userEntity);
        }else{
            if (userEntity.getLockUser()) {
                throw new UserLockException("계정이 잠겼습니다. 관리자에게 문의하세요");
            }
            System.out.println("구글 로그인을 이미 한적이 있습니다. 당신은 자동회원가입이 되어있습니다.");
        }

        return new CustomUserDetails(userEntity, oAuth2User.getAttributes());
        //return oAuth2User;
    }
}
