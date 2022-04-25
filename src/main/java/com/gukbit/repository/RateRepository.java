package com.gukbit.repository;

import com.gukbit.domain.Rate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RateRepository extends JpaRepository<Rate, String> {

    Rate findByUserId(String userId);

    void deleteByUserId(String userId);

    Rate findByRid(String rid);

    List<Rate> findAllBycCidIn(List<String> courses);



//    @Query("select AVG(lecturers_eval),AVG(curriculum_eval),AVG(employment_eval),AVG(culture_eval),AVG(facility_eval) " +
//            "from (SELECT * FROM Rate r join fetch r.course  where r.c_cid = :c_cid)")
//    List<Rate> findAllByCCid(@Param(value="c_cid") String c_cid);
//
//    @Query("select AVG(r.lecturersEval), AVG(r.curriculumEval), AVG(r.employmentEval) , AVG(r.cultureEval), AVG(r.facilityEval)" +
//            "from Rate r join fetch r.course where r.cCid = :cCid group by r.cCid")


}

