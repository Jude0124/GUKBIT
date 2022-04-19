package com.gukbit.repository;

import com.gukbit.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findAllByFields (String fields);

//    @Query("SELECT m FROM Course m WHERE m.academycode = :academycode")
    List<Course> findByAcademycode(String academyCode);
}
