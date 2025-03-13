package kma.ktlt.post.domain.post.dto.request;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Data
public class CreatePostRequest implements Serializable {
    private String content;

}
