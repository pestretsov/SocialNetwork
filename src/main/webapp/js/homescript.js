/**
 * Created by artemypestretsov on 8/6/16.
 */
$(function () {

    var sessionUser = {
        id: $("#sessionUserFirstName").data("user-id"),
        username: $("#requestUserUsername").text()
    };

    var postsContainer = document.getElementById("posts");

    var offsetPostId = 100000000;

    function displayDate(timestamp) {
        var now = new Date();
        var nowWrapper = moment(now);
        var pastDateWrapper = moment.unix(timestamp);
        return pastDateWrapper.from(nowWrapper);
    }

    function toPlainText(string) {
        return string.replace(/</g, "&lt;").replace(/>/g, "&gt;");
    }

    function addPostView(postView) {
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

        var post = document.createElement("div");
        post.className = "post panel row";

        post.setAttribute("data-post-id", postView.postId);
        post.innerHTML += '<div class="col-md-1"><img class="user-avatar" src="/images/artemy.jpg"></div>';

        var postContent = document.createElement("div");
        postContent.className = "col-md-11";

        postContent.innerHTML += '<h3>' + postView.fromFirstName + ' ' + postView.fromLastName + ' <span>@' + postView.fromUsername + '</span><span> &bull; </span><span>' + displayDate(postView.postPublishTime.epochSecond) + '</span></h3>';
        postContent.innerHTML += '<p>' + toPlainText(postView.postText) + '</p>';
        postContent.innerHTML += '</div>';
        postContent.innerHTML += '</div>';

        var postToolbar = document.createElement("div");

        postToolbar.appendChild(likeButton);
        if (postView.editable) {
            var editButtonsDiv = document.createElement("div");
            editButtonsDiv.className = "pull-right";

            var removeButton = document.createElement("span");
            var editButton = document.createElement("span");
            var editOkButton = document.createElement("span");

            removeButton.className = "post-remove glyphicon glyphicon-remove-circle";
            editButton.className = "post-edit glyphicon glyphicon-edit";
            editOkButton.className = "hidden post-edit-ok glyphicon glyphicon-ok";

            editButtonsDiv.appendChild(removeButton);
            editButtonsDiv.appendChild(editButton);
            editButtonsDiv.appendChild(editOkButton);

            removeButton.addEventListener('click', function () {
                var postId = $(this).closest('.post').data("post-id");
                $.ajax({
                    url: "/restapi/posts/delete/" + postId,
                    type: "DELETE",
                    dataType: "json",
                    success: $(this).closest('.post').remove()
                });
            });

            // editButtonsDiv.on('click', '.post-edit', function () {
            //     var p = $(this).parent('div').parent('div').parent('div').siblings('div').children('div').children('p');
            //     var save = $(this).siblings('.post-edit-ok');
            //     var edit = $(this);
            //     var text = p.text().replace("\n", "").trim();
            //
            //     p.replaceWith("<textarea class='post-edit-text'>" + text + "</textarea>");
            //
            //     edit.addClass("hidden");
            //     save.removeClass("hidden");
            //
            //     save.off('click').click(function () {
            //         var postId = $(this).closest('.post').data("post-id");
            //         var textArea = $(this).parent('div').parent('div').parent('div').siblings('div').children('div').children("textarea");
            //         var updatedText = textArea.val();
            //
            //         $.ajax({
            //             url: "/restapi/posts/secure",
            //             type: "PUT",
            //             method: "PUT",
            //             headers: {'Content-Type': 'application/json'},
            //             dataType: "json",
            //             data: JSON.stringify({
            //                 id: postId,
            //                 fromId: requestUser.id,
            //                 text: updatedText
            //             }),
            //             success: function () {
            //                 textArea.replaceWith('<p>' + updatedText + '</p>');
            //
            //                 save.addClass("hidden");
            //                 edit.removeClass("hidden");
            //             }
            //         });
            //     });
            // });
            postToolbar.appendChild(editButtonsDiv);
        }

        postContent.appendChild(postToolbar);
        post.appendChild(postContent);

        return post;
    }

    function loadPersonalTimeline(offsetId) {
        $.ajax({
            url: "/restapi/posts/gettimeline",
            data: {
                followerId: sessionUser.id,
                offsetId: offsetId,
                limit: 3
            },
            type: "GET",
            dataType: "json",
            success: function (postViews) {
                postViews.forEach(function (postView) {
                    postsContainer.appendChild(addPostView(postView));
                    offsetId = postView.postId;
                });

                // no more posts
                if (postViews.length == 0) {
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
    // postsContainer.on('click', '.post-edit', function () {
    //     var p = $(this).parent('div').parent('div').siblings('div').children('div').children('p');
    //     var save = $(this).siblings('.post-edit-ok');
    //     var edit = $(this);
    //     var text = p.text().replace("\n", "").trim();
    //
    //     p.replaceWith("<textarea class='post-edit-text'>" + text + "</textarea>");
    //
    //     edit.addClass("hidden");
    //     save.removeClass("hidden");
    //
    //     save.off('click').click(function () {
    //         var postId = $(this).closest('.post').data("post-id");
    //         var textArea = $(this).parent('div').parent('div').siblings('div').children('div').children("textarea");
    //         var updatedText = textArea.val();
    //         var currentTime = new Date().toISOString();
    //
    //         $.ajax({
    //             url: "/restapi/posts/secure",
    //             type: "PUT",
    //             method: "PUT",
    //             headers: {'Content-Type': 'application/json'},
    //             dataType: "json",
    //             data: JSON.stringify({
    //                 id: postId,
    //                 fromId: sessionUser.id,
    //                 postType: "DEFAULT",
    //                 text: updatedText,
    //                 publishTime: currentTime
    //             }),
    //             success: function () {
    //                 textArea.replaceWith('<p>' + updatedText + '</p>');
    //
    //                 save.addClass("hidden");
    //                 edit.removeClass("hidden");
    //             }
    //         });
    //     });
    // });
});
