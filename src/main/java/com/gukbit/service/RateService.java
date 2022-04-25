package com.gukbit.service;

import com.gukbit.domain.Academy;
import com.gukbit.domain.AuthUserData;
import com.gukbit.domain.Course;
import com.gukbit.domain.Rate;
import com.gukbit.dto.RateDto;
import com.gukbit.repository.AcademyRepository;
import com.gukbit.repository.AuthUserDataRepository;
import com.gukbit.repository.CourseRepository;
import com.gukbit.repository.RateRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

@Service
public class RateService {

    private AcademyRepository academyRepository;
    private RateRepository rateRepository;
    private CourseRepository courseRepository;
    private AuthUserDataRepository authUserDataRepository;

    public RateService(RateRepository rateRepository, CourseRepository courseRepository,
                       AuthUserDataRepository authUserDataRepository, AcademyRepository academyRepository) {
        this.academyRepository = academyRepository;
        this.rateRepository = rateRepository;
        this.courseRepository = courseRepository;
        this.authUserDataRepository = authUserDataRepository;
    }

    @Transactional
    public void saveReview(RateDto rateDto) {
        rateRepository.save(rateDto.toEntity());
    }

    @Transactional
    public void saveReviewEval(RateDto rateDto, String code, int check){
        // code가 null인지 아닌지 확인
        if(code!=null){
            // select list : Allfind Rate // Join Course
            List<Rate> rate = rateRepository.findAllFetch();

            // 반영 되어있던 Eval의 총점
            double sumEval = 0;
            int rateCount =0;
            for(int i=0; i<rate.size(); i++) {
                try{
                    if (rate.get(i).getCourse().getAcademycode().equals(code) && !rate.get(i).getRid().equals(rateDto.getRid())) {
                        rateCount++;
                        sumEval += (rate.get(i).getCultureEval() + rate.get(i).getCurriculumEval() + rate.get(i).getEmploymentEval() + rate.get(i).getFacilityEval() + rate.get(i).getLecturersEval()) / 5;
                    }
                } catch( NullPointerException e) {
                    if (rate.get(i).getCourse().getAcademycode().equals(code)) {
                        rateCount++;
                        sumEval += (rate.get(i).getCultureEval() + rate.get(i).getCurriculumEval() + rate.get(i).getEmploymentEval() + rate.get(i).getFacilityEval() + rate.get(i).getLecturersEval()) / 5;
                    }
                }
            }

            // 반영 할 Eval
            // 계산식 : 입력되는 Eval의 합산/5
            double inputEval = 0;
            if(rateDto != null) { //삭제연산을 위하여 작성
                inputEval = (rateDto.getCultureEval() + rateDto.getCurriculumEval() + rateDto.getEmploymentEval() + rateDto.getFacilityEval() + rateDto.getLecturersEval()) / 5;
            }
            // 수정 될 Eval
            // 계산식 : (반영되어있던 Eval의 총점 + 반영 할 Eval) / (반영되어있는 리뷰수+반영할 리뷰(1))
            double modifyEval =0;
            if((rateCount+check)>0) {
                modifyEval = (sumEval + inputEval) / (rateCount + check);
            }

            academyRepository.setEval(code, modifyEval);
        }

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
