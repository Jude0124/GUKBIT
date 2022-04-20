(function () {

  $('#acd_cour_page').css('display', 'none');

  const academymenu = document.querySelector('.academy_middle');

  $(".academy_middle").on('click', function (e) {
    /*  menuWrap.addEventListener('click', e => { */
    //function unselect_removeAtt() {
    $(".academy_middle ul li").each(function (index, element) {
      if (element.classList.contains('selected') === true) {
        element.classList.remove("selected");
      }
    });
    const selected = e.target;
    // unselect_removeAtt(menuWrap);
    select(academymenu, selected);
  });

  function select(ulEl, liEl) {
    Array.from(ulEl.children).forEach(
        v => v.classList.remove('selected')
    )
    if (liEl) {
      liEl.classList.add('selected');
    }
  };

  $(".academy_middle ul li").on('click', function (e) {
    var index = $(".academy_middle ul li").index(this);

    $('#acd_review_page').css('display', 'none');
    $('#acd_cour_page').css('display', 'none');

    switch (index) {
      case 0:
        $('#acd_review_page').css('display', 'block');
        break;

      case 1:
        $('#acd_cour_page').css('display', 'block');
        break;

      default:

        break;

    }

  });

  /* 남은 */
  /* 페이지 쿼리스트링 얻는 함수 */
  function searchParam(key) {
    return new URLSearchParams(location.search).get(key);
  };

  $('#academy-go-review-input').on('click', function (evt) {
    /* 로그인 여부 확인 */
    let loginUser = $('#loginUser-id').val();
    console.log(loginUser);
    if (loginUser == null) {
      alert('로그인해주세요')
      evt.preventDefault();
      return;
    }
    /* 로그인 유저가 인증을 하지 않은 경우 authData null */
    let authUserAcademyCode = $('#loginUser-authinfo').val();
    console.log(authUserAcademyCode);
    if (authUserAcademyCode == null) {
      alert('해당 학원의 과정 인증이 필요합니다.')
      evt.preventDefault();
      return;
    } else { // 로그인 유저가 인증은 되어있으나 학원코드가 맞지 않는 경우
      if (authUserAcademyCode != searchParam('code')) {
        alert('인증된 학원과 일치하지 않습니다')
        evt.preventDefault();
        return;
      } else {
      }
      alert('리뷰 입력 페이지로 이동합니다.')
    }
  })
})();