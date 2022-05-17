package com.gukbit.controller;

import com.gukbit.domain.*;
import com.gukbit.dto.AcademyDto;
import com.gukbit.dto.BoardDto;
import com.gukbit.dto.ReplyDto;
import com.gukbit.etc.PopularSearchTerms;
import com.gukbit.etc.Today;
import com.gukbit.security.config.auth.CustomUserDetails;
import com.gukbit.service.*;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/academy")
public class AcademyController {

    private final PopularSearchTerms popularSearchTerms;
    private final AcademyService academyService;
    private final RateService rateService;
    private final CourseService courseService;
    private final BoardService boardService;
    private final ReplyService replyService;
    private final UserService userService;

    @Autowired
    public AcademyController(AcademyService academyService, RateService rateService,
        CourseService courseService, BoardService boardService,
        PopularSearchTerms popularSearchTerms, ReplyService replyService,
        UserService userService) {
        this.popularSearchTerms = popularSearchTerms;
        this.academyService = academyService;
        this.rateService = rateService;
        this.courseService =  courseService;
        this.boardService = boardService;
        this.replyService = replyService;
        this.userService = userService;
    }


    //학원별 게시판
    @GetMapping("/list/{param}")
    public String academyBoardMapping(
            @PathVariable String param,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(value = "academyCode") String academyCode,
            Pageable pageable, Today today, Model model) {

        Page<Board> p ;
        if(param.equals("sortByDate")){     //최신순
            p = boardService.findAcademyBoardList(academyCode,pageable);
        } else if(param.equals("sortByView")){
            p = boardService.findAcademyAlignByView(academyCode, pageable);    // 조회순
        } else{
            p = boardService.findAcademyAlignByRecommend(academyCode, pageable); // 추천순
        }
        model.addAttribute("boardList", p);
        model.addAttribute("Today", today);
        model.addAttribute("academyCode", academyCode);

        try {
            Boolean userRateCheck = boardService.findAuthByUserId(customUserDetails.getUser().getUserId());
            model.addAttribute("userRateCheck", userRateCheck);
        } catch (NullPointerException e){
            model.addAttribute("userRateCheck", false);
        }
        return "view/academy/academy-board";
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
        return "view/academy/academy-write";
    }

    //게시판 수정페이지 이동
    @GetMapping("/rewrite")
    public String communityReWriteMapping(@RequestParam(value = "bid", defaultValue = "0") Integer bid, Model model) {
        System.out.println(boardService.findBoardByIdx(bid));
        model.addAttribute("board", boardService.findBoardByIdx(bid));
        return "view/academy/academy-rewrite";
    }
    //게시판 수정
    @PostMapping("/rewrite")
    public String communityPostReWriteMapping(@ModelAttribute("board") BoardDto boardDto) {
        System.out.println("board = " + boardDto);
        boardService.updateBoard(boardDto);
        return "redirect:/academy/list";
    }


