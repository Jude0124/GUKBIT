package com.gukbit.controller;

import com.gukbit.domain.Notice;
import com.gukbit.dto.NoticeDto;
import com.gukbit.etc.Today;
import com.gukbit.service.NoticeService;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/notice")
public class NoticeController {
    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    //게시판 글작성/저장
    @PostMapping("/create")
    public String boardCreate(NoticeDto noticeDto) {
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


    @GetMapping("/write")
    public String noticeWriteMapping() {
        return "view/notice/notice-write";
    }

    @PostMapping("/delete")
    public String noticeDeleteMapping(@RequestParam(value = "bid", defaultValue = "0") Integer bid) {
        noticeService.deleteNotice(bid);
        return "redirect:/admin/adminMain";
    }

    @GetMapping("/rewrite")
    public String noticeReWriteMapping(@RequestParam(value = "bid", defaultValue = "0") Integer bid,Model model) {
        model.addAttribute("notice", noticeService.findNoticeByIdx(bid));
        return "view/notice/notice-rewrite";
    }

    @PostMapping("/rewrite")
    public String noticePostReWriteMapping(@ModelAttribute("notice") NoticeDto noticeDto) {
        noticeService.updateNotice(noticeDto);
        return "redirect:/admin/adminMain";
    }
    @GetMapping("/details")
    public String notice(@RequestParam(value = "idx", defaultValue = "0") Integer idx,
                         Model model, HttpServletRequest request, HttpServletResponse response) {
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
        Notice notice = noticeService.findNoticeByIdx(idx);
        model.addAttribute("notice", notice);

        return "view/notice/notice-details";
    }
}
