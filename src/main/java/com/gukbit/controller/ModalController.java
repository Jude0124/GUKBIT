package com.gukbit.controller;

import com.gukbit.domain.Board;
import com.gukbit.dto.AcademyDto;
import com.gukbit.service.AcademyService;
import com.gukbit.service.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/community")
public class ModalController {

    private final AcademyService academyService;
    private final BoardService boardService;

    public ModalController(AcademyService academyService, BoardService boardService) {
        this.academyService = academyService;
        this.boardService = boardService;
    }
    @PostMapping("/modal")
    public List<AcademyDto> modalReturn(@RequestParam(value = "SearchValue") String searchValue) {
        List<AcademyDto> academyDtoList = academyService.searchAcademy(searchValue);
        return academyDtoList;
    }

//    @GetMapping("/academy")
//    public String academyBoard(@RequestParam String academyName, Pageable pageable, Model model) {
//        Page<Board> page = boardService.findAcademyBoardList(academyName, pageable);
//        model.addAttribute("boardList", page);
//        return "view/academyboard";
//    }
}
