package com.gukbit.controller;


import com.gukbit.domain.Board;
import com.gukbit.domain.User;
import com.gukbit.service.BoardService;
import com.gukbit.repository.BoardRepository;
import com.gukbit.session.SessionConst;
import com.gukbit.etc.Today;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


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
    public String communityAllBoardMapping(Pageable pageable,Today today, Model model) {
        Page<Board> p = boardService.findBoardList(pageable);
        model.addAttribute("boardList", p);
        model.addAttribute("Today", today);

        return "view/communityboard";
    }

    @GetMapping("/academy")
    public String academyBoard(@RequestParam String academyCode, Pageable pageable, Model model) {
        Page<Board> page = boardService.findAcademyBoardList(academyCode, pageable);
        model.addAttribute("boardList", page);
        return "view/academyboard";
    }




    @GetMapping("/write")
    public String communityWriteMapping() {
        return "view/communityboard-write";
    }

    @GetMapping("/delete")
    public String communityDeleteMapping(@RequestParam(value = "bid", defaultValue = "0") Integer bid) {
        boardService.deleteBoard(bid);
        return "redirect:/community/list";
    }

    @GetMapping("/rewrite")
    public String communityReWriteMapping(@RequestParam(value = "bid", defaultValue = "0") Integer bid,Model model) {
        System.out.println("CommunityController.communityReWriteMapping");
        System.out.println(boardService.findBoardByIdx(bid));
        model.addAttribute("board", boardService.findBoardByIdx(bid));
        return "view/communityboard-rewrite";
    }
    
    @PostMapping("/rewrite")
    public String communityPostReWriteMapping(@ModelAttribute("board") Board board, BindingResult bindingResult) {
        System.out.println("board = " + board);
        boardService.updateBoard(board);
        return "redirect:/community/list";
    }
//    Board board = new Board();
//        board.setBid(Integer.parseInt(request.getParameter("bid")));
//        board.setAuthor(request.getParameter("author"));
//        board.setDate(request.getParameter("date"));
//        board.setTitle(request.getParameter("title"));
//        board.setContent(request.getParameter("content"));
//        board.setBacademycode(request.getParameter("bacademycode"));
//        board.setB_course_id(request.getParameter("b_course_id"));
//        board.setVisible(true);
//        board.setView(0);
//        board.setRecommend(0);

    //게시판 저장
    @ResponseBody
    @PostMapping("/board/create")
    public Board board_Create(@RequestBody Board board){
        log.info("params={}", board);

        boardService.board_Create(board);
        return board;
    };


    @GetMapping("/details")
    public String board(@RequestParam(value = "idx", defaultValue = "0") Integer idx,
                        @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser, Model model) {
        boolean check = boardService.writeUserCheck(loginUser, idx);
        Board board = boardService.findBoardByIdx(idx);
        boardService.updateView(idx);
        model.addAttribute("board", board);
        model.addAttribute("check", check);
        return "view/communityboardPick";
    }
}
