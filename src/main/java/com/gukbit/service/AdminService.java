package com.gukbit.service;

import com.gukbit.domain.Board;
import com.gukbit.domain.Notice;
import com.gukbit.domain.User;
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

    public List<User> getUserList() {
        return userService.getUserList();
    }

    public List<Board> getBoardList() {
        return boardService.getBoardList();
    }
    public List<Notice> getNoticeList() {return noticeService.getNoticeList();}

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

}
