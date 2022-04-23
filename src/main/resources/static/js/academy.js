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
  		alert('로그인해주세요');
  		evt.preventDefault();
  		return;
  	}
  	/* 로그인 유저가 인증을 하지 않은 경우 authData null */

  	if (authUserAcademyCode == null) {
  		alert('해당 학원의 과정 인증이 필요합니다.');
  		evt.preventDefault();
  		return;
  	} else {	// authUserAcademyCode !=null
  		// 로그인 유저가 인증은 되어있으나 학원코드가 맞지 않는 경우
  		if (authUserAcademyCode !== searchParam('code')) {
  			alert('인증된 학원과 일치하지 않습니다');
  			evt.preventDefault();
  			return;
  		} else {
  			// 로그인 유저가 인증 되어있고 학원코드도 맞는데 rate는 작성하지 않은 경우
  			if(userRateCheck=="true"){
  				if(confirm('이미 리뷰를 작성하셨습니다. 마이페이지로 이동하여 확인하시겠습니까?')){
  					evt.preventDefault()
  					$('#form-academy-go-review-input').submit();
  					return true;
  				}else {
  					evt.preventDefault();
  					return false;
  				}
  			} else {
        }
  		}
  		alert('리뷰 입력 페이지로 이동합니다.');
  	}
  });


})();


/* 카카오 API */

function getParameter(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

var code_value = getParameter("code");

$.ajax({
    url : "/academy/review",
    data: {code : code_value},
    type : 'post',
    success : function(data) {
        map(data);
    },
});

function map(data) {

    var mapContainer = document.getElementById('map'), // 지도를 표시할 div
        mapOption = {
            center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
            level: 2 // 지도의 확대 레벨
        };

    var map = new kakao.maps.Map(mapContainer, mapOption);

    var geocoder = new kakao.maps.services.Geocoder();

    // 주소로 좌표를 검색합니다
    geocoder.addressSearch(data.addr, function(result, status) {



        // 정상적으로 검색이 완료됐으면
        if (status === kakao.maps.services.Status.OK) {

            var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

            // 결과값으로 받은 위치를 마커로 표시합니다
            var marker = new kakao.maps.Marker({
                map: map,
                position: coords
            });
            var addrSlice = data.addr.split('(');

            var contents = `<div style="width:150px;text-align:center;padding:6px 0;">` + addrSlice[0] + `</div>`
            //  '<div style="width:150px;text-align:center;padding:6px 0;"> 학원위치 </div>'
            // 인포윈도우로 장소에 대한 설명을 표시합니다
            var infowindow = new kakao.maps.InfoWindow({
                content: String(contents)
            });
            infowindow.open(map, marker);

            // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
            map.setCenter(coords);
        }
    });
}


