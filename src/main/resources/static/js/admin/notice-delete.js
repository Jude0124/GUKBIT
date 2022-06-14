function noticeDelete(bid) {
    let params = {
        bid: bid
    };
    $.ajax({
        type: 'POST',
        url: '/admin/noticeDelete',
        dataType: 'json',
        data: JSON.stringify(params),
        contentType: 'application/json',
        success: function (result) {
            if (result) {
                location.reload();
            }
        },
        error: function (request, status, error) {
        }
    });
}