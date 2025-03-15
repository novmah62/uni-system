package kma.ktlt.post.domain.notification.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "token_device_firebase")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDeviceFirebase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String tokenDevice;
}
