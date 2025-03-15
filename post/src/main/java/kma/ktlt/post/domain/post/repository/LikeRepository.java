package kma.ktlt.post.domain.post.repository;

import kma.ktlt.post.domain.common.enumType.CommentStatus;
import kma.ktlt.post.domain.common.enumType.DeleteBy;
import kma.ktlt.post.domain.common.enumType.LikeStatus;
import kma.ktlt.post.domain.common.enumType.PostStatus;
import kma.ktlt.post.domain.post.entity.Comment;
import kma.ktlt.post.domain.post.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {



    @Query("""
    SELECT l FROM Like l WHERE l.userId = :userId AND l.postId = :postId
""")
    Optional<Like> findLikeByUserIdAndPostId(@Param("userId") String userId, @Param("postId") Long postId);




    @Modifying
    @Query("""
    UPDATE Like l 
    SET l.status = :newStatus 
    WHERE l.status = :currentStatus 
      AND l.userId = :userId 
      AND l.postId = :postId
""")
    int disLike(@Param("userId") String userId,
                @Param("postId") Long postId,
                @Param("currentStatus") LikeStatus currentStatus,
                @Param("newStatus") LikeStatus newStatus);


    @Modifying
    @Query("""
    UPDATE Like l 
    SET l.status = :newStatus, 
        l.removeId = :removeId, 
        l.deleteBy = :deleteBy 
    WHERE l.postId = :postId 
      AND l.status <> :excludedStatus
""")
    int onDeletePost(@Param("postId") Long postId,
                     @Param("excludedStatus") LikeStatus excludedStatus,
                     @Param("newStatus") LikeStatus newStatus,
                     @Param("removeId") String removeId,
                     @Param("deleteBy") DeleteBy deleteBy);





}
