package kma.ktlt.post.domain.kafka;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostDTO {
    private String postId;
    private String content;
    private String label;
}
