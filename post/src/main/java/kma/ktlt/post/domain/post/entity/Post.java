package kma.ktlt.post.domain.post.entity;
import jakarta.persistence.*;
import kma.ktlt.post.domain.common.enumType.CommentStatus;
import kma.ktlt.post.domain.common.enumType.DeleteBy;
import kma.ktlt.post.domain.common.enumType.PostStatus;
import kma.ktlt.post.domain.common.enumType.TypeReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;


    private String userId;


    @Enumerated(EnumType.STRING)
    private PostStatus status;


    private String removeId;

    private boolean isUpdated;


    @Enumerated(EnumType.STRING)
    private DeleteBy deleteBy;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}