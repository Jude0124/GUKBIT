package com.gukbit.service;

import com.gukbit.domain.AuthUserData;
import com.gukbit.domain.Course;
import com.gukbit.domain.Rate;
import com.gukbit.dto.RateDto;
import com.gukbit.repository.AuthUserDataRepository;
import com.gukbit.repository.CourseRepository;
import com.gukbit.repository.RateRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RateService {

    private RateRepository rateRepository;
    private CourseRepository courseRepository;
    private AuthUserDataRepository authUserDataRepository;

    public RateService(RateRepository rateRepository, CourseRepository courseRepository,
                       AuthUserDataRepository authUserDataRepository) {

        this.rateRepository = rateRepository;
        this.courseRepository = courseRepository;
        this.authUserDataRepository = authUserDataRepository;
    }

    @Transactional
    public void saveReview(RateDto rateDto) {
        rateRepository.save(rateDto.toEntity());
    }

    public List<Course> getCoursesByAcademyCode(String academyCode) {
        List<Course> courseListForAcademy = courseRepository.findByAcademycode(academyCode);
        return courseListForAcademy;
    }

    public Course getCourseByCourseidAndSession(String courseId, int session) {
        Course courseForAcademy = courseRepository.findByIdAndSession(courseId, session);
        return courseForAcademy;
    }

    public AuthUserData getAuthUserData(String userId) {
        AuthUserData authUserData = authUserDataRepository.findByUserId(userId);
        return authUserData;
    }

    public RateDto findByRid(String rid) {
        Rate rateByRid = rateRepository.findByRid(rid);
        RateDto rateDtoByRid;

        rateDtoByRid = RateDto.builder()
                .rid(rateByRid.getRid())
                .cCid(rateByRid.getCCid())
                .userId(rateByRid.getUserId())
                .lecturersEval(rateByRid.getLecturersEval())
                .curriculumEval(rateByRid.getCurriculumEval())
                .employmentEval(rateByRid.getEmploymentEval())
                .cultureEval(rateByRid.getCultureEval())
                .facilityEval(rateByRid.getFacilityEval())
                .advantage(rateByRid.getAdvantage())
                .disadvantage(rateByRid.getDisadvantage())
                .oneStatement(rateByRid.getOneStatement())
                .build();
        return rateDtoByRid;
    }

    public void deleteRate(String rid) {
        rateRepository.deleteById(rid);
    }

    public Boolean findRateByUserId(String userId) {
        if (rateRepository.findByUserId(userId) != null) {
            return true;
        } else {
            return false;
        }

    }

    public List<Rate> findAllRateByCourseIdIn(List<String> courseId) {
        return rateRepository.findAllBycCidIn(courseId);
    }
}
