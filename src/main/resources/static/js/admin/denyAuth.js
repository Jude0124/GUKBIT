function denyAuth(aid) {
    let params = {
        aid: aid
    };
    $.ajax({
        type: 'POST',
        url: '/admin/denyAuth',
        dataType: 'json',
        data: JSON.stringify(params),
        contentType: 'application/json',
        success: function (result) {
            if (result) {
                location.reload();
                console.log("성공");
            }
        },
        error: function (request, status, error) {
            console.log("실패")
        }
    });
}