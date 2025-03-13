package kma.ktlt.post.app.exception;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseMultipleField {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private Map<String , String> message;
    private String path;
    private ErrorType type;
}
