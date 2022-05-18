
$(document).ready(function () {

  $('.userInfo').submit(function (e) {
    let pwCheck = 0;
    let formCheck = 0;

    var checkPattern = RegExp(/^[A-Za-z0-9_\-]{4,16}$/);
    if (checkPattern.test($('#changePassword').val())) {
      pwCheck++;
    }

    if (checkPattern.test($('#changePasswordCheck').val())) {
      pwCheck++;
    }

    if ($('#changePasswordCheck').val() === '' && $('#changePassword').val()
        === '') {
      pwCheck = 3;
    }

    if ($('.form-select option:selected').text() !== '과정명 선택') {
      formCheck = 1;
    }

    if (formCheck && pwCheck === 2) {
      Swal.fire({
        title: '비밀번호와 과정을 수정합니다.<br>정말 수정하시겠습니까?',
        text: "※과정을 수정하시면 이전에 작성했던 평가와 별점이 삭제됩니다.",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#0059ab',
        cancelButtonColor: '#606060',
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then((result) => {
        if (result.isConfirmed) {
          $(this).unbind();
          $(this).submit();
          return true;
        }
      })

      return false;
    } else if (formCheck && pwCheck === 3) {
      Swal.fire({
        title: '과정을 수정합니다.<br>정말 수정하시겠습니까?',
        text: "※과정을 수정하시면 이전에 작성했던 평가와 별점이 삭제됩니다.",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#0059ab',
        cancelButtonColor: '#606060',
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then((result) => {
        if (result.isConfirmed) {
          $(this).unbind();
          $(this).submit();
          return true;
        }
      })
      return false;
    }

    if (pwCheck === 2) {
      Swal.fire({
        title: '비밀번호를 수정하시겠습니까?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#0059ab',
        cancelButtonColor: '#606060',
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then((result) => {
        if (result.isConfirmed) {
          $(this).unbind();
          $(this).submit();
          return true;
        }
      })
    }

    if (formCheck === 0 && pwCheck === 3) {
      e.preventDefault();
      Swal.fire({
        icon: 'info',
        text: '수정된 항목이 없습니다',
        confirmButtonColor: '#0059ab',
        confirmButtonText: '확인'
      })

      return false;
    }

    if (pwCheck < 2) {
      e.preventDefault();
      Swal.fire({
        icon: 'warning',
        html: '비밀번호는 4자 이상 16자 미만의<br>영문과 숫자 조합으로 입력해주세요.',
        confirmButtonColor: '#0059ab',
        confirmButtonText: '확인'
      })
      return false;
    }
    return false;
  }); // mypage 유효성 검사 끝

  /* 평가 수정/삭제 버튼 클릭 시 */
  $('#authCourseRewrite').click(function (e) {
    let code = $('#academy-code').val();
    if ($('#authCourse').val() === '') {
      e.preventDefault();
      Swal.fire({
        icon: 'info',
        html: '과정 인증을 먼저 진행해주세요',
        confirmButtonColor: '#0059ab',
        confirmButtonText: '확인'
      })
      return false;
    }
    if ($('#comment').val() === '') {
      Swal.fire({
        title: '과정 평가 기록이 없습니다.',
        text: '학원 리뷰 페이지로 이동하시겠습니까?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#0059ab',
        cancelButtonColor: '#606060',
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then((result) => {
        if (result.isConfirmed) {
          location.href = '/academy/review?code=' + code;
        }
      })
      e.preventDefault();
      return false;
    }
    let href = $('#authCourseRewrite').attr('href');
    e.preventDefault()
    Swal.fire({
      icon: 'success',
      html: '평가 수정/삭제 페이지로 이동합니다.',
      showConfirmButton: false,
      timer: 1000
    }).then(function () {
      location.href = href;
    })
  })// click이벤트 끝
});

