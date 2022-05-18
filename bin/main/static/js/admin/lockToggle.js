function lockToggle(event, userLock) {
    let userId = $(event.target).prev().val();
    let params = {
        userLock: userLock,
        userId: userId
    };
    $.ajax({
        type: 'POST',
        url: '/admin/lockToggle',
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