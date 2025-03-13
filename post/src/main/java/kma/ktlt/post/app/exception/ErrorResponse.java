package kma.ktlt.post.app.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private ErrorType type;
    private int code;

}
