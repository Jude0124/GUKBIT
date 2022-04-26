package com.gukbit.repository;

import com.gukbit.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("select  c from Course c join fetch c.academy where c.fields like :fields% AND c.end >= CURRENT_DATE group by c.academyCode" )
    List<Course> findAllByFieldsStartingWith(@Param(value="fields") String fields);

    @Query(value="select c from Course c join fetch c.academy WHERE c.fields IN(:#{#fields[0]},:#{#fields[1]},:#{#fields[2]},:#{#fields[3]}) AND c.end >= CURRENT_DATE group by c.academyCode")
    List<Course> findAllByFieldsIn (@Param(value="fields") String[] fields);

    @Query("select  c from Course c join fetch c.academy where c.academyCode=:academyCode" )
    List<Course> findAllByAcademyCode(@Param(value="academyCode") String academyCode);

    /* List<Course> findAllJoinFatch(); */

    List<Course> findByAcademyCode(String academyCode);
    Course findByIdAndSession(String courseId, int session);
    List<Course> findAllById(String courseId);
}
