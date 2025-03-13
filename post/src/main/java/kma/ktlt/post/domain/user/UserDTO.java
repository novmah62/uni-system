package kma.ktlt.post.domain.user;


import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class UserDTO {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private boolean emailVerified;
}
