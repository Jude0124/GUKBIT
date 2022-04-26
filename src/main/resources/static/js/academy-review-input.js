(function () {
  $('#form-review-input').submit(function (e) {

    let oneStatement = $("textarea[name='oneStatement']")
    let advantage = $("textarea[name='advantage']")
    let disadvantage = $("textarea[name='disadvantage']")
    let lecturersEval = $("input:radio[name='lecturersEval']:checked")
    let employmentEval = $("input:radio[name='employmentEval']:checked")
    let facilityEval = $("input:radio[name='facilityEval']:checked")
    let curriculumEval = $("input:radio[name='curriculumEval']:checked")
    let cultureEval = $("input:radio[name='cultureEval']:checked")

    /* 한줄평 */
    if (oneStatement.val() === "") {
      // alert('한줄평을 입력하지 않았습니다')
      e.preventDefault();
      Swal.fire({
        icon: 'warning',
        text: '한줄평을 입력하지 않았습니다.',
        confirmButtonColor: '#0059ab',
        confirmButtonText: '확인'
      })
      // oneStatement.focus();
      return false;
    }

    if (oneStatement.val().length < 5 || oneStatement.val().length > 30) {
      // alert('한줄평을 5글자 이상 30글자 미만으로 입력해주세요')
      Swal.fire({
        icon: 'warning',
        text: '한줄평을 5글자 이상 30글자 미만으로 입력해주세요',
        confirmButtonColor: '#0059ab',
        confirmButtonText: '확인'
      })
      e.preventDefault();
      oneStatement.focus();
      return false;
    }
    /* 장점 */
    if (advantage.val() === "") {
      // alert('장점을 입력하지 않았습니다')
      Swal.fire({
        icon: 'warning',
        text: '장점을 입력하지 않았습니다',
        confirmButtonColor: '#0059ab',
        confirmButtonText: '확인'
      })
      e.preventDefault();
      advantage.focus();
      return false;
    }
    if (advantage.val().length < 10 || advantage.val().length > 300) {
      // alert('장점을 10글자 이상 300자 미만으로 입력해주세요')
      Swal.fire({
        icon: 'warning',
        text: '장점을 10글자 이상 300자 미만으로 입력해주세요',
        confirmButtonColor: '#0059ab',
        confirmButtonText: '확인'
      })
      e.preventDefault();
      advantage.focus();
      return false;
    }
    /* 단점 */
    if (disadvantage.val() === "") {
      // alert('단점을 입력하지 않았습니다')
      Swal.fire({
        icon: 'warning',
        text: '단점을 입력하지 않았습니다',
        confirmButtonColor: '#0059ab',
        confirmButtonText: '확인'
      })
      e.preventDefault();
      disadvantage.focus();
      return false;
    }
    if (disadvantage.val().length < 10 || disadvantage.val().length > 300) {
      // alert('장점을 10글자 이상 300자 미만으로 입력해주세요')
      Swal.fire({
        icon: 'warning',
        text: '장점을 10글자 이상 300자 미만으로 입력해주세요',
        confirmButtonColor: '#0059ab',
        confirmButtonText: '확인'
      })
      e.preventDefault();
      disadvantage.focus();
      return false;
    }
    /* 별점 */
    if (lecturersEval.length < 1) {
      // alert("강사진 별점을 선택해주세요.");
      Swal.fire({
        icon: 'warning',
        text: '강사진 별점을 선택해주세요.',
        confirmButtonColor: '#0059ab',
        confirmButtonText: '확인'
      })
      e.preventDefault();
      return false;
    }
    if (employmentEval.length < 1) {
      // alert("취업연계 별점을 선택해주세요.");
      Swal.fire({
        icon: 'warning',
        text: '취업연계 별점을 선택해주세요.',
        confirmButtonColor: '#0059ab',
        confirmButtonText: '확인'
      })
      e.preventDefault();
      return false;
    }
    if (facilityEval.length < 1) {
      // alert("운영 및 시설 별점을 선택해주세요.");
      Swal.fire({
        icon: 'warning',
        text: '운영 및 시설 별점을 선택해주세요.',
        confirmButtonColor: '#0059ab',
        confirmButtonText: '확인'
      })
      e.preventDefault();
      return false;
    }
    if (curriculumEval.length < 1) {
      // alert("커리큘럼 별점을 선택해주세요.");
      Swal.fire({
        icon: 'warning',
        text: '커리큘럼 별점을 선택해주세요.',
        confirmButtonColor: '#0059ab',
        confirmButtonText: '확인'
      })
      e.preventDefault();
      return false;
    }
    if (cultureEval.length < 1) {
      // alert("학원 내 문화 별점을 선택해주세요.");
      Swal.fire({
        icon: 'warning',
        text: '학원 내 문화 별점을 선택해주세요.',
        confirmButtonColor: '#0059ab',
        confirmButtonText: '확인'
      })
      e.preventDefault();
      return false;
    }
    // alert('리뷰 작성이 완료되었습니다.')
    e.preventDefault()
    Swal.fire({
      icon: 'success',
      html: '리뷰 작성이 완료되었습니다.',
      showConfirmButton: false,
      timer: 1000
    }).then(function () {
      $('#form-review-input').unbind('submit').submit();
    })
  });

  $('#review-delete-btn').click(function (e) {
    // if (confirm('정말 삭제하시겠습니까?')) {
    //   return true;
    // }
    e.preventDefault();
    Swal.fire({
      title: '정말 삭제하시겠습니까?',
      text: '삭제된 리뷰는 복구할 수 없습니다.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#Dc3545',
      cancelButtonColor: '#D5D5D5',
      confirmButtonText: '삭제',
      cancelButtonText: '취소'
    }).then((result) => {
      if (result.isConfirmed) {
        $('#delete-form').submit();
      }
    })
    e.preventDefault();
    return false;
  })
})();