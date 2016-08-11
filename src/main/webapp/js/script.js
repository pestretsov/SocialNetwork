$(function () {

    var requestUser = {
        id:       $("#requestUserUsername").data("user-id"),
        username: $("#requestUserUsername").text(),
        fullName: $("#requestUserFullName").text()
    };

    var sessionUser = {
        id: $("#sessionUserFirstName").data("user-id")
    };

    var postContainer = document.getElementById("post");
    var commentsContainer = document.getElementById("comments");
    var postsContainer = document.getElementById("posts");
    var timelineContainer = document.getElementById("timeline");

    var offsetPostId = 100000000;

    function toPlainText(string) {
        return string.replace(/</g, "&lt;").replace(/>/g, "&gt;");
    }

    function displayDate(timestamp) {
        var now = new Date();
        var nowWrapper = moment(now);
        var pastDateWrapper = moment.unix(timestamp);
        return pastDateWrapper.from(nowWrapper);
    }

    function addPostView(postView, isComment, isPostToComment) {
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

        function addCommentsButton() {
            var commentsButton = document.createElement("div");
            commentsButton.className = "post-comment";
            var commentsGlyph = document.createElement("span");
            commentsGlyph.className = "glyphicon glyphicon-comment";
            commentsButton.appendChild(commentsGlyph);

            commentsButton.addEventListener('click', function () {
                window.location.href = "/postcomments/" + postView.postId;
            });

            return commentsButton;
        }

        var post = document.createElement("div");
        post.className = "post panel row";

        if (isComment) {
            post.setAttribute("data-comment-id", postView.commentId);
        } else {
            post.setAttribute("data-post-id", postView.postId);
        }

        post.innerHTML += '<div class="col-md-1"><img class="user-avatar" src="/images/artemy.jpg"></div>';

        var postContent = document.createElement("div");
        postContent.className = "col-md-11";

        var postUserInfo = document.createElement("h3");
        var usernameAncor = document.createElement("a");
        usernameAncor.href = "/user/" + postView.fromUsername;
        usernameAncor.innerHTML = postView.fromFirstName + ' ' + postView.fromLastName;
        postUserInfo.appendChild(usernameAncor);
        postUserInfo.innerHTML += '<span> @' + postView.fromUsername + '</span>';
        if (isComment) {
            postUserInfo.innerHTML += '<span> &bull; </span><span>' + displayDate(postView.commentPublishTime.epochSecond) + '</span>';
        } else {
            postUserInfo.innerHTML += '<span> &bull; </span><span>' + displayDate(postView.postPublishTime.epochSecond) + '</span>';
        }
        postContent.appendChild(postUserInfo);
        if (isComment) {
            postContent.innerHTML += '<p>' + toPlainText(postView.commentText) + '</p>';
        } else {
            postContent.innerHTML += '<p>' + toPlainText(postView.postText) + '</p>';
        }

        if (!isComment && !isPostToComment) {
            var postToolbar = document.createElement("div");

            var interactButtonsDiv = document.createElement("div");
            interactButtonsDiv.className = "pull-left";
            interactButtonsDiv.appendChild(addLikeButton());
            interactButtonsDiv.appendChild(addCommentsButton());

            postToolbar.appendChild(interactButtonsDiv);

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

                postToolbar.appendChild(editButtonsDiv);
            }

            postContent.appendChild(postToolbar);
        }

        post.appendChild(postContent);

        return post;
    }

    function loadUserPosts(user, offsetId) {
        $.ajax({
            url: "/restapi/posts/",
            data: {
                userId: sessionUser.id,
                fromId: user.id,
                offsetId: offsetId,
                limit: 10
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
                        if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight) {
                            loadUserPosts(user, offsetId);
                        }
                    });
                }
            }
        });
    }

    function loadPersonalTimeline(offsetId) {
        $.ajax({
            url: "/restapi/posts/gettimeline",
            data: {
                offsetId: offsetId,
                limit: 10
            },
            type: "GET",
            dataType: "json",
            success: function (postViews) {
                postViews.forEach(function (postView) {
                    timelineContainer.appendChild(addPostView(postView, false));
                    offsetId = postView.postId;
                });

                console.log("remaining="+postViews.length);
                // no more posts
                if (postViews.length == 0) {
                    $(window).off('scroll');
                } else {
                    $(window).off('scroll').scroll(function () {
                        if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight) {
                            loadPersonalTimeline(offsetId);
                        }
                    });
                }
            }
        });
    }

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
                    commentsContainer.appendChild(addPostView(comment, true));
                    offsetId = comment.commentId;
                });

                if (comments.length == 0) {
                    $(window).off('scroll');
                } else {
                    $(window).off('scroll').scroll(function () {
                        if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight) {
                            loadComments(postId, offsetId, 10);
                        }
                    });
                }
            }
        });
    }

    var onPostLoad = function(postView) {
        postContainer.appendChild(addPostView(postView, false, true));
        loadComments(postId, 1000000, 10);

        $("#addcomment").click(function (e) {
            e.preventDefault();

            $.ajax({
                type: "POST",
                method: 'POST',
                url: "/restapi/comments/addcomment",
                headers: {'Content-type': 'application/json'},
                data: JSON.stringify({
                    postId: postId,
                    fromId: sessionUser.id,
                    text: $("#commenttext").val()
                }),
                success: function () {
                    window.location.reload();
                }
            })
        });
    };

    if (postsContainer) {
        loadUserPosts(requestUser, offsetPostId);
    }

    if (timelineContainer) {
        loadPersonalTimeline(offsetPostId);
    }

    if (postContainer && commentsContainer) {
        $.ajax({
            url: '/restapi/posts/getfullpost/' + postId,
            method: 'GET',
            success: onPostLoad
        });
    }
});
