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

    public void deletePreAuthUserData(Integer authId){preAuthUserDataService.deletePreAuthUserData(authId);}

    public void test(Integer authId){
        PreAuthUserData preAuthUserData = preAuthUserDataService.getPreAuthUserData(authId);
        User user = userService.getUserByUserId(preAuthUserData.getUserId());
        user.setAuth(1);
        user.setRole("USER_AUTH");

        AuthUserData authUserData;
        authUserData = new AuthUserData().builder()
            .userId(preAuthUserData.getUserId())
            .academyCode(preAuthUserData.getAcademyCode())
            .courseId(preAuthUserData.getCourseId())
            .courseName(preAuthUserData.getCourseName())
            .session(preAuthUserData.getSession()).
            build();

        System.out.println("user = " + user);
        System.out.println("authUserData = " + authUserData);
        preAuthUserDataService.deletePreAuthUserData(preAuthUserData.getAid());
        authUserDataService.updateAuthUserData(authUserData);
        userService.updateUser(user);
    }
}
