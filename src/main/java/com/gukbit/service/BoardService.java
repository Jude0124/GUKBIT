package com.gukbit.service;


import com.gukbit.dto.domain.Board;
import com.gukbit.dto.domain.User;
import com.gukbit.service.repository.BoardRepository;
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
        return boardRepository.findAll(pageable);
    }

<<<<<<< HEAD

=======
    public void board_Create(Board board) {
        boardRepository.save(board);
    }
>>>>>>> f65fae4faf245fac58157e6c2549abfe1aab7272

    public Board findBoardByIdx(Long bid) {
        return boardRepository.findById(bid).orElse(new Board());
    }

    public void deleteBoard(Long bid){
        boardRepository.deleteById(bid);
    }

    public void updateBoard(Board board){
        boardRepository.save(board);
    }

    public boolean writeUserCheck(User loginUser, Long bid){
        if(boardRepository.findById(bid).isEmpty()){
            return false;
        }
        if(loginUser == null){
            return false;
        }
        Board board = boardRepository.findById(bid).get();
        if(board.getAuthor().equals(loginUser.getUserId())){
            return true;
        }
        return false;
    }
}
