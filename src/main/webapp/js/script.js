$(function () {

    var requestUser = {
        id:       $("#requestUserUsername").data("user-id"),
        username: $("#requestUserUsername").text(),
        fullName: $("#requestUserFullName").text()
    };

    var sessionUser = {
        id: $("#sessionUserFirstName").data("user-id")
    };
    
    function requestUserIsSessionUser(reqUser, sesUser) {
        return sessionUser.id === reqUser.id;
    }

    var postsContainer = $("#posts");

    var offsetPostId = 100000000;

    function toPlainText(string) {
        return string.replace(/</g, "&lt;").replace(/>/g, "&gt;");
    }

    function loadUserPosts(user, offsetId) {
        $.ajax({
            url: "/restapi/posts",
            data: {
                fromId: user.id,
                offsetId: offsetId,
                limit: 3
            },
            type: "GET",
            dataType: "json",
            success: function (postViews) {
                postViews.forEach(function (postView) {
                    var now = new Date();
                    var nowWrapper = moment(now);
                    var pastDateWrapper = moment.unix(postView.postPublishTime.epochSecond);
                    var displayDate = pastDateWrapper.from(nowWrapper);

                    var prepareHtml = '<div class="row">';
                    prepareHtml += '<div class="panel post" data-post-id="'+ postView.postId +'">';

                    prepareHtml += '<div class="col-md-1">';
                        prepareHtml += '<img class="user-avatar" src="/images/artemy.jpg">';
                    prepareHtml += '</div>';

                    prepareHtml += '<div class="col-md-11">';

                        prepareHtml += '<div class="row">';
                            prepareHtml += '<div class="col-md-12">';
                            prepareHtml += '<h3>'+postView.fromFirstName+" "+postView.fromLastName+' <span>@' + postView.fromUsername + '</span>'+
                                                    '<span> &bull; </span><span>' + displayDate + '</span> </h3>';
                            prepareHtml += '<p>' + toPlainText(postView.postText) + '</p>';
                            prepareHtml += '</div>';
                        prepareHtml += '</div>';

                        if (requestUserIsSessionUser(requestUser, sessionUser)) {
                            prepareHtml += '<div class="row">';
                            prepareHtml += '<div class="col-md-12">';

                            prepareHtml += '<span class="post-remove glyphicon glyphicon-remove-circle"></span>';
                            prepareHtml += '<span class="post-edit glyphicon glyphicon-edit"></span>';
                            prepareHtml += '<span class="hidden post-edit-ok glyphicon glyphicon-ok"></span>';

                            prepareHtml += '</div>';
                            prepareHtml += '</div>';
                        }

                    prepareHtml += '</div>';

                    prepareHtml += '</div>';
                    prepareHtml += '</div>';


                    postsContainer.append(prepareHtml);
                    offsetId = postView.postId;
                });

                // no more posts
                if (posts.length == 0) {
                    $(window).off('scroll');
                } else {
                    $(window).off('scroll').scroll(function () {
                        if ($(window).scrollTop() == $(document).height() - $(window).height()) {
                            loadUserPosts(user, offsetId);
                        }
                    });
                }
            }
        });
    }

    loadUserPosts(requestUser, offsetPostId);

    if (requestUserIsSessionUser(requestUser, sessionUser)) {
        postsContainer.on('click', '.post-remove', function () {
            var postId = $(this).closest('.post').data("post-id");

            $.ajax({
                url: "/restapi/posts/secure/" + postId,
                type: "DELETE",
                dataType: "json",
                success: $(this).closest('.post').remove()
            });
        });

        postsContainer.on('click', '.post-edit', function () {
            var p = $(this).parent('div').parent('div').siblings('div').children('div').children('p');
            var save = $(this).siblings('.post-edit-ok');
            var edit = $(this);
            var text = p.text().replace("\n", "").trim();

            p.replaceWith("<textarea class='post-edit-text'>" + text + "</textarea>");

            edit.addClass("hidden");
            save.removeClass("hidden");

            save.off('click').click(function () {
                var postId = $(this).closest('.post').data("post-id");
                var textArea = $(this).parent('div').parent('div').siblings('div').children('div').children("textarea");
                var updatedText = textArea.val();

                $.ajax({
                    url: "/restapi/posts/secure",
                    type: "PUT",
                    method: "PUT",
                    headers: {'Content-Type': 'application/json'},
                    dataType: "json",
                    data: JSON.stringify({
                        id: postId,
                        fromId: requestUser.id,
                        text: updatedText
                    }),
                    success: function () {
                        textArea.replaceWith('<p>' + updatedText + '</p>');

                        save.addClass("hidden");
                        edit.removeClass("hidden");
                    }
                });
            });
        });
    }
});
