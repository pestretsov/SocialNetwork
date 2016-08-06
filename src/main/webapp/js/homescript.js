/**
 * Created by artemypestretsov on 8/6/16.
 */
$(function () {

    var sessionUser = {
        id: $("#sessionUserFirstName").data("user-id"),
        username: $("#requestUserUsername").text()
    };

    var postsContainer = $("#posts");

    var offsetPostId = 100000000;

    function epochToDate(utcSeconds) {
        var d = new Date(0); // The 0 there is the key, which sets the date to the epoch
        d.setUTCSeconds(utcSeconds);
        return d;
    }

    function toPlainText(string) {
        return string.replace(/</g, "&lt;").replace(/>/g, "&gt;");
    }

    function loadPersonalTimeline(offsetId) {
        $.ajax({
            url: "/restapi/posts/secure",
            data: {
                followerId: sessionUser.id,
                offsetId: offsetId,
                limit: 3
            },
            type: "GET",
            dataType: "json",
            success: function (posts) {
                posts.forEach(function (post) {

                    var user = post.from;

                    var now = new Date();
                    var nowWrapper = moment(now);
                    console.log(post.publishTime.epochSecond);
                    console.log(now);
                    var pastDateWrapper = moment.unix(post.publishTime.epochSecond);
                    var displayDate = pastDateWrapper.from(nowWrapper);

                    var prepareHtml = '<div class="row">';
                    prepareHtml += '<div class="panel post" data-post-id="'+ post.id +'">';

                    prepareHtml += '<div class="col-md-1">';
                    prepareHtml += '<img class="user-avatar" src="images/artemy.jpg">';
                    prepareHtml += '</div>';

                    prepareHtml += '<div class="col-md-11">';

                    prepareHtml += '<div class="row">';
                    prepareHtml += '<div class="col-md-12">';
                    prepareHtml += '<h3>'+user.firstName + " " + user.lastName +' <span>' + user.username + '</span>'+
                        '<span> &bull; </span><span>' + displayDate + '</span> </h3>';
                    prepareHtml += '<p>' + toPlainText(post.text) + '</p>';
                    prepareHtml += '</div>';
                    prepareHtml += '</div>';

                    if (user.id == sessionUser.id) {
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
                    offsetId = post.id;
                });

                // no more posts
                if (posts.length == 0) {
                    $(window).off('scroll');
                } else {
                    $(window).off('scroll').scroll(function () {
                        if ($(window).scrollTop() == $(document).height() - $(window).height()) {
                            loadPersonalTimeline(offsetId);
                        }
                    });
                }
            }
        });
    }

    loadPersonalTimeline(offsetPostId);

    postsContainer.on('click', '.post-remove', function () {
        var postId = $(this).closest('.post').data("post-id");

        $.ajax({
            url: "/restapi/posts/secure" + postId,
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
            var currentTime = new Date().toISOString();

            $.ajax({
                url: "/restapi/posts/secure",
                type: "PUT",
                method: "PUT",
                headers: {'Content-Type': 'application/json'},
                dataType: "json",
                data: JSON.stringify({
                    id: postId,
                    fromId: sessionUser.id,
                    postType: 0,
                    text: updatedText,
                    publishTime: currentTime
                }),
                success: function () {
                    textArea.replaceWith('<p>' + updatedText + '</p>');

                    save.addClass("hidden");
                    edit.removeClass("hidden");
                }
            });
        });
    });
});
