package com.gukbit.service;


import com.gukbit.domain.User;
import com.gukbit.etc.UpdateUserData;
import com.gukbit.repository.AuthUserDataRepository;
import com.gukbit.repository.CourseRepository;
import com.gukbit.repository.RateRepository;
import com.gukbit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;

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
            userRepository.save(user);
        }


        if (request.getParameter("courseDropBox") != null) {
            String[] temp = request.getParameter("courseDropBox").split("/");

            String courseId = temp[0];
            String academyCode = courseRepository.findAllById(courseId).get(0).getAcademycode();
            int session = Integer.parseInt(temp[1]);

            updateUserData.getAuthUserData().setAcademyCode(academyCode);
            updateUserData.getAuthUserData().setCourseId(courseId);
            updateUserData.getAuthUserData().setSession(session);
            System.out.println("UserService.updateCheck");
            System.out.println("updateUserData.getAuthUserData() = " + updateUserData.getAuthUserData());
            System.out.println("updateUserData.getRate() = " + updateUserData.getRate());

            authUserDataRepository.save(updateUserData.getAuthUserData());

            if(updateUserData.getRate() != null){
                rateRepository.deleteByUserId(updateUserData.getRate().getUserId());
            }
        }

    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public void makeUpdateUser(UpdateUserData updateUserData) {
        updateUserData.setAuthUserData(authUserDataRepository.findByUserId(updateUserData.getUser().getUserId()));
        updateUserData.setRate(rateRepository.findByUserId(updateUserData.getUser().getUserId()));
    }
}
