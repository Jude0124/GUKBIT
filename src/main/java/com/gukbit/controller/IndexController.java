package com.gukbit.controller;


import com.gukbit.domain.*;
import com.gukbit.repository.UserRepository;
import com.gukbit.service.BoardService;
import com.gukbit.service.indexService;
import com.gukbit.session.SessionConst;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
@RequiredArgsConstructor
public class IndexController {

    @Autowired
    private indexService indexservice;

    @RequestMapping ( value = "/indexCard", method = {RequestMethod.POST})
    @ResponseBody
    public List<Academy> indexSlideData(@RequestParam(value = "Tag") String tag, @RequestParam(value ="Local") String local, Model model) {


        List<Course> courses = indexservice.getfield_sCourses(tag);
        Set<String> Code = new HashSet<>();
        for (int i = 0; i < courses.size(); i++) {
            Code.add(courses.get(i).getAcademycode());
        }

        List<String> Incoding_Code = new ArrayList<>(Code);


        List<Academy> Academy = new ArrayList<>();
        //  Iterator<String> it = Code.iterator();

        for (int j = 0; j < Incoding_Code.size(); j++) {
//            String next;
//            next = it.next();
            String next = Incoding_Code.get(j);
            int localNum = Integer.parseInt(local);
            System.out.println("52번째줄 LOCALNUM " + localNum);

            // 지역을 저장하기 위한 LIST
            List<String> localData = new ArrayList<>();
            if (localNum == 10) {

            } else if (localNum == 11) {
                localData.add("서울");
            } else if (localNum == 41) {
                localData.add("경기");
            } else if (localNum == 28) {
                localData.add("인천");
            } // 42 이상
            else if (localNum == 43) {
                localData.add("충북");
                localData.add("충남");
                localData.add("세종");
                localData.add("대전");
            } else if (localNum == 45) {
                localData.add("전북");
                localData.add("전남");
                localData.add("광주");
            } else if (localNum == 47) {
                localData.add("경북");
                localData.add("경남");
                localData.add("부산");
                localData.add("대구");
            } else if (localNum == 51) {
                localData.add("제주");
                localData.add("강원");
            }




            if (localNum == 10) { //전체출력
                Academy.add(indexservice.getOneCodeAcademy(next));
            } else { // 지역선택시
                for (int i = 0; i < localData.size(); i++) {
                    System.out.println("LocalData :" + localData.get(i));
                    /* if (localNum > 41) // 서울, 경기, 인천제외
                    { */
                        if (indexservice.getOneCodeAcademy(next).getRegion().contains(localData.get(i))) {
                            Academy.add(indexservice.getOneCodeAcademy(next));
                        }
                    /* } else if (localNum > 10 && localNum <= 41) { // 서울, 경기, 인천
                        if (indexservice.getOneCodeAcademy(next).getAddr().contains(localData.get(i))) {
                            Academy.add(indexservice.getOneCodeAcademy(next));
                        } */
                    }
                }
            }


        

        model.addAttribute("cardCourses", Academy);

        return Academy;
    }

        private final UserRepository userRepository;
        private final BoardService boardService;


    @GetMapping("/")
        public String indexMapping(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser, Pageable pageable, Model model) {
            List<Division_S> DivisionSs = indexservice.selectSlideMenu();
            model.addAttribute("sideMenuList", DivisionSs);

            Page<Board> p1 = boardService.findBoardSampleNew(pageable);
            model.addAttribute("boardListNew", p1);


            Page<Board> p2 = boardService.findBoardSampleBest(pageable);
            model.addAttribute("boardListBest", p2);

            if(loginUser == null){
                return "index";
            }

            model.addAttribute("user", loginUser);
            return "index";
        }


        @GetMapping("/signUp")
        public String signUpMapping(Model model) {
            model.addAttribute("user", new User());
            return "/view/signUp";
        }
        @GetMapping("/review-input")
        String reviewInputMapping(){
            return "/view/review-input";
        }

        @GetMapping("/findid")
        String findidMapping() {
            return "/view/findid";
        }

        @GetMapping("/findpw")
        String findpwMapping() {
            return "/view/findpw";
        }

        @GetMapping("/findpw-auth")
        String indpwAuthMapping() {
            return "/view/findpw-auth";
        }

        @GetMapping("/mypage")
        String myPageMapping() {
            return "/view/myPage";
        }


        @GetMapping("/mypage-auth")
        String myPageAuthMapping() {
            return "/view/mypage-auth";
        }

        @GetMapping("/noticeList")
        String noticeList() {
            return "/view/noticeList";
        }

        @GetMapping("/notice")
        String notice() {
            return "/view/notice";
        }
    }

