package com.gukbit.repository;

import com.gukbit.domain.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Integer> {

    @Query("select r from Reply r join fetch r.board where r.rBid=:bid" )
    List<Reply> findAllByBid(@Param(value="bid") Integer bid);

    Page<Reply> findAllByrAuthor(@Param(value="rAuthor") String userId, Pageable pageable);


    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM reply r Where r.r_rid=:rrid")
    void deleteByRrid(@Param(value="rrid") int rrid);
}
