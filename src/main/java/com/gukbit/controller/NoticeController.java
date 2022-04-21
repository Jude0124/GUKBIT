package com.gukbit.controller;

import com.gukbit.domain.Notice;
import com.gukbit.domain.User;
import com.gukbit.repository.NoticeRepository;
import com.gukbit.service.NoticeService;
import com.gukbit.session.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;


@Slf4j
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

    //게시판 저장
    @ResponseBody
    @PostMapping("/create")
    public Notice board_Create(@RequestBody Notice notice){
        log.info("params={}", notice);

        noticeService.notice_Create(notice);
        return notice;
    };

    @GetMapping("/list")
    public String noticeAllBoardMapping(Pageable pageable, Model model) {
        Page<Notice> p = noticeService.findNoticeList(pageable);
        model.addAttribute("noticeList", p);
        return "view/noticeList";
    }

    @GetMapping("/write")
    public String noticeWriteMapping() {
        return "view/notice-write";
    }

    @GetMapping("/delete")
    public String noticeDeleteMapping(@RequestParam(value = "bid", defaultValue = "0") Integer bid) {
        noticeService.deleteNotice(bid);
        return "redirect:/notice/list";
    }

    @GetMapping("/rewrite")
    public String noticeReWriteMapping(@RequestParam(value = "bid", defaultValue = "0") Integer bid,Model model) {
        model.addAttribute("notice", noticeService.findNoticeByIdx(bid));
        return "view/notice-rewrite";
    }

    @PostMapping("/rewrite")
    public String noticePostReWriteMapping(@ModelAttribute("notice") Notice notice, BindingResult bindingResult) {
        noticeService.updateNotice(notice);
        return "redirect:/notice/list";
    }
    @GetMapping("/details")
    public String notice(@RequestParam(value = "idx", defaultValue = "0") Integer idx,
                        @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser, Model model) {
        boolean check = noticeService.writeUserCheck(loginUser, idx);
        Notice notice = noticeService.findNoticeByIdx(idx);
        noticeService.updateView(idx);
        model.addAttribute("notice", notice);
        model.addAttribute("check", check);
        return "view/noticePick";
    }
}
