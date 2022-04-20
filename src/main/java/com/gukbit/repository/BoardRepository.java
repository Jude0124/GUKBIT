package com.gukbit.repository;

import com.gukbit.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    Board findByAuthor(String author);

    Page<Board> findByBacademycode(String Bacademycode, Pageable pageable);

}
