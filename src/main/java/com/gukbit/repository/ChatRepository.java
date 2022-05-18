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

//    @Query(value = "SELECT distinct c.academyCode FROM Chat c where c.userId=:userId")
//    List<String> findByUserId(@Param("userId") String userId);

    @Query(value = "select c.academy_code from chat c where (c.academy_code, c.chat_date) \n" +
            "in (select c2.academy_code, max(c2.chat_date) from chat c2 group by c2.academy_code) and user_id=:userId order by c.chat_date desc limit 5", nativeQuery = true)
    List<String> findByUserId(@Param("userId") String userId);
}
