(function () {
$('#form-review-input').submit(function(e){

  let oneStatement = $("textarea[name='oneStatement']")
  let advantage = $("textarea[name='advantage']")
  let disadvantage = $("textarea[name='disadvantage']")
  let lecturers_eval = $("input:radio[name='lecturersEval']:checked")
  let employment_eval = $("input:radio[name='employmentEval']:checked")
  let facility_eval = $("input:radio[name='facilityEval']:checked")
  let curriculum_eval = $("input:radio[name='curriculumEval']:checked")
  let culture_eval = $("input:radio[name='cultureEval']:checked")

  /* 한줄평 */
  if(oneStatement.val()===""){
    alert('한줄평을 입력하지 않았습니다')
    e.preventDefault();
    oneStatement.focus();
    return false;
  }

  if (oneStatement.val().length<5||oneStatement.val().length>30){
    alert('한줄평을 5글자 이상 30글자 미만으로 입력해주세요')
    e.preventDefault();
    oneStatement.focus();
    return false;
  }
  /* 장점 */
  if(advantage.val()===""){
    alert('장점을 입력하지 않았습니다')
    e.preventDefault();
    advantage.focus();
    return false;
  }
  if(advantage.val().length<10||advantage.val().length>300){
    console.log(advantage.val())

    alert('장점을 10글자 이상 300자 미만으로 입력해주세요')
    e.preventDefault();
    advantage.focus();
    return false;
  }
  /* 단점 */
  if(disadvantage.val()===""){
    alert('단점을 입력하지 않았습니다')
    e.preventDefault();
    disadvantage.focus();
    return false;
  }
  if(disadvantage.val().length<10||disadvantage.val().length>300){
    console.log(disadvantage.val())
    alert('장점을 10글자 이상 300자 미만으로 입력해주세요')
    e.preventDefault();
    disadvantage.focus();
    return false;
  }
  /* 별점 */
  if(lecturers_eval.length < 1){
    alert("강사진 별점을 선택해주세요.");
    e.preventDefault();
    return false;
  }
  if(employment_eval.length < 1){
    alert("취업연계 별점을 선택해주세요.");
    e.preventDefault();
    return false;
  }
  if(facility_eval.length < 1){
    alert("운영 및 시설 별점을 선택해주세요.");
    e.preventDefault();
    return false;
  }
  if(curriculum_eval.length < 1){
    alert("커리큘럼 별점을 선택해주세요.");
    e.preventDefault();
    return false;
  }
  if(culture_eval.length < 1){
    alert("학원 내 문화 별점을 선택해주세요.");
    e.preventDefault();
    return false;
  }
alert('리뷰 작성이 완료되었습니다.')

});
})();