package com.gukbit.service;


import com.gukbit.domain.Notice;
import com.gukbit.domain.Notice;
import com.gukbit.domain.User;
import com.gukbit.repository.NoticeRepository;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


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
    public void notice_Create(Notice notice) {
        noticeRepository.save(notice);
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
    public void updateNotice(Notice notice){noticeRepository.save(notice);}
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

    @Transactional
    public int updateView(int id) {
        return noticeRepository.updateView(id);
    }

}
