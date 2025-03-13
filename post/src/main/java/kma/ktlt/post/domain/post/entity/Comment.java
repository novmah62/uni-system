package kma.ktlt.post.domain.post.entity;

import jakarta.persistence.*;
import kma.ktlt.post.domain.common.enumType.CommentStatus;
import kma.ktlt.post.domain.common.enumType.DeleteBy;
import kma.ktlt.post.domain.common.enumType.PostStatus;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class    Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;


    private Long postId;

    private String userId;

    private boolean isUpdated;

    private Long parentCommentId;

    @Enumerated(EnumType.STRING)
    private CommentStatus status;


    private String removeId;

    @Enumerated(EnumType.STRING)
    private DeleteBy deleteBy; // Ai đã xóa comment



    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
