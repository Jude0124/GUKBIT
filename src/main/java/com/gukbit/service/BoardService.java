package com.gukbit.service;


import com.gukbit.domain.Board;
import com.gukbit.domain.User;
import com.gukbit.dto.BoardDto;
import com.gukbit.repository.AuthUserDataRepository;
import com.gukbit.repository.BoardRepository;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final AuthUserDataRepository authUserDataRepository;


    public BoardService(BoardRepository boardRepository,AuthUserDataRepository authUserDataRepository) {
        this.boardRepository = boardRepository;
        this.authUserDataRepository = authUserDataRepository;
    }

    //페이징하여 보드 반환
    public Page<Board> findBoardList(Pageable pageable, String column) {
        Sort sort = Sort.by(column).descending();
        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, 7,sort);
        return boardRepository.findAll(pageable);
    }

    public Page<Board> findAcademyBoardList(String academyCode, Pageable pageable, String column) {
        Sort sort = Sort.by(column).descending();
        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, 7,sort);
        Page<Board> Board = boardRepository.findBybAcademyCode(academyCode, pageable);
        return Board;
    }

    public Page<Board> findBoardSample(Pageable pageable, String column) {
        Sort sort = Sort.by(column).descending();
        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, 5,sort);
        return boardRepository.findAll(pageable);
    }

    public Boolean findAuthByUserId(String userId) {
        if (authUserDataRepository.findByUserId(userId) != null) {
            return true;
        } else {
            return false;
        }
    }
    

    //보드 생성
    @Transactional
    public void boardCreate(BoardDto boardDto) {
        boardDto.setDateNow();
        boardRepository.save(boardDto.toEntity());
    }

    //id로 보드 반환
    public Board findBoardByIdx(Integer bid) {
        return boardRepository.findById(bid).orElse(null);
    }

    //보드 삭제
    public void deleteBoard(Integer bid){
        System.out.println("BoardService.deleteBoard");
        System.out.println("bid = " + bid);
        boardRepository.deleteById(bid);
    }

    //보드 갱신
    @Transactional
    public void updateBoard(BoardDto boardDto){
        boardRepository.save(boardDto.toEntity());
    }

    /* Views Counting */
    @Transactional
    public int updateView(int id) {return boardRepository.updateView(id);}

    public List<Board> getBoardList(){
        return boardRepository.findAll();
    }

    public List<Board> getBoardListByUserId(String userId){
        return boardRepository.findAllByAuthorContaining(userId);
    }

    public List<Board> getBoardListByTitle(String title){
        return boardRepository.findAllByTitleContaining(title);
    }

    public void saveBoard(Board board){boardRepository.save(board);}

    /* Views Counting */
    @Transactional
    public int updateRecommend(int id) {return boardRepository.updateRecommend(id);
    }

    @Transactional
    @Modifying
    public void viewRecommendUpdate(Integer idx, HttpServletRequest request, HttpServletResponse response, String param) {
        boolean cookieHas = false;
//        param = "boardView or boardRecommend"
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                String name = cookie.getName();
                String value = cookie.getValue();
                if(param.equals(name) && value.contains("|" + idx + "|")) {
                    cookieHas = true;
                    break;
                }
            }
        }

        if(!cookieHas) {
            Cookie cookie = new Cookie(param, param+"|" + idx + "|");
            if (param.equals("boardView")){
                cookie.setMaxAge(-1);
                //브라우저 끄면 쿠기 사라지고 조회수 증가 가능
                response.addCookie(cookie);
                this.updateView(idx);
            } else if (param.equals("boardRecommend")){
                cookie.setMaxAge(60 * 60 * 24 * 365);
                //브라우저 꺼도 쿠키 안사라지고 1년동안 보관.
                // 쿠키를 지우지 않고선 1년 동안 추천수 조작 불가
                response.addCookie(cookie);
                this.updateRecommend(idx);
            }
        }
    }

}
