$(document).ready(function(){
    $(".tabLocalList li").on("click", function(){
        // 클릭하는 요소의 index 번호를 가져옴
        const num = $(".tabLocalList li").index($(this));
        // 기존에 적용되어 있는 selected class 삭제
        $(".tabLocalList li").removeClass("selected");
        // 클릭한 요소에 selected class 추가
        $('.tabLocalList li:eq(' + num + ')').addClass("selected");
    });
})