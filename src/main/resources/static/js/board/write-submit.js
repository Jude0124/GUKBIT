import {Util} from '../util/util.js';

window.writeSubmit = function () {
  //step2. 게시판 등록

  var params = {
    title: $.trim($('#boardTitle').val()),
    content: $.trim($('#boardContent').val()),
    author: $('#loginId').val(),
    date: 0,
    view: 0,
    // bAcademyName : $('#authUserAcademy > option').text(),
    bAcademyName : $('#authUserAcademyName').val(),
    bAcademyCode : $('#authUserAcademy').val(),
    bCourseName : $('#authUserCourseName').val(),
    bCourseCode : $('#authUserCourse').val(),
    visible:1,
    recommend:0
  };
  const referrer = document.referrer;

  if (params.title ==='') {
    Swal.fire({
      icon: 'error',
      text: '제목을 입력해주세요'
    });
    return;
  } else if (params.content ==='') {
    Swal.fire({
      icon: 'error',
      text: '내용을 입력해주세요'
    });
    return;
  }

  if(Util.hasSwear(params.title, params.content)){
    return;
  }


  $.ajax({
    type: 'POST',
    url: '/board/create',
    dataType: 'json',
    data: JSON.stringify(params),

    contentType: 'application/json',
    success: function () {
      Swal.fire({
        icon: 'success',
        text: '해당 글이 등록되었습니다'
      }).then(() => {
        location.href = '/board/list/sortByDate';//전체게시판으로 돌아가기

        // location.href = referrer; // ver2.
      });
    },
    error: function (request, status, error) {},
  });
}