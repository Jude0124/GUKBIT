package com.gukbit.repository;

import com.gukbit.domain.Board;
import com.gukbit.domain.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    Board findByAuthor(String author);

    Page<Board> findByBacademycode(String academyCode, Pageable pageable);

    @Modifying
    @Query("update Board a set a.view = a.view + 1 where a.bid = :id")
    int updateView(@Param("id") int id);
}
