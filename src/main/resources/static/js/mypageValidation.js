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
		let pwCheck = 0;
		let formCheck = 0;

		var checkPattern = RegExp(/^[A-Za-z0-9_\-]{4,16}$/);
		if (checkPattern.test($('#changePassword').val())) {
			pwCheck++;
		}

		if (checkPattern.test($('#changePasswordCheck').val())) {
			pwCheck++;
		}

		if ($('#changePasswordCheck').val() === '' && $('#changePassword').val() === '') {
			pwCheck = 3;
		}

		if ($('.form-select option:selected').text() !== '과정명 선택') {
			formCheck = 1;
		}

		if (formCheck && pwCheck === 2) {
			if (
				confirm(
					'비밀번호와 과정을 수정합니다. 정말 수정하시겠습니까? ※과정을 수정하시면 이전에 작성했던 평가와 별점이 삭제됩니다.',
				)
			) {
				return true;
			}
			return false;
		} else if (formCheck && pwCheck === 3) {
			if (
				confirm(
					'과정을 수정합니다. 정말 수정하시겠습니까? ※과정을 수정하시면 이전에 작성했던 평가와 별점이 삭제됩니다.',
				)
			) {
				return true;
			}
			return false;
		}

		if (pwCheck === 2) {
			if (confirm('비밀번호를 수정하시겠습니까?')) {
				return true;
			}
		}

		if (formCheck === 0 && pwCheck === 3) {
			alert('수정된 항목이 없습니다.');
			return false;
		}

		alert('비밀번호는 4자이상 16자 미만의 영문과 숫자가 가능합니다.');
		return false;
	});
});
