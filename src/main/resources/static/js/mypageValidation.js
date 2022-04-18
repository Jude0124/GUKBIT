// $(function () {
// 	//비밀번호 유효성검사
// 	$('#password').on('input', function () {
// 		var regex = /^[A-Za-z\d]{8,12}$/;
// 		var result = regex.exec($('#password').val());

// 		if (result != null) {
// 			$('.pw_regex').html('');
// 		} else {
// 			$('.pw_regex').html('영어 대소문자,숫자 8-11자리');
// 			$('.pw_regex').css('color', 'red');
// 		}
// 	});

// 	//비밀번호 확인
// 	$('#pwauth').on('keyup', function () {
// 		if ($('#password').val() == $('#pwauth').val()) {
// 			$('.pwauth_regex').html('비밀번호가 일치합니다');
// 		} else {
// 			$('.pwauth_regex').html('비밀번호가 일치하지않습니다');
// 		}
// 	});
// });

$(document).ready(function () {
	$('.userInfo').submit(function () {
		let check = 0;

		var pwCheck = RegExp(/^[A-Za-z0-9_\-]{3,16}$/);
		if (pwCheck.test($('#changePassword').val())) {
			check++;
		}

		if (pwCheck.test($('#changePasswordCheck').val())) {
			check++;
		}

		if (check === 2) {
			return true;
		}

		alert('비밀번호는 3자이상 16자 미만의 영문과 숫자가 가능합니다.');
		return false;
	});
});
