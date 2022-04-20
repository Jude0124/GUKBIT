package com.gukbit.repository;

import com.gukbit.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    Board findByAuthor(String author);

    @Modifying
    @Query("update Board a set a.view = a.view + 1 where a.bid = :id")
    int updateView(int id);
}
