package kma.ktlt.post.app.api;

import jakarta.validation.Valid;
import kma.ktlt.post.domain.common.ResponseData;
import kma.ktlt.post.domain.post.dto.request.CreateCommentRequest;
import kma.ktlt.post.domain.post.dto.request.CreatePostRequest;
import kma.ktlt.post.domain.post.dto.response.CreateCommentResponse;
import kma.ktlt.post.domain.post.dto.response.CreatePostResponse;
import kma.ktlt.post.domain.post.service.CommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/comments")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CommentController {

    CommentService commentService;

//    @PreAuthorize("hasAnyRole('USER' , 'ADMIN')")
    @PostMapping
    public ResponseData<CreateCommentResponse> createComment(
            @RequestBody @Valid CreateCommentRequest request) {
        CreateCommentResponse response = commentService.createComment(request);
        return new ResponseData<>(
                HttpStatus.CREATED.value(),
                "Post created successfully",
                LocalDateTime.now(),
                response
        );
    }
//    @PreAuthorize("hasAnyRole('USER' , 'ADMIN')")

    @PutMapping("/{commentId}")
    public ResponseData<CreateCommentResponse> updateComment(
            @PathVariable Long commentId,
            @RequestBody @Valid CreateCommentRequest request) {
        CreateCommentResponse response = commentService.updateComment(commentId , request);
        return new ResponseData<>(
                HttpStatus.CREATED.value(),
                "Comment updated successfully",
                LocalDateTime.now(),
                response
        );
    }

//    @PreAuthorize("hasAnyRole('USER' , 'ADMIN')")

    @DeleteMapping("/comment-owner/{postId}")
    public ResponseData<Void> deleteCommentByOwner(
            @PathVariable Long postId) {
        commentService.deleteCommentByOwner(postId);
        return new ResponseData<>(
                HttpStatus.NO_CONTENT.value(),
                "Comment deleted successfully",
                LocalDateTime.now()
        );
    }

//    @PreAuthorize("hasAnyRole('USER' , 'ADMIN')")

    @DeleteMapping("/post-owner/{postId}")
    public ResponseData<Void> deletePostByPostOwner(
            @PathVariable Long postId) {
        commentService.deleteCommentByPostOwner(postId);
        return new ResponseData<>(
                HttpStatus.NO_CONTENT.value(),
                "Comment deleted successfully",
                LocalDateTime.now()
        );
    }


//    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/admin/{postId}")
    public ResponseData<Void> deleteCommentByAdmin(
            @PathVariable Long postId) {
        commentService.deleteCommentByAdmin(postId);
        return new ResponseData<>(
                HttpStatus.NO_CONTENT.value(),
                "Comment deleted successfully",
                LocalDateTime.now()
        );
    }

}
