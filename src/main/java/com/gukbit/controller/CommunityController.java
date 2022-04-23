package com.gukbit.controller;



import com.gukbit.domain.AuthUserData;
import com.gukbit.domain.Board;
import com.gukbit.domain.User;
import com.gukbit.etc.ReplyDto;

import com.gukbit.domain.Academy;
import com.gukbit.domain.AuthUserData;
import com.gukbit.domain.Board;
import com.gukbit.domain.Course;
import com.gukbit.domain.Reply;
import com.gukbit.domain.User;
import com.gukbit.etc.ReplyDto;
import com.gukbit.service.AcademyService;
import com.gukbit.service.BoardService;
import com.gukbit.repository.BoardRepository;
import com.gukbit.service.CourseService;
import com.gukbit.service.RateService;
import com.gukbit.service.ReplyService;
import com.gukbit.etc.Today;
import com.gukbit.service.BoardService;
import com.gukbit.service.ReplyService;
import com.gukbit.service.UserService;
import com.gukbit.session.SessionConst;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Slf4j
@Controller
@RequestMapping("/community")
public class CommunityController {
    private final BoardService boardService;
    private final ReplyService replyService;
    private final AcademyService academyService;
    private final CourseService courseService;
    private final RateService rateService;
    private final UserService userService;

    public CommunityController(BoardService boardService,ReplyService replyService, UserService userService, AcademyService academyService, CourseService courseService, RateService rateService) {
        this.boardService = boardService;
        this.replyService = replyService;
        this.academyService = academyService;
        this.courseService = courseService;
        this.rateService = rateService;
        this.userService = userService;
    }

    @GetMapping("/list")
    public String communityAllBoardMapping(
        @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
        Pageable pageable,Today today, Model model) {
        Page<Board> p = boardService.findBoardList(pageable);
        model.addAttribute("boardList", p);
        model.addAttribute("Today", today);
        try {
            Boolean userRateCheck = boardService.findAuthByUserId(loginUser.getUserId());
            model.addAttribute("userRateCheck", userRateCheck);
        } catch (NullPointerException e){
            model.addAttribute("userRateCheck", false);
        }


        return "view/communityboard";
    }


    @GetMapping("/SortByView")
    public String alignByView(Pageable pageable, Model model) {
        Page<Board> p = boardService.alignByView(pageable);
        model.addAttribute("boardList", p);
        return "view/communityboard";
    }


    @GetMapping("/write")
    public String communityWriteMapping(
        @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
        Model model) {
        /* 로그인 유저 관련 정보 전달 */
        try {
            String userId = loginUser.getUserId();
            AuthUserData authUserData = rateService.getAuthUserData(userId);
            model.addAttribute("authUserData", authUserData);
            List<Course> courseData = courseService.getCourseData(authUserData.getCourseId());
            model.addAttribute("courseData", courseData);
            Academy academyInfo = academyService.getAcademyInfo(authUserData.getAcademyCode());
            model.addAttribute("academyInfo", academyInfo);


        } catch(NullPointerException e) {
            model.addAttribute("authUserData", null);
            model.addAttribute("courseData", null);
            model.addAttribute("academyInfo", null);
        }
        /* 전체 학원 정보 조회 */
        List<Academy> academyList = academyService.searchAllAcademy();
        model.addAttribute("academyList", academyList);
        return "view/communityboard-write";
    }

    @GetMapping("/delete")
    public String communityDeleteMapping(@RequestParam(value = "bid", defaultValue = "0") Integer bid) {
        boardService.deleteBoard(bid);
        return "redirect:/community/list";
    }

    @GetMapping("/rewrite")
    public String communityReWriteMapping(@RequestParam(value = "bid", defaultValue = "0") Integer bid, Model model) {
        System.out.println("CommunityController.communityReWriteMapping");
        System.out.println(boardService.findBoardByIdx(bid));
        model.addAttribute("board", boardService.findBoardByIdx(bid));
        return "view/communityboard-rewrite";
    }

    @PostMapping("/rewrite")
    public String communityPostReWriteMapping(@ModelAttribute("board") Board board, BindingResult bindingResult) {
        System.out.println("board = " + board);
        boardService.updateBoard(board);
        return "redirect:/community/list";
    }

    //게시판 저장
    @ResponseBody
    @PostMapping("/board/create")
    public Board board_Create(@RequestBody Board board) {
        log.info("params={}", board);

        boardService.board_Create(board);
        return board;
    }

    ;

    @GetMapping("/details")
    public String board(@RequestParam(value = "idx", defaultValue = "0") Integer idx, @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser, Model model) {
        boolean check = boardService.writeUserCheck(loginUser, idx);
        Board board = boardService.findBoardByIdx(idx);


        List<ReplyDto> replyList = replyService.getReplyList(idx);


        boardService.updateView(idx);
        model.addAttribute("idx", idx);
        model.addAttribute("board", board);
        model.addAttribute("check", check);
        model.addAttribute("replyList", replyList);
        return "view/communityboardPick";
    }

    @PostMapping("/reply")
    @ResponseBody
    public String reply(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser, @RequestBody Map<String, String> map) {
        replyService.saveReply(map, loginUser);
        return "success";
    }

    @GetMapping("/mycom")
    public String myCom(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser) {
        if (loginUser == null) {
            return "redirect:/";
        }
        AuthUserData authUserData = userService.getAuthUserData(loginUser.getUserId());
        if (authUserData == null) {
            return "redirect:/";
        }
        return "redirect:/academy?academyCode=" + authUserData.getAcademyCode();
        //academy?academyCode=500020039927
    }
}
