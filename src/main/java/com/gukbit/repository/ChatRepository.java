package com.gukbit.repository;

import com.gukbit.domain.Chat;
import com.gukbit.dto.ChatDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

    List<ChatDto> findByAcademyCode(String academyCode);

    @Query(value = "SELECT distinct c.academyCode FROM Chat c where c.userId=:userId")
    List<String> findByUserId(@Param("userId") String userId);
}
