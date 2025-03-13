package kma.ktlt.post.domain.post.repository;

import kma.ktlt.post.domain.common.enumType.DeleteBy;
import kma.ktlt.post.domain.common.enumType.PostSort;
import kma.ktlt.post.domain.common.enumType.PostStatus;
import kma.ktlt.post.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Modifying
    @Query("""
    UPDATE Post p 
    SET p.content = :content ,p.isUpdated = true 
    WHERE p.id = :postId 
    AND p.status = :constrainStatus 
    AND p.userId = :ownerId
""")
    int updatePostByOwner(@Param("postId") Long postId,
                   @Param("ownerId") String ownerId,
                   @Param("content") String content,
                   @Param("constrainStatus") PostStatus constrainStatus);



    @Modifying
    @Query("""
    UPDATE Post p 
    SET p.status = :newStatus, p.removeId = :userId, p.deleteBy = :deleteBy
    WHERE p.id = :postId 
    AND p.status = :constrainStatus
    AND p.userId = :userId
""")
    int deletePostByOwner(@Param("postId") Long postId,
                          @Param("userId") String userId,
                          @Param("constrainStatus") PostStatus constrainStatus,
                          @Param("newStatus") PostStatus newStatus,
                          @Param("deleteBy") DeleteBy deleteBy);



    @Modifying
    @Query("""
    UPDATE Post p 
    SET p.status = :newStatus, p.removeId = :adminId, p.deleteBy = :deleteBy
    WHERE p.id = :postId 
    AND p.status = :constrainStatus
""")
    int deletePostByAdmin(@Param("postId") Long postId,
                          @Param("adminId") String adminId,
                          @Param("constrainStatus") PostStatus constrainStatus,
                          @Param("newStatus") PostStatus newStatus,
                          @Param("deleteBy") DeleteBy deleteBy);


    @Modifying
    @Query("""
    UPDATE Post p 
    SET p.status = :newStatus,  p.deleteBy = :deleteBy
    WHERE p.id = :postId 
    AND p.status = :constrainStatus
""")
    int deletePostBySpam(@Param("postId") Long postId,
                          @Param("constrainStatus") PostStatus constrainStatus,
                          @Param("newStatus") PostStatus newStatus,
                          @Param("deleteBy") DeleteBy deleteBy);




    @Query("""
    SELECT p FROM Post p 
    WHERE p.status = :constrainStatus 
    AND p.id = :postId
""")
    Optional<Post> findPostToCommentOrLike(@Param("postId") Long postId,
                                     @Param("constrainStatus") PostStatus constrainStatus);

    @Query("""
    SELECT c FROM Post c WHERE c.deleteBy = :adminValue
""")
    List<Post> findSpamPost(@Param("adminValue")DeleteBy deleteBy);


//    @Query(value = """
//
//""", nativeQuery = true)
//    List<Object> getNewFeed(int pageNo, int pageSize, String userId, String keyword, LocalDate fromDate, LocalDate toDate, String sortColumn , String sortDirection);
//

}
