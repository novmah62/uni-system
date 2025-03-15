package kma.ktlt.post.domain.post.entity;

import jakarta.persistence.*;
import kma.ktlt.post.domain.common.enumType.CommentStatus;
import kma.ktlt.post.domain.common.enumType.DeleteBy;
import kma.ktlt.post.domain.common.enumType.LikeStatus;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "likes")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long postId;

    private String userId;

    @Enumerated(EnumType.STRING)
    private LikeStatus status;

    @Enumerated(EnumType.STRING)
    private DeleteBy deleteBy;

    private String removeId;


    private LocalDateTime createdAt = LocalDateTime.now();
}
