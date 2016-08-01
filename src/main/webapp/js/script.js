$(function () {

    var requestUser = {
        id:       $("#requestUserUsername").data("user-id"),
        username: $("#requestUserUsername").text(),
        fullName: $("#requestUserFullName").text()
    };

    var postsContainer = $("#posts");

    var offsetPostId = 100000000;

    function loadPosts(user, offsetId) {
        $.ajax({
            url: "/restapi/posts",
            data: {
                fromId: user.id,
                offsetId: offsetId, // --> offsetId = inf ; ... ; in java tackle this case
                limit: 3
            },
            type: "GET",
            dataType: "json",
            success: function (posts) {
                posts.forEach(function (post) {
                    var prepareHtml = '<div class="row post panel" data-post-id="'+ post.id +'">';

                    prepareHtml += '<div class="col-md-1">';
                    prepareHtml += '<img class="user-avatar" src="images/artemy.jpg">';
                    prepareHtml += '</div>';

                    prepareHtml += '<div class="col-md-11">';
                    prepareHtml += '<h3>'+user.fullName +' <span>' + user.username + '</span>'+
                                            '<span>;</span><span>' + "12312312" + '</span> </h3>';
                    prepareHtml += '<p>' + post.text + '</p>';
                    prepareHtml += '<span class="post-remove glyphicon glyphicon-remove-circle"></span>';
                    prepareHtml += '<span class="post-edit glyphicon glyphicon-edit"></span>';
                    prepareHtml += '<span class="post-edit-ok glyphicon glyphicon-ok"></span>';
                    prepareHtml += '</div>';

                    prepareHtml += '</div>';

                    postsContainer.append(prepareHtml);
                    offsetPostId = post.id;
                });
            }
        });
    }

    $.when(loadPosts(requestUser, offsetPostId)).then(function(){
        console.log("here one");
        $(window).scroll(function () {
            if($(window).scrollTop() == $(document).height() - $(window).height()) {
                loadPosts(requestUser, offsetPostId);
            }
        });
    });

    postsContainer.on('click', '.post-remove', function () {
        var postId = $(this).closest('.post').data("post-id");

        $.ajax({
            url: "/restapi/posts/" + postId,
            type: "DELETE",
            dataType: "json",
            success: $(this).closest('.post').remove()
        });
    });

    postsContainer.on('click', '.post-edit', function () {
        var width = $("p").css('width');
        var p = $(this).siblings('p');

        var text = p.text().replace("\n", "").trim();
        console.log(text);
        p.replaceWith("<textarea class='edit'>" + text + "</textarea>");
        $(".edit").css("width", width);


        var save = $(this).siblings('.post-edit-ok');
        // $(this).hide();
        // save.show();

        save.click(function () {
            console.log("click");
            var postId = $(this).closest('.post').data("post-id");
            var textArea = $(this).siblings("textarea");
            var updatedText = textArea.val();

            $.ajax({
                url: "/restapi/posts",
                type: "PUT",
                method: "PUT",
                headers: {'Content-Type' : 'application/json'},
                dataType: "json",
                data: JSON.stringify({
                    id: postId,
                    fromId: requestUser.id,
                    postType: 0,
                    publishTime: Date.now(),
                    text: updatedText
                }),
                success: function() {
                    p.text(updatedText);
                    textArea.replaceWith(p);
                    // $(this).show();
                    // save.hide();
                }
            });
        });
    });
});
