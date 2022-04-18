package com.gukbit.controller;

import com.gukbit.domain.Academy;
import com.gukbit.domain.Course;
import com.gukbit.domain.Division_S;
import com.gukbit.service.indexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
public class IndexController {

    @Autowired
    private indexService indexservice;
    /*
    @GetMapping("/")
    public String indexMapping(){
    return "/index";
    }
    */
    @GetMapping("/")
    public String indexSlideMapping(Model model){

        List<Division_S> DividsionSs = indexservice.selectSlideMenu();
        model.addAttribute("sideMenuList", DividsionSs);

        return "/index";
    }

    @RequestMapping ( value = "/indexCard", method = {RequestMethod.POST})
    @ResponseBody
    public List<Academy> indexSlideData(@RequestParam(value = "Tag") String tag, @RequestParam(value ="Local") String local, Model model){


        List<Course> courses = indexservice.getfield_sCourses(tag);
        Set<String> Code = new HashSet<>();
        for(int i= 0; i<courses.size();i++) {
            Code.add(courses.get(i).getAcademycode());
        }

        List<String> Incoding_Code = new ArrayList<>(Code);

        List<Academy> Academy = new ArrayList<>();
       //  Iterator<String> it = Code.iterator();

       for(int j=0; j<Incoding_Code.size(); j++)
       {
//            String next;
//            next = it.next();
            String next = Incoding_Code.get(j);
            int localNum = Integer.parseInt(local);
            System.out.println("52번째줄 LOCALNUM" + localNum);

            // 지역을 저장하기 위한 LIST
            List<String> localData= new ArrayList<>();
            if(localNum==10)
            {

            }
            else if (localNum==11)
            {
                localData.add("서울");
            }
            else if (localNum==41)
            {
                localData.add("경기");
            }
            else if (localNum==28)
            {
                localData.add("인천");
            } // 42 이상
            else if (localNum==43)
            {
                localData.add("충북");
                localData.add("충남");
                localData.add("세종");
                localData.add("대전");
            }
            else if (localNum==45)
            {
                localData.add("전북");
                localData.add("전남");
                localData.add("광주");
            }
            else if (localNum==47)
            {
                localData.add("경북");
                localData.add("경남");
                localData.add("부산");
                localData.add("대구");
            }

            else if (localNum==51)
            {
                localData.add("제주");
                localData.add("강원");
            }


            if(localNum == 10) { //전체출력
                Academy.add(indexservice.getOneCodeAcademy(next));
            } else { // 지역선택시
                for (int i = 0; i < localData.size(); i++) {
                    System.out.println(localData.get(i));
                    if (localNum > 41) // 서울, 경기, 인천제외
                    {
                        if (indexservice.getOneCodeAcademy(next).getAddr().contains(localData.get(i)))
                        {
                            Academy.add(indexservice.getOneCodeAcademy(next));
                        }
                    } else if (localNum > 10 && localNum <= 41) { // 서울, 경기, 인천
                        if (indexservice.getOneCodeAcademy(next).getAddr().contains(localData.get(i)))
                        {
                            Academy.add(indexservice.getOneCodeAcademy(next));
                        }
                    }
                }
            }

        }


        model.addAttribute("cardCourses", Academy);

        return Academy;
    }


    @GetMapping("/signUp")
    public String signUpMapping() {
        return "/view/signUp";
    }

    @GetMapping("/login")
    public String loginMapping() {
        return "/view/login";
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

    @GetMapping("/review-input")
    String reviewInputMapping(){
        return "/view/review-input";
    }
    
    @GetMapping("/academy")
    String academyMapping(Model model) {
        List<String> items = new ArrayList<>();
        items.add("강사진");
        items.add("커리큘럼");
        items.add("취업 연계");
        items.add("학원 내 문화");
        items.add("운영 및 시설");
        model.addAttribute("items", items);
        return "/view/academy";
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

    @GetMapping("/searchAcademy")
    String searchAcademy(){
        return "/view/searchAcademy";
    }

}