    //게시판 조회
    @GetMapping("/details")
    public String board(@RequestParam(value = "idx", defaultValue = "0") Integer idx, Model model, HttpServletRequest request, HttpServletResponse response) {

        boolean cookieHas = false;

        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                String name = cookie.getName();
                String value = cookie.getValue();
                if("academyView".equals(name) && value.contains("|" + idx + "|")) {
                    cookieHas = true;
                    break;
                }
            }
        }

        if(!cookieHas) {
            Cookie cookie = new Cookie("academyView", "academyView|" + idx + "|");
            cookie.setMaxAge(-1);
            //브라우저 끄면 쿠기 사라지고 조회수 증가 가능
            response.addCookie(cookie);
            boardService.updateView(idx);
        }

        Board board = boardService.findBoardByIdx(idx);

        List<ReplyDto> replyList = replyService.getReplyList(idx);
        int countAllReply = replyService.countAllReply(idx);

        model.addAttribute("idx", idx);
        model.addAttribute("board", board);
        model.addAttribute("replyList", replyList);
        model.addAttribute("countAllReply", countAllReply);

        return "view/academy/academy-pick";
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

        return "view/academy/academy-pick";
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





    //리뷰 탭
    @GetMapping({"/review", "/expected"})
    String academyMapping(@RequestParam("code") String code,
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
                          @Qualifier("reviewed") Pageable pageable1, @Qualifier("expected") Pageable pageable2,
                          Model model, HttpServletRequest request) {


        if(request.getRequestURL().toString().contains("/expected")){
            model.addAttribute("expectedSelect", true);
        }else{
            model.addAttribute("expectedSelect", false);
        }

        /* 평가 리뷰출력 페이지 데이터 */
        List<String> items = new ArrayList<>();
        items.add("강사진");
        items.add("커리큘럼");
        items.add("취업 연계");
        items.add("학원 내 문화");
        items.add("운영 및 시설");
        model.addAttribute("items", items);

        /* 학원 정보 출력 */

        Academy academyInfo = academyService.getAcademyInfo(code);
        model.addAttribute("academyInfo", academyInfo);

        /* 각각의 Course/ reivewed Page 객체 호출*/
        List<Course> courseList =  courseService.getCourseList(code);    
        double[] evalAll = academyService.reviewCourseAverage(courseList);
        int countAll = (int)evalAll[6];
        Page<Rate> page1 = academyService.reviewCoursePageList(courseList,pageable1);
        Page<Course> page2 = academyService.expectedCoursePageList(code, pageable2);

        model.addAttribute("reviewCoursePageList", page1);   
        model.addAttribute("expectedCoursePageList", page2);
        model.addAttribute("evalAll",evalAll);
        model.addAttribute("countAll",countAll);
        model.addAttribute("link1", "academy/review?code=" + code);
        model.addAttribute("link2", "academy/expected?code=" + code);



        //워드클라우드
        try {
            List<Map<String,Integer>> list = academyService.analysis(code);
            model.addAttribute("advantageList", getJsonList(list.get(0), "장점"));
            model.addAttribute("disadvantageList", getJsonList(list.get(1),"단점"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        
        /* 로그인 유저 관련 정보 전달 */
        try {
            String userId = customUserDetails.getUsername();
            AuthUserData authUserData = rateService.getAuthUserData(userId);
            model.addAttribute("authUserData", authUserData);

        } catch (NullPointerException e) {
            model.addAttribute("authUserData", null);
        }
        try {
            Boolean userRateCheck = rateService.findRateByUserId(customUserDetails.getUsername());
            model.addAttribute("userRateCheck", userRateCheck);
        } catch (NullPointerException e) {
            model.addAttribute("userRateCheck", false);

        }
        return "view/academy/academy";
    }
    @PostMapping("/review")
    @ResponseBody
    public Academy academyMapMapping(@RequestParam(value = "code") String code){
        return academyService.getAcademyInfo(code);
    }


    @GetMapping("/search")
    public String searchAcademy(@RequestParam(value = "keyword") String keyword, Model model, HttpServletRequest request, HttpServletResponse response) {
        boolean cookieHas = false;

        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                String name = cookie.getName();
                String value = cookie.getValue();
                try {
                    //인코딩된 쿠키 value를 디코딩
                    value = URLDecoder.decode(value, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if("popularKeyword".equals(name) && keyword.equals(value)) {
                    cookieHas = true;
                    break;
                }
            }
        }

        if(!cookieHas) {
            Cookie cookie = null;
            try {
                //쿠키 value에 공백이나 특스문자가 들어갈 수 없기 때문에 인코딩
                cookie = new Cookie("popularKeyword", URLEncoder.encode(keyword, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            cookie.setMaxAge(-1);
            response.addCookie(cookie);
            popularSearchTerms.insert(keyword);
        }
        List<AcademyDto> academyDtoList = academyService.searchAcademy(keyword);

        model.addAttribute("academyList", academyDtoList);
        model.addAttribute("keyword", keyword);
        return "view/academy/search-academy";
    }

    //wordCloud 초기데이터 저장 불필요할 경우 삭제 요망
    @GetMapping("/wordCloud")
    @ResponseBody
    public List<JSONObject> wordCloud() {
        for (int i = 0; i < 15; i++)
            popularSearchTerms.insert("멀티캠퍼스");
        for (int i = 0; i < 10; i++)
            popularSearchTerms.insert("이젠");
        for (int i = 0; i < 20; i++)
            popularSearchTerms.insert("그린");
        return popularSearchTerms.getJson();
    }



    public List<JSONObject> getJsonList(Map<String,Integer> map, String mainText){
        List<JSONObject> list = new ArrayList<>();
        Integer maxValue = Collections.max(map.values());

        for (String s : map.keySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("text",s);
            jsonObject.put("weight",map.get(s));
            list.add(jsonObject);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("text", mainText);
        jsonObject.put("weight", maxValue + 1);
        list.add(jsonObject);

        return list;
    }

    /* ******************** Academy-compare 영역 ********************  */
    @GetMapping("/compare")
    public String academyCompare(Model model){
        return "view/academy/academy-compare";
    }

    @PostMapping("/compare/search")
    @ResponseBody
    public List<AcademyDto> CompareSearchView(@RequestParam(value = "academyName") String academyName, Model model) {
        List<AcademyDto> academyDtoList = academyService.searchAcademy(academyName);
        return academyDtoList;
    }

    @PostMapping("/compare/data")
    @ResponseBody
    public Map<String, List> rateCompare(@RequestParam("code") String academyCode){
        List<DivisionS> divisionS = courseService.getAllDivisionS();
        List<Rate> rates = rateService.getAllRate(academyCode);
        Academy academy = academyService.getAcademyInfo(academyCode);
        List<Academy> academyTemp = new ArrayList<>();
        academyTemp.add(academy);
        List<Course> courses = courseService.getCourseList(academyCode);

        Map<String, List> data = new HashMap<>();
        data.put("academy", academyTemp);
        data.put("course", courses);
        data.put("rate", rates);
        data.put("divisions", divisionS);
        return data;
    }

}
