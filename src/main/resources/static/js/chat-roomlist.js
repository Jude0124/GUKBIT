$('#search_academy_btn').on('click', function (event) {
    $('.academyList').remove();
    event.preventDefault();
    let searchValue = document.getElementById('search_academy').value.trim();
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
                    addRow =
                        '<tr class="academyList">' +
                        '<td>' +
                        '학원명' +
                        '</td>' +
                        '<td>' +
                        '학원주소' +
                        '</td>' +
                        '<td>' +
                        '</td>' +
                        '</tr>';
                    $('#academyTableHead').append(addRow);
                    for (var i = 0; i < result.length; i++) {
                        addRow =
                            '<tr class="academyList">' +
                            '<td>' +
                            result[i].name +
                            '</td>' +
                            '<td>' +
                            result[i].region +
                            '</td>' +
                            '<td>' +
                            '<button class="enterBtn" type="button"><a href="/chat/room/' +
                            result[i].code +
                            '">' +
                            "입장" +
                            '</a></button>' +
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
$('#search_academy').on('keypress', function (event) {
    if (event.keyCode == 13) {
        $('#search_academy_btn').click();
        event.preventDefault();
    }
});