/**
 * Created by artemypestretsov on 8/10/16.
 */
$(function () {
    var postContainer = $("#post");
    var commentsContainer = $("#comments");

    function toPlainText(string) {
        return string.replace(/</g, "&lt;").replace(/>/g, "&gt;");
    }

    function displayDate(timestamp) {
        var now = new Date();
        var nowWrapper = moment(now);
        var pastDateWrapper = moment.unix(timestamp);
        return pastDateWrapper.from(nowWrapper);
    }

    var onPostLoad = function(postView) {
        function addLikeButton() {
            var likeButton = document.createElement("div");
            likeButton.className = "post-like";
            var likeGlyph = document.createElement("span");
            likeGlyph.className = "glyphicon glyphicon-heart";

            if (!postView.likable) {
                likeGlyph.className += " post-like-engaged";
            }

            likeButton.appendChild(likeGlyph);
            var likeCount = document.createElement("span");
            if (postView.likeCount > 0) {
                likeCount.innerText = postView.likeCount;
            }
            likeButton.appendChild(likeCount);
            if (sessionUser.id != null) {
                likeButton.addEventListener('click', function () {
                    function onSuccess() {
                        if (postView.likable) {
                            postView.likeCount++;
                        } else {
                            postView.likeCount--;
                        }

                        likeGlyph.className = "glyphicon glyphicon-heart";

                        likeGlyph.className = "glyphicon glyphicon-heart";
                        if (postView.likable) {
                            likeGlyph.className += " post-like-engaged";
                        }

                        if (postView.likeCount != 0) {
                            likeCount.innerText = postView.likeCount;
                        } else {
                            likeCount.innerText = "";
                        }

                        postView.likable = !postView.likable;
                    }

                    if (postView.likable) {
                        $.ajax({
                            url: '/restapi/likes/addlike',
                            headers: {'Content-type': 'application/json'},
                            method: 'POST',
                            data: JSON.stringify({postId: postView.postId}),
                            success: onSuccess()
                        });
                    } else {
                        $.ajax({
                            url: '/restapi/likes/removelike',
                            headers: {'Content-type': 'application/json'},
                            method: 'DELETE',
                            data: JSON.stringify({postId: postView.postId}),
                            success: onSuccess()
                        });
                    }
                });
            }

            return likeButton;
        }

        var prepareHtml = '<div class="row">';
        prepareHtml += '<div class="panel post">';
        prepareHtml += '<div class="col-md-2">';
        prepareHtml += '<img class="user-avatar" src="/images/artemy.jpg">';
        prepareHtml += '</div>';

        prepareHtml += '<div class="col-md-10">';

        prepareHtml += '<h3>' + postView.fromFirstName + ' ' + postView.fromLastName + ' <span>@' + postView.fromUsername + '</span><span> &bull; </span><span>' + displayDate(postView.postPublishTime.epochSecond) + '</span></h3>';
        prepareHtml += '<p>' + toPlainText(postView.postText) + '</p>';

        prepareHtml += '</div>';

        prepareHtml += '</div>';
        prepareHtml += '</div>';

        postContainer.append(prepareHtml);
        console.log(sessionUserId);

        $("#addcomment").click(function (e) {
            e.preventDefault();

            $.ajax({
                type: "POST",
                method: 'POST',
                url: "/restapi/comments/addcomment",
                headers: {'Content-type': 'application/json'},
                data: JSON.stringify({
                    postId: postId,
                    fromId: sessionUserId,
                    text: $("#commenttext").val()
                }),
                success: function () {
                    window.location.reload();
                }
            })
        });
    };

    function loadComments(postId, offsetId, limit) {
        $.ajax({
            url: '/restapi/comments/getcomments',
            method: 'GET',
            data: {
                postId: postId,
                offsetId: offsetId,
                limit: limit
            },
            success: function (comments) {
                comments.forEach(function (comment) {
                    var text = '<p>' + comment.commentText + '</p>';
                    commentsContainer.append(text);
                });
            }
        });
    }

    $.ajax({
        url: '/restapi/posts/getfullpost/' + postId,
        method: 'GET',
        success: onPostLoad
    });
});