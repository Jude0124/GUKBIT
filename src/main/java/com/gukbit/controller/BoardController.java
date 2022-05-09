package com.gukbit.controller;


import com.gukbit.domain.*;
import com.gukbit.dto.BoardDto;
import com.gukbit.dto.ReplyDto;
import com.gukbit.etc.Today;
import com.gukbit.security.config.auth.CustomUserDetails;
import com.gukbit.service.*;
import com.gukbit.session.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


@Slf4j
@Controller
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    private final ReplyService replyService;
    private final AcademyService academyService;
    private final CourseService courseService;
    private final RateService rateService;
    private final UserService userService;

    public BoardController(BoardService boardService,ReplyService replyService, UserService userService, AcademyService academyService, CourseService courseService, RateService rateService) {
        this.boardService = boardService;
        this.replyService = replyService;
        this.academyService = academyService;
        this.courseService = courseService;
        this.rateService = rateService;
        this.userService = userService;
    }

    @GetMapping("/list")
    public String communityAllBoardMapping(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        Pageable pageable,Today today, Model model) {
        Page<Board> p = boardService.findBoardList(pageable);
        model.addAttribute("boardList", p);
        model.addAttribute("Today", today);
        try {
            Boolean userRateCheck = boardService.findAuthByUserId(customUserDetails.getUser().getUserId());
            model.addAttribute("userRateCheck", userRateCheck);
        } catch (NullPointerException e){
            model.addAttribute("userRateCheck", false);
        }

        return "view/board/board";
    }

    // 조회순으로 정렬
    @GetMapping("/sortByView")
    public String alignByView(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                              Pageable pageable, Model model,Today today) {
        Page<Board> p = boardService.alignByView(pageable);
        model.addAttribute("boardList", p);
        model.addAttribute("Today",today);
        try {
            Boolean userRateCheck = boardService.findAuthByUserId(loginUser.getUserId());
            model.addAttribute("userRateCheck", userRateCheck);
        } catch (NullPointerException e){
            model.addAttribute("userRateCheck", false);
        }
        return "view/board/board-view";
    }

    @GetMapping("/sortByRecommend")
    public String alignByRecommend(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
        Pageable pageable, Model model,Today today) {
        Page<Board> p = boardService.alignByRecommend(pageable);
        model.addAttribute("boardList", p);
        model.addAttribute("Today",today);
        try {
            Boolean userRateCheck = boardService.findAuthByUserId(loginUser.getUserId());
            model.addAttribute("userRateCheck", userRateCheck);
        } catch (NullPointerException e){
            model.addAttribute("userRateCheck", false);
        }
        return "view/board/board-recommend";
    }


    //게시판 작성페이지 이동
    @GetMapping("/write")
    public String communityWriteMapping(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
        Model model) {
        /* 로그인 유저 관련 정보 전달 */
        try {
            String userId = customUserDetails.getUser().getUserId();
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
        return "view/board/board-write";
    }

    //게시판 작성
    @ResponseBody
    @PostMapping("/create")
    public BoardDto boardCreate(@RequestBody BoardDto boardDto) {
        log.info("params={}", boardDto);
        boardService.boardCreate(boardDto);
        return boardDto;
    }
    //게시판 삭제
    @PostMapping("/delete")
    public @ResponseBody Boolean communityDeleteMapping(@RequestBody JSONObject jsonObject) {
        Integer bid = (Integer)jsonObject.get("bid");
        boardService.deleteBoard(bid);
        return true;
    }
    //게시판 수정페이지 이동
    @GetMapping("/rewrite")
    public String communityReWriteMapping(@RequestParam(value = "bid", defaultValue = "0") Integer bid, Model model) {
        System.out.println(boardService.findBoardByIdx(bid));
        model.addAttribute("board", boardService.findBoardByIdx(bid));
        return "view/board/board-rewrite";
    }
    //게시판 수정
    @PostMapping("/rewrite")
    public String communityPostReWriteMapping(@ModelAttribute("board") BoardDto boardDto, BindingResult bindingResult) {
        System.out.println("board = " + boardDto);
        boardService.updateBoard(boardDto);
        return "redirect:/board/list";
    }

    //게시판 조회
    @GetMapping("/details")
    public String board(@RequestParam(value = "idx", defaultValue = "0") Integer idx, @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model, HttpServletRequest request, HttpServletResponse response) {

        boolean cookieHas = false;

        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                String name = cookie.getName();
                String value = cookie.getValue();
                if("boardView".equals(name) && value.contains("|" + idx + "|")) {
                    cookieHas = true;
                    break;
                }
            }
        }

        if(!cookieHas) {
            Cookie cookie = new Cookie("boardView", "boardView|" + idx + "|");
            cookie.setMaxAge(-1);
            //브라우저 끄면 쿠기 사라지고 조회수 증가 가능
            response.addCookie(cookie);
            boardService.updateView(idx);
        }

        boolean check = boardService.writeUserCheck(customUserDetails.getUser(), idx);
        Board board = boardService.findBoardByIdx(idx);

        List<ReplyDto> replyList = replyService.getReplyList(idx);
        int countAllReply = replyService.countAllReply(idx);

        model.addAttribute("idx", idx);
        model.addAttribute("board", board);
        model.addAttribute("check", check);
        model.addAttribute("replyList", replyList);
        model.addAttribute("countAllReply", countAllReply);

        return "view/board/board-pick";
    }


    //게시판 추천하기
    @GetMapping("/recommend")
    public String recommend(@RequestParam(value = "idx", defaultValue = "0") Integer idx, @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model, HttpServletRequest request, HttpServletResponse response, Thread thread)
        throws InterruptedException {

        boolean cookieHas = false;

        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                String name = cookie.getName();
                String value = cookie.getValue();
                if("boardRecommend".equals(name) && value.contains("|" + idx + "|")) {
                    cookieHas = true;
                    break;
                }
            }
        }

        if(!cookieHas) {
            Cookie cookie = new Cookie("boardRecommend", "boardRecommend|" + idx + "|");
            cookie.setMaxAge(60 * 60 * 24 * 365);
            //브라우저 꺼도 쿠키 안사라지고 1년동안 보관.
            // 쿠키를 지우지 않고선 1년 동안 추천수 조작 불가
            response.addCookie(cookie);
            boardService.updateRecommend(idx);
        }

        boolean check = boardService.writeUserCheck(customUserDetails.getUser(), idx);
        Board board = boardService.findBoardByIdx(idx);

        List<ReplyDto> replyList = replyService.getReplyList(idx);
        int countAllReply = replyService.countAllReply(idx);

        model.addAttribute("idx", idx);
        model.addAttribute("board", board);
        model.addAttribute("check", check);
        model.addAttribute("replyList", replyList);
        model.addAttribute("countAllReply", countAllReply);

        return "view/board/board-pick";
    }

    @PostMapping("/reply")
    @ResponseBody
    public String reply(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody Map<String, String> map) {
        if(map.get("text").equals("")){
            return "fail";
        }
        replyService.saveReply(map, customUserDetails);
        return "success";
    }

    @GetMapping("/mycom")
    public String myCom(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (customUserDetails == null) {
            return "redirect:/";
        }
        AuthUserData authUserData = userService.getAuthUserData(customUserDetails.getUser().getUserId());
        if (authUserData == null) {
            return "redirect:/";
        }
        return "redirect:/academy?academyCode=" + authUserData.getAcademyCode();
    }
}
