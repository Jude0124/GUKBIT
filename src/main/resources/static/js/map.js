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

