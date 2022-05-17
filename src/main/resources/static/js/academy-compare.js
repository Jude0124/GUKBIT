/* 학원 비교에서의 영역 위치 체크*/
var location_click = 0;

/* 학원 검색 후 학원을 클릭했을 때 학원의 모든데이터 저장되는 변수 */
var data_list;

$(document).ready(function( $ ){
    view_Modal(0);

    /* 학원 검색 후 검색된 학원 리스트가 저장되는 변수 */
    let academy_data_list;

    /* ************************************ 학원비교 페이지 ************************************ */
    /* 학원 1 영역 -> 모달 */
    $(".compareSearchClick1").on("click", function(event){
        view_academy_search_print(1,0);
        view_Modal(1);
        location_click = 1;
    });

    /* 학원 2 영역 -> 모달 */
    $(".compareSearchClick2").on("click", function(event){
        view_academy_search_print(1,0);
        view_Modal(1);
        location_click = 2;
    });

    /* 모달 : CLOSE 버튼 누를 시 모달창 닫기 */
    $(".close").on("click", function(event) {
        view_Modal(0);
    });

    /* 모달 : Body 영역 클릭시 모달창 닫기 */
    $("body").on("click", function(event) { 
        if(event.target.className == 'backon'){
            view_Modal(0);
        }
      });



    /* ************************************ 모달 처리 영역 ************************************ */

    /* 모달 : 검색 Click시 실행되는 영역 */
    $("#searchClick").on("click", function(event) {
        academy_data();
    });

    $("#academySearchBox").keypress(function(e) {
        if(e.keyCode==13)
        {
            academy_data();
        }
    });

    /* 모달 : 검색된 학원 가져오는 Ajax : AcademyController */
    function academy_data(){
        let academy_name_val = $('#academySearchBox').val();
        $.ajax({
            url : "/academy/compare/search",
            type : 'POST',
            data : {academyName : academy_name_val},
            success : function(data) {
                academy_data_list = data;
                academy_list_modal(data);

            },
        });
    }

    /* 모달 :  학원 검색후 Table 형태로 학원 출력해주는 함수 */
    function academy_list_modal(data){
        view_academy_search_print(1,0);

        if(data[0]!=null){

            let dataTemp = `<tr> <th>학원명</th> <th>주소</th></tr>`;
            for(let i=0; i<data.length; i++){
                dataTemp += `<tr onClick="javascript:All_data(` + data[i].code + `)" > <td>`+ data[i].name +`</td> <td>`+ data[i].region +`</td></tr>`;
            }
            // location.href='/academy/rate/compare?code=`+data[i].code +`'
            view_academy_search_print(0,1);
            $('.academySearchTable').html(dataTemp);
        } else {
        }

    }




});




/* 실행 시 바로 실행 외 영역 & SHOW / HIDE 영역 */
/* ************************************ 학원비교 처리 영역 ************************************ */
/* 학원1, 2 영역에 따라 출력되는 영역 변경 */
/* 학원1 : compareSearchClick1 "+" -> compareNum1 변경 "학원정보" */
/* 학원2 : compareSearchClick2 "+" -> compareNum2 변경 "학원정보" */

function academy_list_check(){
    if(location_click == 0)
    {
        view_compare_search_click(1,1);
        $(".compareNum1").hide();
        $(".compareNum2").hide();
    } else {
        if(location_click == 1) {

            $(".compareSearchClick1").hide();
            $(".compareNum1").show();
        }
        if(location_click == 2) {

            $(".compareSearchClick2").hide();
            $(".compareNum2").show();
        }
    }

}


/* SHOW / HIDE 영역 */

function view_Modal(check){
    if(check==0)
    {
        $("#academySearchModal").hide();
        $(".backon").hide();
    } else if (check==1) {
        $("#academySearchModal").show();
        $(".backon").show();
    }
}


function view_compare_search_click(click1, click2){
    if(click1 == 0){
        $(".compareSearchClick1").hide();
    } else if(click1 == 1) {
        $(".compareSearchClick1").show();
    }

    if(click2 == 0){
        $(".compareSearchClick2").hide();
    } else if(click2 == 1) {
        $(".compareSearchClick2").show();
    }
}

function view_academy_search_print(print1, print2){
    if(print1 == 0){
        $(".academySearchPrint1").hide();
    } else if(print1 == 1) {
        $(".academySearchPrint1").show();
    }

    if(print2 == 0){
        $(".academySearchPrint2").hide();
    } else if(print2 == 1) {
        $(".academySearchPrint2").show();
    }
}

