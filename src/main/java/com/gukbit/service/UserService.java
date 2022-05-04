package com.gukbit.service;


import com.gukbit.domain.AuthUserData;
import com.gukbit.domain.Course;
import com.gukbit.domain.User;
import com.gukbit.etc.UpdateUserData;
import com.gukbit.repository.AuthUserDataRepository;
import com.gukbit.repository.CourseRepository;
import com.gukbit.repository.RateRepository;
import com.gukbit.repository.UserRepository;
import com.gukbit.session.SessionConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthUserDataRepository authUserDataRepository;
    private final RateRepository rateRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public UserService(UserRepository userRepository, AuthUserDataRepository authUserDataRepository, RateRepository rateRepository, CourseRepository courseRepository) {
        this.userRepository = userRepository;
        this.authUserDataRepository = authUserDataRepository;
        this.rateRepository = rateRepository;
        this.courseRepository = courseRepository;
    }

    public void joinUser(User user) {
        userRepository.save(user);
    }

    public int idCheck(String id) {
        User user = userRepository.findByUserId(id);
        if (user == null) return 0;
        if (user.getUserId().equals(id)) {
            return 1;
        }
        return 0;
    }

    //마이페이지에서 가져온 수정데이터를 가지고 validation 검사 후 유저데이터 조작, 과정정보 수정 및 평점 삭제
    @Transactional
    public void updateCheck(UpdateUserData updateUserData, BindingResult bindingResult, HttpServletRequest request) {
        //비밀번호가 일치하지 않을 때
        if (!updateUserData.getChangePassword().equals(updateUserData.getChangePasswordCheck())) {
            bindingResult.addError(new FieldError("updateUserData", "changePassword", "비밀번호가 일치하지 않습니다."));
            bindingResult.addError(new FieldError("updateUserData", "changePasswordCheck", "비밀번호가 일치하지 않습니다."));
            return;
        }

        //비밀번호가 비어있지 않을 때 (성공 케이스)
        //비어 있는 경우는 프론트에서 처리
        if(!updateUserData.getChangePassword().isEmpty() && !updateUserData.getChangePasswordCheck().isEmpty()){
            User user = updateUserData.getUser();
            if (updateUserData.getChangePassword() != null)
                user.setPassword(updateUserData.getChangePassword());
            this.updateUser(user);

            updateSession(request, user);
        }


        //만약 드랍박스가 선택 되었다면
        if (request.getParameter("courseDropBox") != null) {
            String[] temp = request.getParameter("courseDropBox").split("/");

            String courseId = temp[0];
            Course course = courseRepository.findAllById(courseId).get(0);
            System.out.println("course = " + course);
            String academyCode = course.getAcademyCode();
            String courseName = course.getName();

            int session = Integer.parseInt(temp[1]);

            //원래 인증이 된 사용자의 경우
            if(updateUserData.getAuthUserData() != null) {
                updateUserData.getAuthUserData().setAcademyCode(academyCode);
                updateUserData.getAuthUserData().setCourseId(courseId);
                updateUserData.getAuthUserData().setCourseName(courseName);
                updateUserData.getAuthUserData().setSession(session);
                updateUserData.getUser().setAuth(1);
                userRepository.save(updateUserData.getUser());
                authUserDataRepository.save(updateUserData.getAuthUserData());
                updateSession(request, updateUserData.getUser());
                if(updateUserData.getRate() != null){
                    rateRepository.deleteByUserId(updateUserData.getRate().getUserId());
                }
            }else{
                //회원 가입할 때 빈 authUserData와 rate를 만들어 놓으면 좋을거 같다
                AuthUserData authUserData = new AuthUserData(updateUserData.getUser().getUserId(),academyCode,courseId,courseName,session);
                updateUserData.getUser().setAuth(1);
                userRepository.save(updateUserData.getUser());
                authUserDataRepository.save(authUserData);
                updateUserData.setAuthUserData(authUserData);
                updateSession(request, updateUserData.getUser());
            }
        }
    }

    //유저의 값이 존재하면 수정 없으면 저장
    public void updateUser(User user) {
        if(userRepository.findByUserId(user.getUserId())!=null)
            userRepository.save(user);
    }

    //해당 유저 정보 삭제
    public void deleteUser(User user) {
        if(authUserDataRepository.findByUserId(user.getUserId())!= null)
            authUserDataRepository.delete(authUserDataRepository.findByUserId(user.getUserId()));
        if(rateRepository.findByUserId(user.getUserId())!=null)
            rateRepository.delete(rateRepository.findByUserId(user.getUserId()));
        userRepository.delete(user);
    }

    //사용자의 인증정보와 평점 작성 정보를 가져오는 함수
    public void makeUpdateUser(UpdateUserData updateUserData) {
        updateUserData.setAuthUserData(authUserDataRepository.findByUserId(updateUserData.getUser().getUserId()));
        updateUserData.setRate(rateRepository.findByUserId(updateUserData.getUser().getUserId()));
    }

    public AuthUserData getAuthUserData(String userId){
        return authUserDataRepository.findByUserId(userId);
    }

    public void updateSession(HttpServletRequest request, User user){
        HttpSession userSession = request.getSession();

        //세션에 로그인 유저 정보 저장
        userSession.setAttribute(SessionConst.LOGIN_USER, user);
    }

    // 전화번호를 통해 유저 정보 찾기
    public String findIdByTel(String tel) {
        User user = userRepository.findByTel(tel);
        String message;
        if (user == null) {
            message = "회원 정보를 찾을 수 없습니다.";
        } else {
            message = "회원님의 ID는 [" + user.getUserId() + "] 입니다";
        }
        return message;
    }

    // 메일 주소를 통해 유저 정보 찾기
    public String findIdByEmail(String email) {
        User user = userRepository.findByEmail(email);
        String message;
        if (user == null) {
            message = "회원 정보를 찾을 수 없습니다.";
        } else {
            message = "회원님의 ID는 [" + user.getUserId() + "] 입니다";
        }
        return message;
    }


}
