function denyAuth(aid,userId) {
    let params = {
        aid: aid,
        userId : userId
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
            }
        },
        error: function (request, status, error) {
        }
    });
}