/* *************************** 학원 출력 ******************************* */
/* 학원 HTML 생성 영역 */

function academy_data(data){
    let htmlTemp = ``;
    let rates = data["rate"];
    let academy = data["academy"][0];
    let courses = data["course"];

    htmlTemp += `<div><img src="../images/academy/` + academy.imageUrl + `" style = "width:180px; height:120px;" ></div>
                    <div style="font-size: 22px; font-weight: bold">` + academy.name + `</div>
                    <div>` + academy.addr + `</div>
                    <div style="font-size: 18px;">` + star_maker(academy.eval) + ` (` + academy.eval.toFixed(1) +`)` + `</div>`;
    htmlTemp += `</div> <hr>`;


    htmlTemp += `<div style="font-size: 20px; font-weight: bold"> 분야별 평점 </div>`;
    htmlTemp += `<canvas id="marksChart` + location_click + `" width="600" height="400" ></canvas>`

    htmlTemp += `<hr> <div class="bwReview">` ;
    htmlTemp += `<div style="font-size: 20px; font-weight: bold; margin: 10px"> 학원 Best & Worst Top3 리뷰</div>`;
    htmlTemp += `<div class="bwList" style="color : dodgerblue;">- Best </div>`;

    /* 리뷰수 확인 변수 */
    let llc;

    if(rates.length < 3) {
        llc = rates.length;
    } else {
        llc = 3;
    }

    if(llc==0 || llc==null) {
        htmlTemp += `<div> 리뷰 수가 적어 표시할 수 없습니다. </div>`;
    } else {
        for (let i = 0; i < llc; i++) {
            const avgEval = ((rates[i].lecturersEval + rates[i].curriculumEval + rates[i].employmentEval + rates[i].cultureEval + rates[i].facilityEval) / 5);
            htmlTemp += `<div> <span>` + star_maker(avgEval) + `</span><span> (` + avgEval + `) </span> <span>` + rates[i].oneStatement + `</span> </div>`;
        }
    }

    htmlTemp += `<div class="bwList" style="color : orangered;">- Worst </div>`;

    llc=rates.length;

    if(llc<=3 || llc==null) {
        llc = 3;
    } else if(llc<=4) {
        llc = 2;
    } else if(llc<=5) {
        llc = 1;
    } else {
        llc = 0;
    }
    if(llc!=3)
    {
        for(let i=rates.length-1; i>rates.length-(4-llc); i--){
            const avgEval = ((rates[i].lecturersEval + rates[i].curriculumEval + rates[i].employmentEval + rates[i].cultureEval + rates[i].facilityEval)/5);
            htmlTemp += `<div> <span>` + star_maker(avgEval) + `</span><span> (` + avgEval + `) </span> <span>` +  rates[i].oneStatement + `</span> </div>`;
        }
    } else {
        htmlTemp += `<div> 리뷰 수가 적어 표시할 수 없습니다. </div>`;
    }

    htmlTemp += `</div>`; // bwReview 닫기
    htmlTemp += `<div class="">` ;
    htmlTemp += `</div>`;


    htmlTemp += `<div class="academy_button"> <div onClick="javascript:re_find_academy(`+ location_click +`)"> 다른 학원 검색 </div> <div OnClick="location.href ='/academy/review?code=`+ academy.code + `'"> 학원 구경 하기 </div> </div>`;


    return htmlTemp;
}

function ncs_count_data(data){
    let course = data["course"];
    let divisions = data["divisions"];
    console.log(divisions);
    let map = new Map();
    let listTemp = [];
    for(let i=0; i<course.length; i++){
        if(map.has(course[i].fieldS)){
            map.set(course[i].fieldS, map.get(course[i].fieldS)+1);
        } else {
            map.set(course[i].fieldS, 1);
        }
    }
    let count = 0;
    for(let[key,value] of map) {
        count ++;
        console.log(key + "=" + value);
        listTemp[count] = {[key] : value};
    }

    console.log("맵");
    console.log(map);
    console.log("코스");
    console.log(course);

    console.log("임시리스트");
    console.log(listTemp);

}


