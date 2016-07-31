$(function () {

    var requestUser = {
        id:       $("#requestUserId").text(),
        username: $("#requestUserUsername").text(),
        fullName: $("#requestUserFullName").text()
    };

    var postsContainer = $("#posts");

    $.ajax({
        url: "/restapi/posts",
        data: {
            fromId: requestUser.id,
            offset: 0,
            limit: 10
        },
        type: "GET",
        dataType: "json"
    }).done(function (posts) {

        posts.forEach(function (post) {
            var prepareHtml = '<div class="row post panel">';

            prepareHtml += '<div class="col-md-1">';
            prepareHtml += '<img class="user-avatar" src="images/artemy.jpg">';
            prepareHtml += '</div>';

            prepareHtml += '<div class="col-md-11">';
            prepareHtml += '<h3>'+requestUser.fullName +' <span>' + requestUser.username + '</span>'+
                                    '<span>;</span><span>' + "12312312" + '</span> </h3>';
            prepareHtml += '<p>' + post.text + '</p>';
            prepareHtml += '<span class="glyphicon glyphicon-star-empty"></span>';
            prepareHtml += '</div>';

            prepareHtml += '</div>';

            postsContainer.append(prepareHtml);
        });
    });
});
