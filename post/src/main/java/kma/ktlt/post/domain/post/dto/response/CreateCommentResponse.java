package kma.ktlt.post.domain.post.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateCommentResponse {
    Long commentId;
}
