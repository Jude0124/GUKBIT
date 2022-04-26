package com.gukbit.repository;

import com.gukbit.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("select  c from Course c join fetch c.academy where c.fieldS like :fieldS% AND c.end >= CURRENT_DATE group by c.academycode" )
    List<Course> findAllByFieldSStartingWith(@Param(value="fieldS") String fieldS);

    @Query(value="select c from Course c join fetch c.academy WHERE c.fieldS IN(:#{#fieldS[0]},:#{#fieldS[1]},:#{#fieldS[2]},:#{#fieldS[3]}) AND c.end >= CURRENT_DATE group by c.academycode")
    List<Course> findAllByFieldSIn (@Param(value="fieldS") String[] fields);

    @Query("select  c from Course c join fetch c.academy where c.academycode=:academycode" )
    List<Course> findAllByAcademyCode(@Param(value="academycode") String academycode);

    /* List<Course> findAllJoinFatch(); */

    List<Course> findByAcademycode(String academyCode);
    Course findByIdAndSession(String courseId, int session);
    List<Course> findAllById(String courseId);
}
