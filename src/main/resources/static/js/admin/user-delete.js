function userDelete(event) {
    let userId = $(event.target).prev().val();
    let params = {
        userId: userId
    };
    $.ajax({
        type: 'POST',
        url: '/admin/userDelete',
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