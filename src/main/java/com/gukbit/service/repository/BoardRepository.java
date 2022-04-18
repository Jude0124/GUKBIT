package com.gukbit.service.repository;

import com.gukbit.dto.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findByAuthor(String author);
}
