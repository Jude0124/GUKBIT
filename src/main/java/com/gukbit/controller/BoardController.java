package com.gukbit.controller;


import com.gukbit.domain.Academy;
import com.gukbit.domain.AuthUserData;
import com.gukbit.domain.Board;
import com.gukbit.domain.Course;
import com.gukbit.dto.AcademyDto;
import com.gukbit.dto.BoardDto;
import com.gukbit.dto.ReplyDto;
import com.gukbit.etc.Today;
import com.gukbit.security.config.auth.CustomUserDetails;
import com.gukbit.service.*;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping({"/list/{param}"})
    public String communityAllBoardMapping(@PathVariable String param,
                                           @RequestParam(value = "academyCode",required = false) String academyCode,
                                           @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                           Pageable pageable,Today today, Model model) {
        Page<Board> p;

        if(academyCode==null) { //학원별 코드가 없다면
            if (param.equals("sortByDate")) {     //최신순
                p = boardService.findBoardList(pageable, "date");
            } else if (param.equals("sortByView")) {
                p = boardService.findBoardList(pageable, "view");    // 조회순
            } else {
                p = boardService.findBoardList(pageable, "recommend"); // 추천순
            }
        }else{ // 학원별 코드가 있다면
            if(param.equals("sortByDate")){     //최신순
                p = boardService.findAcademyBoardList(academyCode,pageable,"date");
            } else if(param.equals("sortByView")){
                p = boardService.findAcademyBoardList(academyCode,pageable,"view");    // 조회순
            } else{
                p = boardService.findAcademyBoardList(academyCode,pageable,"recommend"); // 추천순
            }
            model.addAttribute("academyCode", academyCode);
        }
        model.addAttribute("boardList", p);
        model.addAttribute("checkParam",param);
        model.addAttribute("Today", today);
        try {
            Boolean userRateCheck = boardService.findAuthByUserId(customUserDetails.getUser().getUserId());
            model.addAttribute("userRateCheck", userRateCheck);
        } catch (NullPointerException e){
            model.addAttribute("userRateCheck", false);
        }
        return "view/board/board";
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
        model.addAttribute("board", boardService.findBoardByIdx(bid));
        return "view/board/board-rewrite";
    }
    //게시판 수정
    @PostMapping("/rewrite")
    public String communityPostReWriteMapping(@ModelAttribute("board") BoardDto boardDto) {
        boardService.updateBoard(boardDto);
        String redirect = "redirect:/board/list/sortByDate";

        return redirect;
    }

    //게시판 기본 조회, 추천하기
    @GetMapping("/details/{param}")
    public String boardDetails(@RequestParam(value = "idx", defaultValue = "0") Integer idx, @PathVariable String param, Model model, HttpServletRequest request, HttpServletResponse response) {
        boardService.viewRecommendUpdate(idx, request, response, param);

        Board board = boardService.findBoardByIdx(idx);
        List<ReplyDto> replyList = replyService.getReplyList(idx);
        int countAllReply = replyService.countAllReply(idx);

        model.addAttribute("idx", idx);
        model.addAttribute("board", board);
        model.addAttribute("replyList", replyList);
        model.addAttribute("countAllReply", countAllReply);

        return "view/board/board-details";
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
        return "redirect:/board/list/sortByDate?academyCode=" + authUserData.getAcademyCode();
    }

    @PostMapping("/modal")
    @ResponseBody
    public List<AcademyDto> modalReturn(@RequestParam(value = "SearchValue") String searchValue) {
        List<AcademyDto> academyDtoList = academyService.searchAcademy(searchValue);
        return academyDtoList;
    }

}
