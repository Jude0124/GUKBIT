package com.gukbit.controller;

import com.gukbit.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/adminMain")
    public String adminMain(Model model) {
        model.addAttribute("userList", adminService.getUserList());
        model.addAttribute("boardList", adminService.getBoardList());
        return "view/admin/admin-main";
    }

    @PostMapping("/userDelete")
    public @ResponseBody Boolean userDelete(@RequestBody JSONObject jsonObject) {
        adminService.deleteUser((String) jsonObject.get("userId"));
        return true;
    }

    @PostMapping("/roleDelete")
    public @ResponseBody Boolean roleDelete(@RequestBody JSONObject jsonObject) {
        adminService.deleteUserRole((String) jsonObject.get("userId"));
        return true;
    }

    @PostMapping("/lockToggle")
    public @ResponseBody Boolean lockToggle(@RequestBody JSONObject jsonObject){
        adminService.lockToggle(jsonObject);
        return true;
    }

    @GetMapping("/userSearch")
    public String userSearch(@RequestParam(value = "searchId")String userId, Model model){
        model.addAttribute("userList", adminService.getSearchUserList(userId));
        return "view/admin/admin-main";
    }

    @GetMapping("/boardSearchByTitle")
    public String boardSearchByTitle(@RequestParam(value = "searchTitle")String searchTitle, Model model){
        model.addAttribute("boardList", adminService.getBoardListByTitle(searchTitle));
        return "view/admin/admin-main";
    }

    @GetMapping("/boardSearchByUserId")
    public String boardSearchByUserId(@RequestParam(value = "searchId")String searchId, Model model){
        model.addAttribute("boardList", adminService.getBoardListByUserId(searchId));
        return "view/admin/admin-main";
    }

    @PostMapping("/boardDelete")
    public @ResponseBody Boolean boardDelete(@RequestBody JSONObject jsonObject){
        adminService.deleteBoard(jsonObject);
        return true;
    }

    @PostMapping("/visibleToggle")
    public @ResponseBody Boolean visibleToggle(@RequestBody JSONObject jsonObject){
        System.out.println("jsonObject = " + jsonObject);
        adminService.visibleToggle(jsonObject);
        return true;
    }
}
