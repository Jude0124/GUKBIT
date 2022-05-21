function boardDelete(bid) {
    let params = {
        bid: bid
    };
    $.ajax({
        type: 'POST',
        url: '/admin/boardDelete',
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