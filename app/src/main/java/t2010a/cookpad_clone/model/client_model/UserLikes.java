package t2010a.cookpad_clone.model.client_model;

import java.io.Serializable;

public class UserLikes implements Serializable {

    private Long postId;
    private Long userId;

    public UserLikes(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
