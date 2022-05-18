package com.gukbit.service;


import com.gukbit.domain.Notice;
import com.gukbit.domain.User;
import com.gukbit.dto.NoticeDto;
import com.gukbit.repository.NoticeRepository;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Autowired
    public NoticeService(NoticeRepository noticeRepository) {this.noticeRepository = noticeRepository;}

    public Page<Notice> findNoticeList(Pageable pageable) {
        Sort sort = Sort.by("bid").descending();
        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, 5,sort);
        return noticeRepository.findAll(pageable);
    }

    //보드 생성
    @Transactional
    public void noticeCreate(NoticeDto noticeDto) {
        noticeRepository.save(noticeDto.toEntity());
    }
    //id로 보드 반환
    public Notice findNoticeByIdx(Integer bid) {
        return noticeRepository.findById(bid).orElse(new Notice());
    }
    //보드 삭제
    public void deleteNotice(Integer bid){
        noticeRepository.deleteById(bid);
    }
    //보드 갱신
    @Transactional
    public void updateNotice(NoticeDto noticeDto){noticeRepository.save(noticeDto.toEntity());}

    //보드를 클릭한 유저가 본인인지 체크
    public boolean writeUserCheck(User loginUser, Integer bid){
        if(noticeRepository.findById(bid).isEmpty()){
            return false;
        }
        if(loginUser == null){
            return false;
        }
        Notice notice = noticeRepository.findById(bid).get();
        if(notice.getAuthor().equals(loginUser.getUserId())){
            return true;
        }
        return false;
    }

    public Page<Notice> alignByView(Pageable pageable) {
        Sort sort = Sort.by("view").descending();
        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, 5,sort);
        return noticeRepository.findAll(pageable);
    }


    @Transactional
    public int updateView(int id) {
        return noticeRepository.updateView(id);
    }

    public List<Notice> getNoticeListByTitle(String searchTitle){
        return noticeRepository.findAllByTitleContaining(searchTitle);
    }

    public List<Notice> getNoticeList(){
        return noticeRepository.findAll();
    }


}
