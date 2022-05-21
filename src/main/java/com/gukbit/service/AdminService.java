package com.gukbit.service;

import com.gukbit.domain.*;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserService userService;
    private final BoardService boardService;
    private final NoticeService noticeService;
    private final AcademyService academyService;
    private final CourseService courseService;
    private final PreAuthUserDataService preAuthUserDataService;
    private final AuthUserDataService authUserDataService;

    public List<User> getUserList() {
        return userService.getUserList();
    }

    public List<Board> getBoardList() {
        return boardService.getBoardList();
    }

    public List<Notice> getNoticeList() {return noticeService.getNoticeList();}

    public List<PreAuthUserData> getPreAuthUserDataList(){return userService.getPreAuthUserDataList();}

    public List<User> getSearchUserList(String userId) {
        return userService.getSearchUserList(userId);
    }

    public void deleteUser(String userId) {
        userService.deleteUser(userService.getUserByUserId(userId));
    }

    public void deleteUserRole(String userId) {
        userService.deleteUserRole(userId);
    }

    public void lockToggle(JSONObject jsonObject) {
        userService.lockToggle(jsonObject);
    }

    public List<Board> getBoardListByUserId(String userId) {
        return boardService.getBoardListByUserId(userId);
    }

    public List<Board> getBoardListByTitle(String title) {
        return boardService.getBoardListByTitle(title);
    }

    public void deleteBoard(JSONObject jsonObject) {
        boardService.deleteBoard((Integer) jsonObject.get("bid"));
    }

    public void deleteNotice(JSONObject jsonObject) {
        noticeService.deleteNotice((Integer) jsonObject.get("bid"));
    }


    public void visibleToggle(JSONObject jsonObject) {
        Board board = boardService.findBoardByIdx((Integer) jsonObject.get("bid"));
        Boolean visible = (Boolean) jsonObject.get("visible");
        if (board != null) {
            board.setVisible(!visible);
            boardService.saveBoard(board);
        }
    }

    public List<Notice> getNoticeListByTitle(String searchTitle) {
        return noticeService.getNoticeListByTitle(searchTitle);
    }

    public PreAuthUserData getPreAuthUserData(Integer aid){
        return userService.getPreAuthUserData(aid);
    }

    public void deletePreAuthUserData(Integer authId){
        preAuthUserDataService.deletePreAuthUserData(authId);
    }

    public void deletePreAuthUserDataAndRole(Integer authId){
        PreAuthUserData preAuthUserData = preAuthUserDataService.getPreAuthUserData(authId);
        preAuthUserDataService.deletePreAuthUserData(authId);
        userService.modifyRole(preAuthUserData.getUserId(), "ROLE_USER");
    }

    public void authPreAuthUserData(Integer authId){
        PreAuthUserData preAuthUserData = preAuthUserDataService.getPreAuthUserData(authId);
        User user = userService.getUserByUserId(preAuthUserData.getUserId());
        user.setRole("ROLE_AUTH");

        AuthUserData authUserData;
        authUserData = new AuthUserData().builder()
            .userId(preAuthUserData.getUserId())
            .academyCode(preAuthUserData.getAcademyCode())
            .courseId(preAuthUserData.getCourseId())
            .courseName(preAuthUserData.getCourseName())
            .session(preAuthUserData.getSession()).
            build();

        authUserDataService.updateAuthUserData(authUserData);
        userService.updateUser(user);
    }

    public List<PreAuthUserData> getPreAuthUserDataListByUserId(String userId){
        return preAuthUserDataService.getPreAuthUserDataListByUserId(userId);
    }

    public boolean validation(JSONObject jsonObject){
        int authId = (int) jsonObject.get("aid");
        PreAuthUserData preAuthUserData = preAuthUserDataService.getPreAuthUserData(authId);
        System.out.println("preAuthUserData = " + preAuthUserData);
        List<Course> list = courseService.getCourseData(preAuthUserData.getCourseId());
        for (Course course : list) {
            System.out.println("course = " + course);
        }
        //해당하는 과정이 존재한다면
        if(list != null){
            System.out.println("not null");
            for (Course course : list) {
                //과정의 학원명과 회차가 일치한다면
                if(course.getAcademyCode().equals(preAuthUserData.getAcademyCode()) && course.getSession().equals(preAuthUserData.getSession()))
                    return true;
            }
        }
        return false;
    }
}
