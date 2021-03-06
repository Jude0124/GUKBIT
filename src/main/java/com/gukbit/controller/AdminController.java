package com.gukbit.controller;

import com.gukbit.domain.PreAuthUserData;
import com.gukbit.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/adminMain")
    public String adminMain(Model model) {
        model.addAttribute("userList", adminService.getUserList());
        model.addAttribute("boardList", adminService.getBoardList());
        model.addAttribute("noticeList", adminService.getNoticeList());
        model.addAttribute("preAuthUserDataList", adminService.getPreAuthUserDataList());
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
        model.addAttribute("userList", adminService.getSearchUserList(userId.trim()));
        model.addAttribute("boardList", adminService.getBoardList());
        model.addAttribute("noticeList", adminService.getNoticeList());
        model.addAttribute("preAuthUserDataList", adminService.getPreAuthUserDataList());
        return "view/admin/admin-main";
    }

    @GetMapping("/boardSearchByTitle")
    public String boardSearchByTitle(@RequestParam(value = "searchTitle")String searchTitle, Model model){
        model.addAttribute("boardList", adminService.getBoardListByTitle(searchTitle.trim()));
        model.addAttribute("userList", adminService.getUserList());
        model.addAttribute("noticeList", adminService.getNoticeList());
        model.addAttribute("preAuthUserDataList", adminService.getPreAuthUserDataList());
        return "view/admin/admin-main";
    }

    @GetMapping("/noticeSearchByTitle")
    public String noticeSearchByTitle(@RequestParam(value = "searchTitle")String searchTitle, Model model){
        model.addAttribute("noticeList", adminService.getNoticeListByTitle(searchTitle.trim()));
        model.addAttribute("userList", adminService.getUserList());
        model.addAttribute("boardList", adminService.getBoardList());
        model.addAttribute("preAuthUserDataList", adminService.getPreAuthUserDataList());
        return "view/admin/admin-main";
    }

    @GetMapping("/boardSearchByUserId")
    public String boardSearchByUserId(@RequestParam(value = "searchId")String searchId, Model model){
        model.addAttribute("boardList", adminService.getBoardListByUserId(searchId.trim()));
        model.addAttribute("userList", adminService.getUserList());
        model.addAttribute("noticeList", adminService.getNoticeList());
        model.addAttribute("preAuthUserDataList", adminService.getPreAuthUserDataList());
        return "view/admin/admin-main";
    }

    @PostMapping("/boardDelete")
    public @ResponseBody Boolean boardDelete(@RequestBody JSONObject jsonObject){
        adminService.deleteBoard(jsonObject);
        return true;
    }

    @PostMapping("/denyAuth")
    public @ResponseBody Boolean denyAuth(@RequestBody JSONObject jsonObject){
        Integer authId = (Integer) jsonObject.get("aid");
        adminService.deletePreAuthUserDataAndRole(authId);
        return true;
    }

    @PostMapping("/noticeDelete")
    public @ResponseBody Boolean noticeDelete(@RequestBody JSONObject jsonObject){
        adminService.deleteNotice(jsonObject);
        return true;
    }


    @PostMapping("/visibleToggle")
    public @ResponseBody Boolean visibleToggle(@RequestBody JSONObject jsonObject){
        adminService.visibleToggle(jsonObject);
        return true;
    }

    @GetMapping("/noticeWrite")
    public String noticeWrite(){
        return "view/notice/notice-write";
    }

    @GetMapping("/preAuthUserDataSearchByUserId")
    public String preAuthUserDataSearchByUserId(@RequestParam(value = "searchId")String userId, Model model){
        List<PreAuthUserData> list = adminService.getPreAuthUserDataListByUserId(userId);
        model.addAttribute("preAuthUserDataList", list);
        model.addAttribute("userList", adminService.getUserList());
        model.addAttribute("boardList", adminService.getBoardList());
        model.addAttribute("noticeList", adminService.getNoticeList());
        return "view/admin/admin-main";
    }

    @GetMapping("/auth")
    public String authPopup(@RequestParam(value = "authId")Integer aid, Model model){
        model.addAttribute("PreAuthUserData",adminService.getPreAuthUserData(aid));
        return "view/admin/admin-auth";
    }

    @PostMapping("/auth")
    public @ResponseBody String authPopup(@RequestParam(value = "authId")Integer authId){
        adminService.authPreAuthUserData(authId);
        adminService.deletePreAuthUserData(authId);
        return "<script>"
                +"window.opener.document.location.reload();"
                +"window.close();"
                +"</script>";
    }

    @PostMapping("/validation")
    public @ResponseBody Boolean validation(@RequestBody JSONObject jsonObject){
        return adminService.validation(jsonObject);
    }
}
