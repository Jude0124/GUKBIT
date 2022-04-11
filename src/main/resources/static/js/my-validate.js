//키보드 입력이 있을 경우 validation을 체크하여 input-box와 validate-text의 색과 display를 결정 함
function doIfElse(firstUpperValue, value, String) {
	if ($('#input' + firstUpperValue).val() === '') {
		$('.feedback-' + value)
			.css('display', 'block')
			.css('color', 'red')
			.text(String + '을(를) 입력해 주세요');
		$('#input' + firstUpperValue)
			.css('border', '2px solid red')
			.css('border-radius', '5px');
	} else {
		$('.feedback-' + value).css('display', 'none');
		$('#input' + firstUpperValue)
			.css('border', '2px solid green')
			.css('border-radius', '5px');
	}
}

var feedbackIdInput = function () {
	doIfElse('Id', 'id', '아이디');
};

var feedbackPwInput = function () {
	doIfElse('Pw', 'pw', '비밀번호');
};
var feedbackPwCheckInput = function () {
	doIfElse('PwCheck', 'pwcheck', '비밀번호 확인');
};
var feedbackEmailInput = function () {
	doIfElse('Email', 'email', '이메일');
};
var feedbackTelInput = function () {
	doIfElse('Tel', 'tel', '휴대전화 번호');
};

$('#signUp-form').on('submit', function (evt) {
	evt.preventDefault();

	var id = $('#inputId').val();
	var pw = $('#inputPw').val();
	var pwcheck = $('#inputPwCheck').val();
	var email = $('#inputEmail').val();
	var tel = $('#inputTel').val();
	var flag = 0; //flag를 사용해 or연산으로 option체크

	if (id === '') {
		$('.feedback-id').css('display', 'block').css('color', 'red').text('아이디를 입력해 주세요');
		$('#inputId').css('border', '2px solid red').css('border-radius', '5px');
	} else {
		flag = flag | 1;
		$('#inputId').css('border', '2px solid green').css('border-radius', '5px');
	}
	if (pw === '') {
		$('.feedback-pw').css('display', 'block').css('color', 'red').text('비밀번호를 입력해 주세요');
		$('#inputPw').css('border', '2px solid red').css('border-radius', '5px');
	} else {
		flag = flag | 2;
		$('#inputPw').css('border', '2px solid green').css('border-radius', '5px');
	}
	if (pwcheck === '') {
		$('.feedback-pwcheck').css('display', 'block').css('color', 'red').text('비밀번호 확인을 입력해 주세요');
		$('#inputPwCheck').css('border', '2px solid red').css('border-radius', '5px');
	} else {
		flag = flag | 4;
		$('#inputPwCheck').css('border', '2px solid green').css('border-radius', '5px');
	}
	if (email === '') {
		$('.feedback-email').css('display', 'block').css('color', 'red').text('이메일을 입력해 주세요');
		$('#inputEmail').css('border', '2px solid red').css('border-radius', '5px');
	} else {
		flag = flag | 8;
		$('#inputEmail').css('border', '2px solid green').css('border-radius', '5px');
	}
	if (tel === '') {
		$('.feedback-tel').css('display', 'block').css('color', 'red').text('전화번호를 입력해 주세요');
		$('#inputTel').css('border', '2px solid red').css('border-radius', '5px');
	} else {
		flag = flag | 16;
		$('#inputTel').css('border', '2px solid green').css('border-radius', '5px');
	}

	if (pw !== '' && pwcheck !== '') {
		if (pw !== pwcheck) {
			$('#inputPw').css('border', '2px solid red').css('border-radius', '5px');
			$('.feedback-pw').css('display', 'block').css('color', 'red').text('비밀번호가 같지 않습니다.');
			$('#inputPwCheck').css('border', '2px solid red').css('border-radius', '5px');
			$('.feedback-pwcheck').css('display', 'block').css('color', 'red').text('비밀번호가 같지 않습니다.');
		} else {
			flag = flag | 32;
			$('.feedback-pw').css('display', 'none');
			$('.feedback-pwcheck').css('display', 'none');
		}
	}

	console.log(flag);

	if (flag === 63) {
		evt.preventDefault();
		//추후 서버사이드가 완성 될 경우 ajax통신을 통해 회원가입
		/*
        var signUpData = { "name": name, "id": id, "pw": pw, "email": email, "address": address, "phoneNumber": phoneNumber }

		$.ajax({
			type: 'POST',
			url: '/signUp.do',
			contentType: 'application/json',
			data: JSON.stringify(signUpData),
			async: false,
			success: function (result) {
				console.log(result);
				if (result === 'success') {
					location.href = './index.jsp';
					return false;
				} else if (result === 'dup') {
					return false;
				}
			},
			error: (_jqXHR, _status, _error) => {
				alert('error : ' + _error);
			},
		});
        */
	}
});
