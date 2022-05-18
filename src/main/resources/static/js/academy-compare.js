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
        $("#academySearchBox").val('');
        view_Modal(1);
        location_click = 1;
    });

    /* 학원 2 영역 -> 모달 */
    $(".compareSearchClick2").on("click", function(event){
        view_academy_search_print(1,0);
        $("#academySearchBox").val('');
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
    let ncs = ncs_count_data(data, true);

    htmlTemp += `<div class="academy_button"> <img onClick="javascript:re_find_academy(`+ location_click +`)" src="../images/manage_search_black_24dp.svg" > <img OnClick="location.href ='/academy/review?code=`+ academy.code + `'" src="../images/home_black_24dp.svg" > </div>`;
    htmlTemp += `<div class="contents" style="margin: 0 0 10px 0"> <img src="../images/domain_FILL0_wght400_GRAD0_opsz48.svg"  style=" height : 24px; width: 24px; "> 학원 정보 </div>`;
    htmlTemp += `<div class="academy_compare_info">
                 <div><img src="../images/academy/` + academy.imageUrl + `" style = "width:180px; height:120px;" ></div>
                 <div>
                    <div style="font-size: 22px; font-weight: bold">` + academy.name + `</div>
                    <div>` + academy.addr + `</div>
                    <div style="font-size: 18px;">` + star_maker(academy.eval) + ` (` + academy.eval.toFixed(1) +`)` + `</div>`;
    htmlTemp += `</div> </div>`;


    htmlTemp += `<div class="contents"> <img src="../images/assessment_black_24.svg"  style=" height : 24px; width: 24px; "> 분야별 리뷰 점수 </div>`;
    htmlTemp += `<canvas id="marksChart` + location_click + `" width="600" height="400" ></canvas>`


    htmlTemp += `<div class="contents"> <img src="../images/emoji_events_black_24.svg"  style=" height : 24px; width: 24px;"> TOP3. Best & Worst 리뷰</div>`;
    htmlTemp += `<div class="bwReview">` ;
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
            htmlTemp += `<div> <span>` + star_maker(avgEval) + `</span><span>  (` + avgEval + `)   </span> <span>` +  rates[i].oneStatement + `</span> </div>`;
        }
    } else {
        htmlTemp += `<div> 리뷰 수가 적어 표시할 수 없습니다. </div>`;
    }

    htmlTemp += `</div>`; // bwReview 닫기

    htmlTemp += `<div class="contents"> <img src="../images/emoji_events_black_24.svg"  style=" height : 24px; width: 24px;"> TOP5. 강의 NCS 순위</div>`;
    htmlTemp += `<div class="ncs_rank">` ;
    for(let i=0; i<5; i++){

    htmlTemp +=`<div> <span>`+ (i+1) +`. </span><span>`+ ncs[i].ncsName +`</span> <span>`+ ncs[i].count +`회</span></div>`;
    }
    htmlTemp += `</div>`;

    return htmlTemp;
}

function ncs_count_data(data, sortCheck){
    let course = data["course"];
    let divisions = data["divisions"];
    let listTemp = [];
    let listSort = [];

    for(let i=0; i<divisions.length; i++){
        listTemp[i] = { ncsCode : divisions[i].dfieldS , ncsName: divisions[i].div, count : 0 }
    }

    for(let i=0; i<course.length; i++){
        for(let j=0; j<listTemp.length; j++){
            if(listTemp[j].ncsCode == course[i].fieldS ){
                listTemp[j].count += 1;
            }
        }
    }

    if(sortCheck) {
        listSort = listTemp.sort(function (a, b) {
            // DESC 5->1
            return b.count - a.count;

            // DESC 1->5
            //return a.count-b.count;
        });
        return listSort;
    } else {
        return listTemp;
    }



}