/* 차트 입력 영역 */
function chart(lec, cur, emp, cul, fac, location){
    let marksCanvas = document.getElementById("marksChart" + location +"");

    let marksData = {
        labels: ["강사진", "커리큘럼", "취업연계", "학원 내 문화", "운영 및 시설"],
        datasets: [{
            backgroundColor: "rgba(200,0,0,0.2)",
            data: [lec, cur, emp, cul, fac]
        }],
    };

    let chartOptions = {
        scale: {
            ticks: {
                beginAtZero : true,
                min: 0,
                max: 5,
                stepSize : 1
            },
            pointLabels:{
                fontSize:13,
                fontColor: "black"
            },
        },
        legend: {
            display: false

        },

        responsive: false,
        tooltips: {
            enabled: false
        },
        hover: {
            animationDuration: 0
        },
        animation: {
            duration: 1,
            onComplete: function () {
                let chartInstance = this.chart,
                    ctx = chartInstance.ctx;
                ctx.font = Chart.helpers.fontString(15, Chart.defaults.global.defaultFontStyle, Chart.defaults.global.defaultFontFamily);
                ctx.fillStyle = 'black';
                ctx.textAlign = 'center';
                ctx.textBaseline = 'bottom';

                this.data.datasets.forEach(function (dataset, i) {
                    let meta = chartInstance.controller.getDatasetMeta(i);
                    meta.data.forEach(function (bar, index) {
                        let data = dataset.data[index];
                        ctx.fillText(data, bar._model.x, bar._model.y - 5);
                    });
                });
            }
        },

    }

    let radarChart = new Chart(marksCanvas, {
        type: 'radar',
        data: marksData,
        options: chartOptions
    });
}
/* 차트 값 생성 영역 */
function chart_data(data){
    ncs_count_data(data);


    /* 차트 값 생성 영역 */
    let rates = data["rate"];
    let eval = [];
    for(let k=0; k<5; k++){
        eval[k]=0;
    }

    for(var i=0; i<rates.length; i++){
        eval[0] += rates[i].lecturersEval;
        eval[1] += rates[i].curriculumEval;
        eval[2] += rates[i].employmentEval;
        eval[3] += rates[i].cultureEval;
        eval[4] += rates[i].facilityEval;
    }

    for(let j=0; j<eval.length; j++){
        eval[j] = (eval[j]/rates.length).toFixed(1);

    }

     return eval;
    /* ** 차트 값 생성 종료 ** */

}


/* 학원 다시 검색할 때 사용됨 */
function re_find_academy(lc){
    view_academy_search_print(1,0);
    view_Modal(1);
    location_click = lc;
}


/* 학원 HTML 값 반영 및 출력 영역 */
function academy_data_view(data){
    let htmlTemp = academy_data(data);
    let eval = chart_data(data);

    /* ** 차트 값 생성 종료 ** */


    if(location_click==1)
    {
        academy_list_check();
        $('.compareNum1').html(htmlTemp);
        chart(eval[0], eval[1], eval[2], eval[3], eval[4],location_click)

    } else if(location_click ==2){
        academy_list_check();
        $('.compareNum2').html(htmlTemp);
        chart(eval[0], eval[1], eval[2], eval[3], eval[4],location_click)
    }
}

/* ************************************ 모달 처리 영역 ************************************ */

function star_maker(eval){
    /* ************** 별점 계산 ************** */
    var star = ``; // 별 출력용 변수
    var ac_eval = eval.toFixed(1); // 별점 변수
    var ac_eval_b = Math.round(ac_eval*10)%10;

    var starSolid = `<img src="../images/star-solid.svg"  style=" height : 17px; width: 17px; ">`;
    var starReqular = `<img src="../images/star-regular.svg" style=" height : 17px; width: 17px; ">`;
    var starHalf = `<img src="../images/star-half-alt-solid.svg" style=" height : 17px; width: 17px; ">`

    for(var star_i=0; star_i < Math.floor(ac_eval); star_i++)
    {
        star += starSolid;
    }

    if(ac_eval_b <= 2 && ac_eval_b!=0){
        star += starReqular;
    }
    else if(ac_eval_b > 2 && ac_eval_b < 8)
    {
        star += starHalf;
    }
    else if(ac_eval_b >= 8){
        star += starSolid;
    }
    for(var star_i=0; star_i < 5-Math.ceil(ac_eval); star_i++)
    {
        star += starReqular;
    }

    return star;
}


/* ************************* 학원값 가져오는 영역 ************************* */

/* 모달->학원비교페이지 : AcademyController */
function All_data(academy_code){
    $.ajax({
        url : "/academy/compare/data",
        type : 'POST',
        data : {code : academy_code},
        success : function(data) {
            view_Modal(0);
            data_list = data;
            academy_data_view(data_list);
        },
    });
}


