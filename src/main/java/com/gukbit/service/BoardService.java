package com.gukbit.service;


import com.gukbit.domain.Board;
import com.gukbit.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class BoardService {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Page<Board> findBoardList(Pageable pageable) {
        Sort sort = Sort.by("bid").descending();
        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, 5,sort);
        System.out.println("pageable = " + pageable);
        return boardRepository.findAll(pageable);
    }

    public void board_Create(Board board) {
        boardRepository.save(board);
    }

//    public Board findBoardByIdx(Long idx) {
//        return boardRepository.findById(idx).orElse(new Board());
//    }

}