/* 차트 입력 영역 */
function chart(avg, best, worst, location){
    let marksCanvas = document.getElementById("marksChart" + location +"");

    let marksData = {
        labels: ["강사진", "커리큘럼", "취업연계", "학원 내 문화", "운영 및 시설"],
        datasets: [{
            label : "평균 점수",
            borderColor: 'rgba(255,222,62)',
            backgroundColor: "rgba(255,222,62,0.1)",
            pointBackgroundColor: 'rgba(255,222,62)',
            pointBorderColor: "#fff",
            data: [avg.lecturersEval, avg.curriculumEval, avg.employmentEval, avg.cultureEval, avg.facilityEval]
         },{
            label : "최고 점수",
            borderColor: "rgba(72,92,250)",
            backgroundColor: "rgba(72,92,250,0.1)",
            pointBackgroundColor: 'rgba(72,92,250)',
            pointBorderColor: "#fff",
            data: [best.lecturersEval, best.curriculumEval, best.employmentEval, best.cultureEval, best.facilityEval]
        },{
            label : "최저 점수",
            borderColor: 'rgba(224,69,49)',
            backgroundColor: "rgba(224,69,49,0.1)",
            pointBackgroundColor: 'rgba(224,69,49)',
            pointBorderColor: "#fff",
            data: [worst.lecturersEval, worst.curriculumEval, worst.employmentEval, worst.cultureEval, worst.facilityEval]
        }]
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
                fontSize:14,
                fontColor: "black"
            },
        },

        legend: {
            display: true,
            labels: {
                boxWidth: 10,
                boxHeight: 50000000, // fontSize에 비례함
                fontSize: 14,
                fontColor: '#263238',
                generateLabels: function (chart) {
                    const labels = Chart.defaults.global.legend.labels.generateLabels(chart);
                    return labels.map(property => {
                        return {...property, fillStyle: property.strokeStyle};
                    });
                },
            },
            position: 'bottom',

        },

        elements: {
            line: {
                borderWidth: 1.5
            }
        },

        title: {
            display: true,
            text: '  '
        },

        responsive: false,
        tooltips: {
            enabled: false
        },
        hover: {
            animationDuration: 0
        },
        /*
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
        */

    }

    let radarChart = new Chart(marksCanvas, {
        type: 'radar',
        data: marksData,
        options: chartOptions
    });
}
/* 차트 값 생성 영역 */
function chart_data(data){
    /* 차트 값 생성 영역 */
    let rates = data["rate"];
    let eval = [];
    for(let k=0; k<5; k++){
        eval[k]=0;
    }

    for(let i=0; i<rates.length; i++){
        eval[0] += rates[i].lecturersEval;
        eval[1] += rates[i].curriculumEval;
        eval[2] += rates[i].employmentEval;
        eval[3] += rates[i].cultureEval;
        eval[4] += rates[i].facilityEval;
    }

    for(let j=0; j<eval.length; j++){
        eval[j] = (eval[j]/rates.length).toFixed(1);

    }
    let list_eval= [];
        list_eval[0] = {type : "avg" , lecturersEval: eval[0], curriculumEval: eval[1], employmentEval: eval[2], cultureEval: eval[3], facilityEval: eval[4] };
        try {
            list_eval[1] = {
                type: "best",
                lecturersEval: rates[0].lecturersEval,
                curriculumEval: rates[0].curriculumEval,
                employmentEval: rates[0].employmentEval,
                cultureEval: rates[0].cultureEval,
                facilityEval: rates[0].facilityEval
            };
        } catch {
            list_eval[1] = {
                type: "best",
                lecturersEval: 0,
                curriculumEval:0,
                employmentEval: 0,
                cultureEval: 0,
                facilityEval: 0
            };
        }

        try {
            if(rates.length > 1) {
                list_eval[2] = {
                    type: "worst",
                    lecturersEval: rates[rates.length - 1].lecturersEval,
                    curriculumEval: rates[rates.length - 1].curriculumEval,
                    employmentEval: rates[rates.length - 1].employmentEval,
                    cultureEval: rates[rates.length - 1].cultureEval,
                    facilityEval: rates[rates.length - 1].facilityEval
                };
            } else {
                throw new Error();
            }
        } catch {
                list_eval[2] = {
                    type: "worst",
                    lecturersEval: 0,
                    curriculumEval:0,
                    employmentEval: 0,
                    cultureEval: 0,
                    facilityEval: 0

            }
        }

     return list_eval;
    /* ** 차트 값 생성 종료 ** */

}


/* 학원 다시 검색할 때 사용됨 */
function re_find_academy(lc){
    view_academy_search_print(1,0);
    $("#academySearchBox").val('');
    view_Modal(1);
    location_click = lc;
}


/* 학원 HTML 값 반영 및 출력 영역 */
function academy_data_view(data){
    let htmlTemp = academy_data(data);
    let list_eval = chart_data(data);

    /* ** 차트 값 생성 종료 ** */


    if(location_click==1)
    {
        academy_list_check();
        $('.compareNum1').html(htmlTemp);
        chart(list_eval[0], list_eval[1], list_eval[2],location_click);

    } else if(location_click ==2){
        academy_list_check();
        $('.compareNum2').html(htmlTemp);
        chart(list_eval[0], list_eval[1], list_eval[2],location_click);
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


