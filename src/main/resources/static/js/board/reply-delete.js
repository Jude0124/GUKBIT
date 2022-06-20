function replyDelete(rAuthor, rid) {
  // 제목과 내용의 value를 담을 변수
  let obj = {"rid": rid , "rAuthor" : rAuthor};
  $.ajax({
    url: "/board/replyDelete",
    type: 'post',
    data: JSON.stringify(obj),
    contentType: 'application/json',
    success: function (data) {
      //화면 re-rendering 필요
      if (data == 'true') {
        location.reload();
      }
    }
  });
};
