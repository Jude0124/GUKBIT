(function () {
  console.log(expectedSelect);

  const academymenu = document.querySelector('.academy_middle');
  var temp;
  if (expectedSelect) {
    $('#acd_review_page').css('display', 'none');
    $('.academy_middle ul li').each(function (index, element) {
      if (element.classList.contains('selected') === true) {
        element.classList.remove('selected');
      } else {
        temp = element;
      }
    });
    select(academymenu, temp);
  } else {
    $('#acd_cour_page').css('display', 'none');
  }

  $('.academy_middle').on('click', function (e) {
    /*  menuWrap.addEventListener('click', e => { */
    //function unselect_removeAtt() {
    $('.academy_middle ul li').each(function (index, element) {
      if (element.classList.contains('selected') === true) {
        element.classList.remove('selected');
      }
    });
    const selected = e.target;
    // unselect_removeAtt(menuWrap);
    select(academymenu, selected);
  });

  function select(ulEl, liEl) {
    Array.from(ulEl.children).forEach((v) => v.classList.remove('selected'));
    if (liEl) {
      liEl.classList.add('selected');
    }
  }

  $('.academy_middle ul li').on('click', function (e) {
    var index = $('.academy_middle ul li').index(this);

    $('#acd_review_page').css('display', 'none');
    $('#acd_cour_page').css('display', 'none');

    switch (index) {
      case 0:
        $('#acd_review_page').css('display', 'block');
        break;

      case 1:
        $('#acd_cour_page').css('display', 'block');
        break;

      default:
        break;
    }
  });

  /* 페이지 쿼리스트링 얻는 함수 */
  function searchParam(key) {
    return new URLSearchParams(location.search).get(key);
  }

  $('#academy-go-review-input').on('click', function (evt) {

    let loginUser = $('#loginUser-id').val();
    let userRateCheck = $('#userRateCheck').val();
    let authUserAcademyCode = $('#loginUser-authinfo').val();
    console.log(userRateCheck)

    /* 로그인 여부 확인 */
    if (loginUser == null) {
      evt.preventDefault();
      Swal.fire({
        title: '로그인이 필요한 기능입니다.',
        text: '로그인페이지로 이동하시겠습니까?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#0059ab',
        cancelButtonColor: '#D5D5D5',
        confirmButtonText: '로그인',
        cancelButtonText: '취소'
      }).then((result) => {
        if (result.isConfirmed) {
          location.href='/login';
        }
      })
    } else {
      /* 로그인 유저가 인증을 하지 않은 경우 authData null */
      if (authUserAcademyCode == null) {
        if (confirm('해당 학원의 과정 인증이 필요합니다. 마이페이지로 이동하시겠습니까?')) {
          evt.preventDefault()
          $('#form-academy-go-review-input').submit();
        }
        evt.preventDefault();
        return false;
      } else {	// authUserAcademyCode !=null
        // 로그인 유저가 인증은 되어있으나 학원코드가 맞지 않는 경우
        if (authUserAcademyCode !== searchParam('code')) {
          alert('인증된 학원과 일치하지 않습니다');
          evt.preventDefault();
          return;
        } else {
          // 로그인 유저가 인증 되어있고 학원코드도 맞는데 rate는 작성하지 않은 경우
          if (userRateCheck == "true") {
            if (confirm('이미 리뷰를 작성하셨습니다. 마이페이지로 이동하여 확인하시겠습니까?')) {
              evt.preventDefault()
              $('#form-academy-go-review-input').submit();
              return true;
            } else {
              evt.preventDefault();
              return false;
            }
          } else {
          }
        }
        alert('리뷰 입력 페이지로 이동합니다.');
      }
    }
    

  }); // onclick 끝

})();


/* 카카오 API */

/* 주소에서 파라미터를 가져옴 */
function getParameter(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

/* 주소 파라미터를 저장 */
var code_value = getParameter("code");

/* 주소 파라미터값 가지고 data를 가져옴 */
$.ajax({
    url : "/academy/review",
    data: {code : code_value},
    type : 'post',
    success : function(data) {
        map(data);
    },
});

/* 가져온 데이터 가지고 map 함수 실행 */
function map(data) {

    var mapContainer = document.getElementById('map'), // 지도를 표시할 div
        mapOption = {
            center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
            level: 2, // 지도의 확대 레벨
            mapTypeId : kakao.maps.MapTypeId.ROADMAP // 맵 유형
        };

    var map = new kakao.maps.Map(mapContainer, mapOption);

    var geocoder = new kakao.maps.services.Geocoder();

    // 주소 '(' 기준으로 배열로 만듦
    var addrSlice = data.addr.split('(');

    /* 주소를 입력받고 그 위치에 있는 좌표를 가져옴 */
    geocoder.addressSearch(addrSlice[0], function(result, status) {

        
        // 검색이 정상으로 됐는지?
        if (status === kakao.maps.services.Status.OK) {
            
            // 해당 주소의 좌표를 저장함
            var coords = new kakao.maps.LatLng(result[0].y, result[0].x);
            
            // 결과값으로 받은 위치를 마커로 표시합니다
            var marker = new kakao.maps.Marker({
                map: map, //마커 표시할 지도 객체
                position: coords // 마커 위치 좌표
            });
            
            /* 장소에 대한 정보를 addr를 반환함 */
            var contents = `<div style="width:150px;text-align:center;padding:6px 0;">` + addrSlice[0] + `</div>`
            // 장소에 대한 설명을 표시합니다.
            var infowindow = new kakao.maps.InfoWindow({
                content: String(contents)
            });
            
            // infowindow를 지도에 표시함
            infowindow.open(map, marker);

            // 좌표를 map의 Center로 반환함
            map.setCenter(coords);
        }
    });
}


