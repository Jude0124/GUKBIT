$(document).ready(function () {
    $('#pwSubmit').on('click', function (e) {
        // 비밀번호 유효성 검사
        let password = document.getElementById('password').value;
        let passwordVal = document.getElementById('passwordVal').value;
        let checkPattern = RegExp(/^[A-Za-z0-9_\-]{6,13}$/);
        if (password.length == 0) {
            e.preventDefault();
            Swal.fire({
                icon: 'warning',
                text: '비밀번호를 입력해주세요',
                confirmButtonColor: '#0059ab',
                confirmButtonText: '확인'
            })
        } else if (password !== passwordVal) {
            e.preventDefault();
            Swal.fire({
                icon: 'warning',
                text: '동일한 비밀번호를 입력해주세요',
                confirmButtonColor: '#0059ab',
                confirmButtonText: '확인'
            })
        } else if (!(checkPattern.test(password))) {
            e.preventDefault();
            Swal.fire({
                icon: 'warning',
                text: '비밀번호는 6자 이상 12자 이하의 영문과 숫자 조합으로 입력해주세요',
                confirmButtonColor: '#0059ab',
                confirmButtonText: '확인'
            })
        }
    })
})