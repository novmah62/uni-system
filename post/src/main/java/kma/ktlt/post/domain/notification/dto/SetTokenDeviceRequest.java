package kma.ktlt.post.domain.notification.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SetTokenDeviceRequest {
    private String tokenDevice;
}
