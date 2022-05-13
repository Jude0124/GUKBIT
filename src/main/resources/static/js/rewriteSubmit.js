import {Util} from './util.js';


window.rewriteSubmit = function () {
  // 제목과 내용을 담은 변수
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

  if (params.title =='') {
    Swal.fire({
      icon: 'error',
      text: '제목을 입력해주세요'
    });
    return false;
  } else if (params.content =='') {
    Swal.fire({
      icon: 'error',
      text: '내용을 입력해주세요'
    });
    return false;
  }

  if( Util.hasSwear(params.title, params.content)){
    return ;
  }



  $.ajax({
    type: 'POST',
    url: '/board/rewrite',
    dataType: 'json',
    data: JSON.stringify(params),

    contentType: 'application/json',
    success: function (result) {
      Swal.fire({
        icon: 'success',
        text: '해당 글이 수정되었습니다'
      }).then((result) => {
        location.href = '/board/list';
      });
    },
    error: function (request, status, error) {},
  });
}