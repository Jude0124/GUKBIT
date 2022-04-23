package com.gukbit.repository;

import com.gukbit.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("select  c from Course c join fetch c.academy where c.fields=:fields AND c.end >= CURRENT_DATE group by c.academycode, c.fields" )
    List<Course> findAllByFields(@Param(value="fields") String fields);

    @Query("select  c from Course c join fetch c.academy where c.academycode=:academycode" )
    List<Course> findAllByAcademyCode(@Param(value="academycode") String academycode);

    /* List<Course> findAllJoinFatch(); */

    List<Course> findByAcademycode(String academyCode);
    Course findByIdAndSession(String courseId, int session);
    List<Course> findAllById(String courseId);
}
