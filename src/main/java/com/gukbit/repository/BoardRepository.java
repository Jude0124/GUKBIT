package com.gukbit.repository;

import com.gukbit.domain.Board;
import com.gukbit.domain.User;
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
    Page<Board> findBybAcademyCode(String academyCode, Pageable pageable);

    @Modifying
    @Query("update Board a set a.view = a.view + 1 where a.bid = :id")
    int updateView(@Param("id") int id);

//    @Query(value = "SELECT user FROM User user WHERE user.userId LIKE %:userId% ORDER BY user.userId")
//    List<User> findByUserIdContaining(@Param("userId") String userId);
    @Query(value = "SELECT board FROM Board board WHERE board.author LIKE %:userId% ORDER BY board.bid")
    List<Board> findAllByAuthorContaining(String userId);

    @Query(value = "SELECT board FROM Board board WHERE board.title LIKE %:title% ORDER BY board.bid")
    List<Board> findAllByTitleContaining(String title);

    @Modifying
    @Query("update Board a set a.recommend = a.recommend + 1 where a.bid = :id")
    int updateRecommend(@Param("id") int id);
}
