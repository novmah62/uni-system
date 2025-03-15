package kma.ktlt.post.domain.common.enumType;

public enum DeleteBy {
    OWNER,      // Chủ sở hữu comment/post
    POST_OWNER, // Chủ bài viết (có quyền xóa comment trong bài viết của mình)
    ADMIN,      // Quản trị viên
    SPAM,      // Xóa do phát hiện spam
    MODERATOR ,
    COMMENT_OWNER
    }
