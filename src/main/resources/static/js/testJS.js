(function () {

  $('#academy-go-review-input').on('click', function (evt) {
    alert('선택되었습니다')
    let loginUser = $('#loginUser-id').val();
    if (loginUser==""){
      alert('로그인해주세요')
      evt.preventDefault();
      return;
    }else {
      alert('인증된 회원입니다')
    }

  })

})();