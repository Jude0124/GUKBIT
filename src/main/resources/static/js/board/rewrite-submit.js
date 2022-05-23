
import {Util} from '../util/util.js';


window.rewriteSubmit = function () {

  // 제목과 내용을 담은 변수
  var params = {
    title: $.trim($('#boardTitle').val()),
    content: $.trim($('#boardContent').val()),
  };

  if (params.title =='') {
    Swal.fire({
      icon: 'error',
      text: '제목을 입력해주세요'
    });
    return false;
  }else if($('#boardContent').summernote('isEmpty')) {
    Swal.fire({
      icon: 'error',
      text: '내용을 입력해주세요'
    });
    return false;
  }



  if( Util.hasSwear(params.title, params.content)){
    return false;
  }
return true;
}