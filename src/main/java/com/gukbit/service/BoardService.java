package com.gukbit.service;


import com.gukbit.domain.Board;
import com.gukbit.domain.User;
import com.gukbit.repository.AuthUserDataRepository;
import com.gukbit.repository.BoardRepository;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final AuthUserDataRepository authUserDataRepository;


    public BoardService(BoardRepository boardRepository,AuthUserDataRepository authUserDataRepository) {
        this.boardRepository = boardRepository;
        this.authUserDataRepository = authUserDataRepository;
    }

    //페이징하여 보드 반환
    public Page<Board> findBoardList(Pageable pageable) {
        Sort sort = Sort.by("bid").descending();
        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, 5,sort);
        return boardRepository.findAll(pageable);
    }
    public Page<Board> findBoardSampleNew(Pageable pageable) {
        Sort sort = Sort.by("date").descending();
        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, 5,sort);
        return boardRepository.findAll(pageable);
    }

    public Page<Board> findBoardSampleBest(Pageable pageable) {
        Sort sort = Sort.by("view").descending();
        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, 5,sort);
        return boardRepository.findAll(pageable);
    }

    public Page<Board> alignByView(Pageable pageable) {
        Sort sort = Sort.by("view").descending();
        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, 5,sort);
        return boardRepository.findAll(pageable);
    }
    public Page<Board> alignByDate(Pageable pageable) {
        Sort sort = Sort.by("date").descending();
        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, 5,sort);
        return boardRepository.findAll(pageable);
    }

    @Transactional
    public Page<Board> findAcademyBoardList(String academyCode, Pageable pageable) {
        Sort sort = Sort.by("bid").descending();
        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, 5,sort);
        Page<Board> academyBoard = boardRepository.findByBacademycode(academyCode, pageable);
        return academyBoard;
    }

    //보드 생성
    public void board_Create(Board board) {
        boardRepository.save(board);
    }

    //id로 보드 반환
    public Board findBoardByIdx(Integer bid) {
        return boardRepository.findById(bid).orElse(new Board());
    }

    //보드 삭제
    public void deleteBoard(Integer bid){
        boardRepository.deleteById(bid);
    }

    //보드 갱신
    public void updateBoard(Board board){
        boardRepository.save(board);
    }

    //보드를 클릭한 유저가 본인인지 체크
    public boolean writeUserCheck(User loginUser, Integer bid){
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

    /* Views Counting */
    @Transactional
    public int updateView(int id) {
        return boardRepository.updateView(id);
    }
    public Boolean findAuthByUserId(String userId) {
        if (authUserDataRepository.findByUserId(userId) != null) {
            return true;
        } else {
            return false;
        }
    }
}
