package com.gukbit.controller;

import com.gukbit.domain.Notice;
import com.gukbit.dto.NoticeDto;
import com.gukbit.etc.Today;
import com.gukbit.repository.NoticeRepository;
import com.gukbit.security.config.auth.CustomUserDetails;
import com.gukbit.service.NoticeService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Controller
@RequestMapping("/notice")
public class NoticeController {
    private final NoticeService noticeService;
    private final NoticeRepository noticeRepository;

    public NoticeController(NoticeService noticeService,
        NoticeRepository noticeRepository) {
        this.noticeService = noticeService;
        this.noticeRepository = noticeRepository;
    }

    //게시판 글작성/저장
    @PostMapping("/create")
    public String boardCreate(@AuthenticationPrincipal CustomUserDetails customUserDetails, NoticeDto noticeDto) {
        noticeDto.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        noticeDto.setAuthor(customUserDetails.getUsername());
        noticeService.noticeCreate(noticeDto);
        return "redirect:/notice/list";
    };


    @GetMapping("/list")
    public String noticeAllBoardMapping(Pageable pageable, Model model, Today today){
        Page<Notice> p = noticeService.findNoticeList(pageable);
        model.addAttribute("noticeList", p);
        model.addAttribute("Today", today);
        return "view/notice/notice-list";
    }

    @GetMapping("/sortByView")
    public String alignByView(Pageable pageable, Model model, Today today) {
        Page<Notice> p = noticeService.alignByView(pageable);
        model.addAttribute("noticeList", p);
        model.addAttribute("Today", today);

        return "view/notice/notice-list-view";
    }

    @GetMapping("/write")
    public String noticeWriteMapping() {
        return "view/notice/notice-write";
    }

    @GetMapping("/delete")
    public String noticeDeleteMapping(@RequestParam(value = "bid", defaultValue = "0") Integer bid) {
        noticeService.deleteNotice(bid);
        return "redirect:/notice/list";
    }

    @GetMapping("/rewrite")
    public String noticeReWriteMapping(@RequestParam(value = "bid", defaultValue = "0") Integer bid,Model model) {
        model.addAttribute("notice", noticeService.findNoticeByIdx(bid));
        return "view/notice/notice-rewrite";
    }

    @PostMapping("/rewrite")
    public String noticePostReWriteMapping(@ModelAttribute("notice") NoticeDto noticeDto) {
        noticeService.updateNotice(noticeDto);
        return "redirect:/notice/list";
    }
    @GetMapping("/details")
    public String notice(@RequestParam(value = "idx", defaultValue = "0") Integer idx,
                         Model model, HttpServletRequest request, HttpServletResponse response) {

        Notice notice = noticeService.findNoticeByIdx(idx);

        model.addAttribute("notice", notice);



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
            response.addCookie(cookie);
            noticeService.updateView(idx);
                }
        return "view/notice/notice-pick";
    }

}
