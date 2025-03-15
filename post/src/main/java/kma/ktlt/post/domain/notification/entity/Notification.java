package kma.ktlt.post.domain.notification.entity;
import jakarta.persistence.*;
import kma.ktlt.post.domain.common.enumType.TypeReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String receiverId; // Người nhận thông báo


    private String senderId; // Người thực hiện hành động

    @Enumerated(EnumType.STRING)
    private TypeReference type;

    private Long referenceId; // ID của bài viết hoặc bình luận liên quan

    private boolean isRead = false;

    private boolean isRemoved = false;

    private String content;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
