package kma.ktlt.post.domain.notification.dto.UpdateRequest;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OnPostRemove {
    private Long postId;
}
