package com.gukbit.api.controller;

import com.gukbit.domain.Board;
import com.gukbit.etc.Today;
import com.gukbit.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@Controller
@RequestMapping("/academy")
public class AcademyBoardController {

    private final BoardService boardService;

    public AcademyBoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("")
    public String academyBoard(@RequestParam(value = "academyCode") String academyCode, Pageable pageable, Today today, Model model) {
        Page<Board> page = boardService.findAcademyBoardList(academyCode, pageable);
        model.addAttribute("boardList", page);
        model.addAttribute("Today", today);
        model.addAttribute("academyCode", academyCode);
        return "view/academyboard";
    }


}
