package com.gukbit.repository;

import com.gukbit.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;


@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    Board findByAuthor(String author);

    List<Board> findByBacademycode(String Bacademycode, Pageable pageable);

}
