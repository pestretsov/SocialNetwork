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

    function addPostView(postView) {
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

        var post = document.createElement("div");
        post.className = "post panel row";

        post.setAttribute("data-post-id", postView.postId);
        post.innerHTML += '<div class="col-md-1"><img class="user-avatar" src="/images/artemy.jpg"></div>';

        var postContent = document.createElement("div");
        postContent.className = "col-md-11";

        var postUserInfo = document.createElement("h3");
        var usernameAncor = document.createElement("a");
        usernameAncor.href = "/user/" + postView.fromUsername;
        usernameAncor.innerHTML = postView.fromFirstName + ' ' + postView.fromLastName;
        postUserInfo.appendChild(usernameAncor);
        postUserInfo.innerHTML += '<span> @' + postView.fromUsername + '</span>';
        postUserInfo.innerHTML += '<span> &bull; </span><span>' + displayDate(postView.postPublishTime.epochSecond) + '</span>';

        postContent.appendChild(postUserInfo);
        postContent.innerHTML += '<p>' + toPlainText(postView.postText) + '</p>';

        var postToolbar = document.createElement("div");

        postToolbar.appendChild(addLikeButton());

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
                        if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight) {
                            console.log("getComments");
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
                limit: 3
            },
            type: "GET",
            dataType: "json",
            success: function (postViews) {
                postViews.forEach(function (postView) {
                    timelineContainer.appendChild(addPostView(postView));
                    offsetId = postView.postId;
                    console.log("update offsetId=" + postView.postId);
                });

                console.log("remaining="+postViews.length);
                // no more posts
                if (postViews.length == 0) {
                    $(window).off('scroll');
                    console.log("scroll off");
                } else {
                    $(window).off('scroll').scroll(function () {
                        if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight) {
                            console.log("scroll true offsetId="+offsetId);
                            loadPersonalTimeline(offsetId);
                        }
                    });
                }
            }
        });
    }

    if (postsContainer) {
        loadUserPosts(requestUser, offsetPostId);
    }

    if (timelineContainer) {
        loadPersonalTimeline(offsetPostId);
    }
});
