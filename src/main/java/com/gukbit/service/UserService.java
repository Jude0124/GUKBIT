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
        if (!updateUserData.getChangePassword().equals(updateUserData.getChangePasswordCheck())) {
            bindingResult.addError(new FieldError("updateUserData", "changePassword", "비밀번호가 일치하지 않습니다."));
            bindingResult.addError(new FieldError("updateUserData", "changePasswordCheck", "비밀번호가 일치하지 않습니다."));
            return;
        }

        if(!updateUserData.getChangePassword().isEmpty() && !updateUserData.getChangePasswordCheck().isEmpty()){
            User user = updateUserData.getUser();
            if (updateUserData.getChangePassword() != null)
                user.setPassword(updateUserData.getChangePassword());
            this.updateUser(user);

            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
        }


        if (request.getParameter("courseDropBox") != null) {
            String[] temp = request.getParameter("courseDropBox").split("/");

            String courseId = temp[0];
            Course course = courseRepository.findAllById(courseId).get(0);
            System.out.println("course = " + course);
            String academyCode = course.getAcademycode();
            String courseName = course.getName();

            int session = Integer.parseInt(temp[1]);

            if(updateUserData.getAuthUserData() != null) {
                updateUserData.getAuthUserData().setAcademyCode(academyCode);
                updateUserData.getAuthUserData().setCourseId(courseId);
                updateUserData.getAuthUserData().setCourseName(courseName);
                updateUserData.getAuthUserData().setSession(session);
                //updateUserData.getUser().setAuth(1);
                //userRepository.save(updateUserData.getUser());
                authUserDataRepository.save(updateUserData.getAuthUserData());
            }else{
                //회원 가입할 때 빈 authUserData와 rate를 만들어 놓으면 좋을거 같다
                AuthUserData authUserData = new AuthUserData(updateUserData.getUser().getUserId(),academyCode,courseId,courseName,session);
                //updateUserData.getUser().setAuth(1);
                //userRepository.save(updateUserData.getUser());
                authUserDataRepository.save(authUserData);
                updateUserData.setAuthUserData(authUserData);
            }

            if (updateUserData.getAuthUserData() != null)
            {
                updateUserData.getUser().setAuth(1);
                userRepository.save(updateUserData.getUser());
                //세션이 있으면 세션 반환, 없으면 신규 세션을 생성
                HttpSession Usersession = request.getSession();

                //세션에 로그인 유저 정보 저장
                Usersession.setAttribute(SessionConst.LOGIN_USER, updateUserData.getUser());
            }

            if(updateUserData.getRate() != null){
                rateRepository.deleteByUserId(updateUserData.getRate().getUserId());
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
}
