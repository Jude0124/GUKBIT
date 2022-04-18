package com.gukbit.controller;

import com.gukbit.dto.domain.Board;
import com.gukbit.dto.domain.User;
import com.gukbit.service.BoardService;
import com.gukbit.session.SessionConst;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/community")
public class CommunityController {
    private final BoardService boardService;

    public CommunityController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/list")
    public String communityAllBoardMapping(Pageable pageable, Model model) {
        Page<Board> p = boardService.findBoardList(pageable);
        model.addAttribute("boardList", p);
        return "view/communityboard";
    }




    @GetMapping("/write")
    public String communityWriteMapping() {
        return "view/communityboard-write";
    }

    @GetMapping("/rewrite")
    public String communityReWriteMapping(@RequestParam(value = "bid", defaultValue = "0") Long bid,Model model) {
        model.addAttribute("board", boardService.findBoardByIdx(bid));
        return "view/communityboard-rewrite";
    }

    @GetMapping("/details")
    public String board(@RequestParam(value = "idx", defaultValue = "0") Long idx,
                        @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser, Model model) {
        boolean check = boardService.writeUserCheck(loginUser, idx);
        model.addAttribute("board", boardService.findBoardByIdx(idx));
        model.addAttribute("check", check);
        return "view/communityboardPick";
    }
}
