package com.gukbit.service.repository;

import com.gukbit.dto.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findAllByFields (String fields);

}
