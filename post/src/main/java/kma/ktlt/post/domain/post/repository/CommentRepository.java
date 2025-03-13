package kma.ktlt.post.domain.post.repository;

import kma.ktlt.post.domain.common.enumType.CommentStatus;
import kma.ktlt.post.domain.common.enumType.DeleteBy;
import kma.ktlt.post.domain.post.dto.response.feed.CommentResponse;
import kma.ktlt.post.domain.post.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment , Long> {




   @Modifying
    @Query("""
    UPDATE Comment c 
    SET c.content = :content , c.isUpdated = true
    WHERE c.userId = :userId 
    AND c.id = :id 
    AND c.status = :constrainStatus
""")
    int updateComment(@Param("id") Long id,
                      @Param("userId") String userId,
                      @Param("content") String content,
                      @Param("constrainStatus") CommentStatus constrainStatus);


 @Query("""
  SELECT c FROM Comment c WHERE c.id = :id AND c.status = :constrainStatus
""")
 Optional<Comment> findCommentToReply(@Param("id") Long parentCommentId, @Param("constrainStatus") CommentStatus constrainStatus);


//
//@Transactional
//@Modifying
//@Query(value = """
//    WITH RECURSIVE comment_tree AS (
//        -- Chọn tất cả các comment gốc có post_id tương ứng và trạng thái phù hợp
//        SELECT id FROM comments WHERE post_id = :postId AND status = :constrainStatus
//        UNION ALL
//        -- Truy vấn đệ quy tìm tất cả các comment con
//        SELECT c.id FROM comments c
//        INNER JOIN comment_tree ct ON c.parent_comment_id = ct.id
//    )
//    UPDATE comments
//    SET status = :newStatus,
//        remove_id = :removeId,
//        delete_by = :deleteBy
//    WHERE id IN (SELECT id FROM comment_tree);
//    """, nativeQuery = true)
//void removeALlCommentOfPost(
//        @Param("postId") Long postId,
//        @Param("constrainStatus") String constrainStatus,
//        @Param("newStatus") String newStatus,
//        @Param("removeId") String removeId,
//        @Param("deleteBy") String deleteBy
//);



// @Transactional
// @Modifying
// @Query(value = """
//    WITH RECURSIVE comment_tree AS (
//        -- Chọn comment gốc có id = :id và trạng thái phù hợp
//        SELECT id FROM comments WHERE id = :id AND status = :constrainStatus
//        UNION ALL
//        -- Truy vấn đệ quy tìm tất cả comment con (có cùng trạng thái ban đầu)
//        SELECT c.id FROM comments c
//        INNER JOIN comment_tree ct ON c.parent_comment_id = ct.id
//        WHERE c.status = :constrainStatus
//    )
//    UPDATE comments
//    SET status = :newStatus,
//        remove_id = :removeId,
//        delete_by = :deleteBy
//    WHERE id IN (SELECT id FROM comment_tree);
//    """, nativeQuery = true)
// void removeCommentAndAllCommentChild(
//         @Param("id") Long id,
//         @Param("constrainStatus") String constrainStatus,
//         @Param("newStatus") String newStatus,
//         @Param("removeId") String removeId,
//         @Param("deleteBy") String deleteBy
// );

//-----------------------------------------------------------------------------------
 @Transactional
 @Modifying
 @Query(value = """
    WITH RECURSIVE comment_tree AS (
        -- Chọn comment gốc nếu có trạng thái phù hợp
        SELECT id FROM comments WHERE id = :id AND status = :constrainStatus AND user_id = :userId
        UNION ALL
        -- Truy vấn đệ quy tìm tất cả comment con
        SELECT c.id FROM comments c
        INNER JOIN comment_tree ct ON c.parent_comment_id = ct.id
    )
    UPDATE comments
    SET status = :newStatus, 
        remove_id = :userId, 
        delete_by = :deleteBy
    WHERE id IN (SELECT id FROM comment_tree) 
    AND status = :constrainStatus;
    """, nativeQuery = true)

 int removeCommentAndAllCommentChildByCommentOwner(
         @Param("id") Long id,
         @Param("constrainStatus") String constrainStatus,
         @Param("newStatus") String newStatus,
         @Param("userId") String userId,
         @Param("deleteBy") String deleteBy
 );

 @Transactional
 @Modifying
 @Query(value = """
    WITH RECURSIVE comment_tree AS (
        SELECT id FROM comments 
        WHERE id = :id 
        AND status = :constrainStatus 
        AND post_id IN (
            SELECT id FROM posts 
            WHERE user_id = :userId 
        )
        UNION ALL
        SELECT c.id FROM comments c
        INNER JOIN comment_tree ct ON c.parent_comment_id = ct.id
        WHERE c.status = :constrainStatus
    )
    UPDATE comments
    SET status = :newStatus, 
        remove_id = :userId, 
        delete_by = :deleteBy
    WHERE id IN (SELECT id FROM comment_tree);
    """, nativeQuery = true)
 int removeCommentAndAllCommentChildByPostOwner(
         @Param("id") Long id,
         @Param("constrainStatus") String constrainStatus,
         @Param("newStatus") String newStatus,
         @Param("userId") String userId,
         @Param("deleteBy") String deleteBy
 );


 @Transactional
 @Modifying
 @Query(value = """
    WITH RECURSIVE comment_tree AS (
        SELECT id FROM comments 
        WHERE id = :id 
        AND status = :constrainStatus 
        
        UNION ALL
        
        SELECT c.id FROM comments c
        INNER JOIN comment_tree ct ON c.parent_comment_id = ct.id
        WHERE c.status = :constrainStatus
    )
    UPDATE comments
    SET status = :newStatus, 
        remove_id = :userId, 
        delete_by = :deleteBy
    WHERE id IN (SELECT id FROM comment_tree);
    """, nativeQuery = true)
 int removeCommentAndAllCommentChildByAdmin(
         @Param("id") Long id,
         @Param("constrainStatus") String constrainStatus,
         @Param("newStatus") String newStatus,
         @Param("userId") String userId,
         @Param("deleteBy") String deleteBy
 );


 @Transactional
 @Modifying
 @Query(value = """
    WITH RECURSIVE comment_tree AS (
        SELECT id FROM comments WHERE post_id = :postId AND status = :constrainStatus
        UNION ALL
        SELECT c.id FROM comments c
        INNER JOIN comment_tree ct ON c.parent_comment_id = ct.id
    )
    UPDATE comments
    SET status = :newStatus, 
        remove_id = :removeId, 
        delete_by = :deleteBy
    WHERE id IN (SELECT id FROM comment_tree);
    """, nativeQuery = true)
 void removeALlCommentOfPostWhenPostRemoved(
         @Param("postId") Long postId,
         @Param("constrainStatus") String constrainStatus,
         @Param("newStatus") String newStatus,
         @Param("removeId") String removeId,
         @Param("deleteBy") String deleteBy
 );



 @Query("""
    SELECT new kma.ktlt.post.domain.post.dto.response.feed.CommentResponse(
        c.id,
        c.content,
        c.postId,
        c.userId,
        c.isUpdated,
        c.parentCommentId,
        c.updatedAt,
        null
    )
    FROM Comment c
    WHERE c.postId = :postId
      AND c.status = :commentStatus
      AND ((:parentCommentId IS NULL AND c.parentCommentId IS NULL) OR (:parentCommentId IS NOT NULL AND c.parentCommentId = :parentCommentId))
      ORDER BY c.updatedAt DESC
    """)
 Page<CommentResponse> getDirectCommentOfPost(@Param("postId") Long postId,
                                              @Param("parentCommentId") Long parentCommentId,
                                              @Param("commentStatus") CommentStatus commentStatus,
                                              Pageable pageable);


 @Transactional
 @Modifying
 @Query(value = """
    WITH RECURSIVE comment_tree AS (
        SELECT id FROM comments 
        WHERE id = :id 
          AND status = :constrainStatus 
        UNION ALL
        SELECT c.id FROM comments c
        INNER JOIN comment_tree ct ON c.parent_comment_id = ct.id
        WHERE c.status = :constrainStatus
    )
    UPDATE comments
    SET status = :newStatus, 
        delete_by = :deleteBy
    WHERE id IN (SELECT id FROM comment_tree);
    """, nativeQuery = true)
 int removeCommentAndAllCommentChildSpam(
         @Param("id") Long id,
         @Param("constrainStatus") String constrainStatus,
         @Param("newStatus") String newStatus,

         @Param("deleteBy") String deleteBy);



 @Query("""
    SELECT c FROM Comment c WHERE c.deleteBy = :adminValue
""")
 List<Comment> findSpamComment(@Param("adminValue") DeleteBy adminValue);

}
