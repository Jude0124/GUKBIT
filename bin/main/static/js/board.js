/* 페이지 쿼리스트링 얻는 함수 */
function searchParam(key) {
  return new URLSearchParams(location.search).get(key);
}
/* pagination 강조 */
$(function () {

  const pageParam = searchParam('page');
  $('.page-number').each(function() {
    var text = $(this).text();
    if (text===pageParam){
      $(this).css('background-color', '#01a0ff');
      $(this).css('color', 'white');
    } else if (pageParam===null){
      $('.page-number').eq(0).css('background-color', '#01a0ff');
      $('.page-number').eq(0).css('color', 'white');
    }
  });

  const academyCode = searchParam('academyCode');
  if (academyCode!=null){
    $('#tabmenu2').prop("checked", true)
  }
})