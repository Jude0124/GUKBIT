$(document).ready(function( $ ){ 


    var location_click = 0;
    var data_list;
    // var list = document.getElementById('academyPrint');

    $(".compareSearchClick1").on("click", function(event){
        $('#academySearchModal').show();
        $(".academySearchPrint1").show();
        $(".academySearchPrint2").hide();

        $("body").append('<div class="backon"></div>');
        location_click = 1;
    });

    $(".compareSearchClick2").on("click", function(event){
        $('#academySearchModal').show();
        $(".academySearchPrint1").show();
        $(".academySearchPrint2").hide();
        $("body").append('<div class="backon"></div>');
        location_click = 2;
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


    function academy_data (academy_name){
        $.ajax({
            url : "/academy/compare/search",
            type : 'POST',
            data : {academyName : academy_name},
            success : function(data) {
                data_list = data;
                academy_list_modal(data);

            },
        });
    }

    $("#searchClick").on("click", function(event) {
        let academy_name_val = $('#academySearchBox').val();
        console.log(academy_name_val);
        academy_data(academy_name_val);
    });




    function academy_list_modal(data){
        $(".academySearchPrint1").show();
        $(".academySearchPrint2").hide();

        if(data[0]!=null){

            var dataTemp = `<tr> <th>학원명</th> <th>주소</th></tr>`;
            for(var i=0; i<data.length; i++){
                dataTemp += `<tr <!--onclick="location.href='/academy/review?code=`+data[i].code +`'"--> > <td>`+ data[i].name +`</td> <td>`+ data[i].region +`</td></tr>`;
            }
            $(".academySearchPrint2").show();
            $(".academySearchPrint1").hide();
            $('.academySearchTable').html(dataTemp);
        } else {
        }

    }


    function academy_list_check(){
        if(location_click == 0)
        {
            $(".compareSearchClick1").show();
            $(".compareSearchClick2").show();
            $(".compareNum1").hide();
            $(".compareNum2").hide();
        } else {
            if(location_click == 1) {

                console.log(data.length);
                $(".compareSearchClick1").hide();
                $(".compareNum1").show();
            }
            if(location_click == 2) {
                
                $(".compareSearchClick2").hide();
                $(".compareNum2").show();
            }
        }

    }


});

