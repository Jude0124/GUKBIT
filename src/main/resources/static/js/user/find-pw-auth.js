// 타이머 중복 실행 막기위한 변수
var timer = null;
var isRunning = false;

// 타이머
function startTimer(count, display) {
    var minutes, seconds;
    timer = setInterval(function () {
        minutes = parseInt(count / 60, 10);
        seconds = parseInt(count % 60, 10);

        minutes = minutes < 10 ? "0" + minutes : minutes;
        seconds = seconds < 10 ? "0" + seconds : seconds;

        display.html(minutes + "분" + seconds + "초");

        // 시간종료시
        if (--count < 0) {
            clearInterval(timer);
            Swal.fire({
                icon: 'warning',
                text: '인증코드 유효시간 초과',
                confirmButtonColor: '#0059ab',
                confirmButtonText: '확인'
            })
            // alert("시간초과");
            display.html("시간초과");
            isRunning = false;
        }
    }, 1000);
    isRunning = true;
}

// 전화번호 인증번호 요청
$(function(){
    $('#telAuthReqBtn').on('click', function (event){
        let telValue = document.getElementById('tel').value.trim();
        let idValue = document.getElementById('id').value;
        if(telValue.length == 0) {
            Swal.fire({
                icon: 'warning',
                text: '가입시 사용한 휴대전화 번호를 입력해주세요',
                confirmButtonColor: '#0059ab',
                confirmButtonText: '확인'
            })
        } else if(telValue.length > 0) {
            $.ajax({
                type: 'post',
                url: '/find/telGetCode',
                data: {
                    id: idValue,
                    tel: telValue
                },
                success: function (result) {
                    if (result.length > 6) {
                        // 전송실패 메시지
                        Swal.fire({
                            icon: 'warning',
                            text: '회원정보가 일치하지 않습니다',
                            confirmButtonColor: '#0059ab',
                            confirmButtonText: '확인'
                        })
                    } else {
                        // 전송성공시 성공했다고 alert
                        Swal.fire({
                            icon: 'success',
                            title: '인증번호 발송 성공',
                            text: '인증번호가 성공적으로 발송되었습니다',
                        })

                        var display = $('#telTimer');
                        var leftSec = 300;  // 남은 시간
                        // 이미 타이머가 작동중이면 중지
                        if (isRunning){
                            clearInterval(timer);
                            display.html("");
                            startTimer(leftSec, display);
                        }else{
                            startTimer(leftSec, display);
                        }
                    };
                },
                error: function (request, status, error) {
                    alert('code:' + request.status + '\n' + 'message:' + request.responseText + '\n' + 'error:' + error);
                }
            });
        }
    });
});

// tel 인증번호 유효성 검사
$(document).ready(function () {
    $('#telAuth').on('click', function (e) {
        // 인증번호 유효성 검사
        let inputCodeValue = document.getElementById('telCode').value;
        if (inputCodeValue.length == 0) {
            e.preventDefault();
            Swal.fire({
                icon: 'warning',
                text: '인증번호를 입력해주세요',
                confirmButtonColor: '#0059ab',
                confirmButtonText: '확인'
            })
        } else if (inputCodeValue.length != 6) {
            e.preventDefault();
            Swal.fire({
                icon: 'warning',
                text: '유효하지 않은 코드입니다',
                confirmButtonColor: '#0059ab',
                confirmButtonText: '확인'
            })
        }
    })
})

// email 인증번호 요청
$(function(){
    $('#emailAuthReqBtn').on('click', function (event){
        let emailValue = document.getElementById('email').value.trim();
        let idValue = document.getElementById('id').value;
        if(emailValue.length == 0) {
            Swal.fire({
                icon: 'warning',
                text: '가입시 사용한 이메일 주소를 입력해주세요',
                confirmButtonColor: '#0059ab',
                confirmButtonText: '확인'
            })
        } else if(emailValue.length > 0) {
            $.ajax({
                type: 'post',
                url: '/find/emailGetCode',
                data: {
                    id: idValue,
                    email: emailValue
                },
                success: function (result) {
                    if (result.length > 6) {
                        // 전송실패 메시지
                        Swal.fire({
                            icon: 'warning',
                            text: '회원정보가 일치하지 않습니다',
                            confirmButtonColor: '#0059ab',
                            confirmButtonText: '확인'
                        })
                    } else {
                        // 전송성공시 성공했다고 alert
                        Swal.fire({
                            icon: 'success',
                            title: '인증번호 발송 성공',
                            text: '인증번호가 성공적으로 발송되었습니다',
                        })

                        var display = $('#emailTimer');
                        var leftSec = 300;  // 남은 시간
                        // 이미 타이머가 작동중이면 중지
                        if (isRunning){
                            clearInterval(timer);
                            display.html("");
                            startTimer(leftSec, display);
                        }else{
                            startTimer(leftSec, display);
                        }
                    };
                },
                error: function (request, status, error) {
                    alert('code:' + request.status + '\n' + 'message:' + request.responseText + '\n' + 'error:' + error);
                }
            });
        }
    });
});

// email 인증번호 유효성 검사
$(document).ready(function () {
    $('#emailAuth').on('click', function (e) {
        let inputCodeValue = document.getElementById('emailCode').value;
        if (inputCodeValue.length == 0) {
            e.preventDefault();
            Swal.fire({
                icon: 'warning',
                text: '인증번호를 입력해주세요',
                confirmButtonColor: '#0059ab',
                confirmButtonText: '확인'
            })
        } else if (inputCodeValue.length != 6) {
            e.preventDefault();
            Swal.fire({
                icon: 'warning',
                text: '유효하지 않은 코드입니다',
                confirmButtonColor: '#0059ab',
                confirmButtonText: '확인'
            })
        }
    })
})