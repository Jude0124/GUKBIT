$(document).ready(function( $ ){ 

    $(".compareSearchClick1").on("click", function(event){
        $('#academySearchModal').show();
        $("body").append('<div class="backon"></div>');
    });

    $(".compareSearchClick2").on("click", function(event){
        $('#academySearchModal').show();
        $("body").append('<div class="backon"></div>');
    });
    $(".close").on("click", function(event) {
        $("#academySearchModal").hide(); //close버튼 이거나 뒷배경 클릭시 팝업 삭제
              $(".backon").hide();
    });

    $("body").on("click", function(event) { 
        if(event.target.className == 'backon'){
            $("#academySearchModal").hide(); //close버튼 이거나 뒷배경 클릭시 팝업 삭제
              $(".backon").hide();
        }
      });
    
});