package com.gukbit.repository;

import com.gukbit.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("select  c from Course c join fetch c.academy where c.fields=:fields group by c.academycode" )
    List<Course> findAllByFields(@Param(value="fields") String fields);

    /* List<Course> findAllJoinFatch(); */


//    @Query("SELECT m FROM Course m WHERE m.academycode = :academycode")
    List<Course> findByAcademycode(String academyCode);
    List<Course> findAllById(String courseId);
}
