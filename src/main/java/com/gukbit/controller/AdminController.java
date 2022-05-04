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

        return "view/admin/admin-main";
    }

    @GetMapping("/userDelete")
    public String userDelete(@RequestParam(value = "userId") String userId) {
        adminService.deleteUser(userId);
        return "redirect:/admin/adminMain";
    }

    @PostMapping("/lockToggle")
    public @ResponseBody Boolean lockToggle(@RequestBody JSONObject jsonObject){
        System.out.println("jsonObject.get('lock') = " + jsonObject.get("lock"));
        return true;
    }
}
