package com.gukbit.controller;

import com.gukbit.service.BoardService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/community")
public class CommunityController {
    private final BoardService boardService;

    public CommunityController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/list")
    public String communityMapping(@PageableDefault Pageable pageable, Model model) {
        model.addAttribute("boardList", boardService.findBoardList(pageable));
        return "/view/communityboard";
    }

//    @GetMapping({"", "/"})
//    public String board(@RequestParam(value = "idx", defaultValue = "0") Long idx, Model model) {
//        model.addAttribute("board", boardService.findBoardByIdx(idx));
//        return "/board/form";
//    }
}
