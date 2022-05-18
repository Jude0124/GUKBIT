function visibleToggle(bid, visible) {
    let params = {
        bid: bid,
        visible: visible
    };
    $.ajax({
        type: 'POST',
        url: '/admin/visibleToggle',
        dataType: 'json',
        data: JSON.stringify(params),
        contentType: 'application/json',
        success: function (result) {
            location.reload();
            console.log("성공")
        },
        error: function (request, status, error) {
            console.log("실패")
        }
    });
}