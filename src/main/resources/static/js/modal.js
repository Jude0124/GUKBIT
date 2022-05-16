$(function () {
	// 모달 버튼 클릭시 모달 실행
	$('#tab2').click(function () {
		$('.modal').fadeIn();
	});

	// 모달 닫기
	$('.modalClose').click(function () {
		$('.modal').fadeOut();
	});

	$('#modalSearch_start').on('click', function (event) {
		$('.academyList').remove();
		event.preventDefault();
		let searchValue = document.getElementById('modalSearch_academy').value.trim();
		if (searchValue.length > 0) {
			$.ajax({
				type: 'post',
				data: { SearchValue: searchValue },
				url: '/board/modal',
				success: function (result) {
					if (result.length == 0) {
						Swal.fire({
							icon: 'info',
							title: '조건에 맞는 학원이<br>존재하지 않습니다.',
							text:'검색하실 학원 이름을 한번 더 확인해주세요.',
							confirmButtonColor: '#0059ab',
							confirmButtonText: '확인'
						})
					} else {
						for (var i = 0; i < result.length; i++) {
							addRow =
								'<tr class="academyList">' +
								'<td>' +
								'<a href="/board/list?academyCode=' +
								result[i].code +
								'">' +
								result[i].name +
								'</a>' +
								'</td>' +
								'<td>' +
								result[i].region +
								'</td>' +
								'</tr>';
							$('#academyTableHead').append(addRow);
						}
					}
				},
				error: function (request, status, error) {
					alert('code:' + request.status + '\n' + 'message:' + request.responseText + '\n' + 'error:' + error);
				},
			});
		} else {
			// alert('검색어를 입력해주세요.');
			Swal.fire({
				icon: 'info',
				text: '검색어를 입력해주세요.',
				confirmButtonColor: '#0059ab',
				confirmButtonText: '확인'
			})
		}
	});
	// input창에서 enter 눌러도 검색실행
	$('#modalSearch_academy').on('keypress', function (event) {
		if (event.keyCode == 13) {
			$('#modalSearch_start').click();
			event.preventDefault();
		}
	});
});
