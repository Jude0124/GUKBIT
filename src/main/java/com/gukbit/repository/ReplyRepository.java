package com.gukbit.repository;

import com.gukbit.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Integer> {

    @Query("select r from Reply r join fetch r.board where r.rBid=:bid" )
    List<Reply> findAllByBid(@Param(value="bid") Integer bid);
}
