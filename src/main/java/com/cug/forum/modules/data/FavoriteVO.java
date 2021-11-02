package com.cug.forum.modules.data;

import com.cug.forum.modules.entity.Favorite;

public class FavoriteVO extends Favorite {
    // extend
    private PostVO post;

    public PostVO getPost() {
        return post;
    }

    public void setPost(PostVO post) {
        this.post = post;
    }
}
