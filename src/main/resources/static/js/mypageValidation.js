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

	$('.userInfo').submit(function (e) {
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
			// if (
			// 	confirm(
			// 		'비밀번호와 과정을 수정합니다. 정말 수정하시겠습니까? ※과정을 수정하시면 이전에 작성했던 평가와 별점이 삭제됩니다.',
			// 	)
			// ) {
			// 	return true;
			// }
			Swal.fire({
				title: '비밀번호와 과정을 수정합니다.<br>정말 수정하시겠습니까?',
				text: "※과정을 수정하시면 이전에 작성했던 평가와 별점이 삭제됩니다.",
				icon: 'warning',
				showCancelButton: true,
				confirmButtonColor: '#0059ab',
				cancelButtonColor: '#D5D5D5',
				confirmButtonText: '확인',
				cancelButtonText: '취소'
			}).then((result) => {
				if (result.isConfirmed) {
					$(this).unbind();
					$(this).submit();
					return true;
				}
			})

			return false;
		} else if (formCheck && pwCheck === 3) {
			// if (
			// 	confirm(
			// 		'과정을 수정합니다. 정말 수정하시겠습니까? ※과정을 수정하시면 이전에 작성했던 평가와 별점이 삭제됩니다.',
			// 	)
			// ) {
			// 	return true;
			// }
			Swal.fire({
				title: '과정을 수정합니다.<br>정말 수정하시겠습니까?',
				text: "※과정을 수정하시면 이전에 작성했던 평가와 별점이 삭제됩니다.",
				icon: 'warning',
				showCancelButton: true,
				confirmButtonColor: '#0059ab',
				cancelButtonColor: '#D5D5D5',
				confirmButtonText: '확인',
				cancelButtonText: '취소'
			}).then((result) => {
				if (result.isConfirmed) {
					$(this).unbind();
					$(this).submit();
					return true;
				}
			})
			return false;
		}

		if (pwCheck === 2) {
			// if (confirm('비밀번호를 수정하시겠습니까?')) {
			// 	return true;
			// }
			Swal.fire({
				title: '비밀번호를 수정하시겠습니까?',
				icon: 'question',
				showCancelButton: true,
				confirmButtonColor: '#0059ab',
				cancelButtonColor: '#D5D5D5',
				confirmButtonText: '확인',
				cancelButtonText: '취소'
			}).then((result) => {
				if (result.isConfirmed) {
					$(this).unbind();
					$(this).submit();
					return true;
				}
			})
		}

		if (formCheck === 0 && pwCheck === 3) {
			e.preventDefault();
			// alert('수정된 항목이 없습니다.');
			Swal.fire({
				icon: 'info',
				text: '수정된 항목이 없습니다',
				confirmButtonColor: '#0059ab',
				confirmButtonText: '확인'
			})

			return false;
		}

		if (pwCheck < 2) {
			// alert('비밀번호는 4자이상 16자 미만의 영문과 숫자가 가능합니다.');
			e.preventDefault();
			Swal.fire({
				icon: 'warning',
				html: '비밀번호는 4자 이상 16자 미만의<br>영문과 숫자 조합으로 입력해주세요.',
				confirmButtonColor: '#0059ab',
				confirmButtonText: '확인'
			})
			return false;
		}
		return false;
	}); // mypage 유효성 검사 끝

	/* 평가 수정/삭제 버튼 클릭 시 */
	$('#authCourseRewrite').click(function (e) {
		let code = $('#academy-code').val();
		if ($('#authCourse').val() === '') {
			alert('과정 인증을 진행하지 않았습니다.');
			$('#courseId').focus();
			e.preventDefault();
			return false;
		}
		if ($('#comment').val() === '') {
			if (confirm('인증한 과정 평가를 작성하지 않았습니다. 학원 리뷰 페이지로 이동하시겠습니까?')) {
				e.preventDefault();
				location.href = '/academy/review?code=' + code;
				return true;
			}
			e.preventDefault();
			return false;
		}
		alert('평가 수정/삭제 페이지로 이동합니다.');

	});	// click이벤트 끝 


});
