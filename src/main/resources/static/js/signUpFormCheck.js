function idCheck() {
  let id = $("#userId").val()
  if(id.search(/\s/) != -1) {
    alert("아이디에는 공백이 들어갈 수 없습니다.");
  } else {
    if(id.trim().length != 0) {
      $.ajax({
        async : true,
        type : 'POST',
        data: id,
        url: "/user/idCheck",
        dataType: "json",
        contentType: "application/json; charset=UTF-8",
        success: function(count) {
          if(count > 0) {
            alert("해당 아이디가 이미 존재합니다.");
            $("#submit").attr("disabled", "disabled");
            window.location.reload();
          } else {
            alert("사용가능한 아이디입니다.");
            $("#submit").removeAttr("disabled");
            $("input[name=checked_id]").val('y');
          }
        },
        error: function(error) {
          alert("아이디를 입력해주세요.");
        }
      });
    } else {
      alert("아이디를 입력해주세요.");
    }
  }
}

$(function(){
  $("#submit").click(function() {

    if ($("input[name='checked_id']").val() == '') {
      alert('아이디중복 확인을 해주세요.');
      $("input[name='checked_id']").eq(0).focus();
      return false;
    }
  });
});