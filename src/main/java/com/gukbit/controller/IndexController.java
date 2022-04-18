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
    public List<Academy> indexSlideData(@RequestBody String tag, Model model){
        System.out.println("Mapping에서 받은 태그값" + tag);
        List<Course> courses = indexservice.getfield_sCourses(tag);
        Set<String> Code = new HashSet<>();
        for(int i= 0; i<courses.size();i++) {
            System.out.println(courses.get(i).getAcademycode() + " " + courses.size());
            Code.add(courses.get(i).getAcademycode());
        }

        List<Academy> Academy = new ArrayList<>();
        Iterator<String> it = Code.iterator();

        while(it.hasNext()) {

            Academy.add(indexservice.getOneCodeAcademy(it.next()));
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
