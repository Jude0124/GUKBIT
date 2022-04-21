package com.gukbit.service;


import com.gukbit.domain.Notice;
import com.gukbit.domain.Notice;
import com.gukbit.domain.User;
import com.gukbit.repository.NoticeRepository;
import javax.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public void notice_Create(Notice notice) {
        noticeRepository.save(notice);
    }

    public Page<Notice> findNoticeList(Pageable pageable) {
        Sort sort = Sort.by("bid").descending();
        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, 5,sort);
        return noticeRepository.findAll(pageable);
    }

    public Notice findNoticeByIdx(Integer bid) {
        return noticeRepository.findById(bid).orElse(new Notice());
    }

    public void deleteNotice(Integer bid){
        noticeRepository.deleteById(bid);
    }

    public void updateNotice(Notice notice){
        noticeRepository.save(notice);
    }

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
}
