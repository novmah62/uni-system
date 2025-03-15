package kma.ktlt.post.domain.kafka;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentDTO {
    private String commentId;
    private String content;
    private String label; // spam hoặc ham

    // Getter, setter và constructor
}
