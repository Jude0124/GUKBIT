package com.gukbit.controller;
import com.gukbit.domain.Board;
import com.gukbit.repository.BoardRepository;
import com.gukbit.service.BoardService;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import com.gukbit.domain.User;
import com.gukbit.service.BoardService;
import com.gukbit.session.SessionConst;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@Slf4j
@Controller
@RequestMapping("/community")
public class CommunityController {
    private final BoardService boardService;
    private final BoardRepository boardRepository;

    public CommunityController(BoardService boardService,
        BoardRepository boardRepository) {
        this.boardService = boardService;
        this.boardRepository = boardRepository;
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


    //게시판 저장
    @ResponseBody
    @PostMapping("/board/create")
    public Board board_Create(@RequestBody Board board){
        log.info("params={}", board);

        boardService.board_Create(board);
        return board;
    };


    @GetMapping("/details")
    public String board(@RequestParam(value = "idx", defaultValue = "0") Long idx,
                        @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser, Model model) {
        boolean check = boardService.writeUserCheck(loginUser, idx);
        model.addAttribute("board", boardService.findBoardByIdx(idx));
        model.addAttribute("check", check);
        return "view/communityboardPick";
    }
}
