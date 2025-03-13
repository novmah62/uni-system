package kma.ktlt.post.app.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import kma.ktlt.post.domain.common.PageResponse;
import kma.ktlt.post.domain.common.ResponseData;
import kma.ktlt.post.domain.common.enumType.PostSort;
import kma.ktlt.post.domain.common.validation.EnumValue;
import kma.ktlt.post.domain.common.validation.ValidEnumList;
import kma.ktlt.post.domain.post.dto.request.CreatePostRequest;
import kma.ktlt.post.domain.post.dto.response.CreatePostResponse;
import kma.ktlt.post.domain.post.dto.response.feed.CommentResponse;
import kma.ktlt.post.domain.post.dto.response.feed.PostResponse;
import kma.ktlt.post.domain.post.service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PostController {

    PostService postService;

    @PreAuthorize("hasAnyRole('USER' , 'ADMIN' ,'GUEST')")


    @GetMapping
    public ResponseData<PageResponse<List<PostResponse>>> getNewFeed(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String keyword,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) @ValidEnumList(enumClass = PostSort.class) List<String> sortBys,
            @RequestParam(required = false) Integer minLikes,
            @RequestParam(required = false) Integer maxLikes
    ) {
        PageResponse<List<PostResponse>> response = postService.getNewFeed(
                pageNo, pageSize, userId, keyword, fromDate, toDate, sortBys,minLikes ,maxLikes
        );
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Lấy danh sách bài viết thành công",
                LocalDateTime.now(),
                response
        );
    }


    @PreAuthorize("hasAnyRole('USER' , 'ADMIN')")

    @GetMapping("/{postId}/comments")
    public ResponseData<PageResponse<List<CommentResponse>>> getDirectCommentOfPost(
            @PathVariable("postId") Long postId,
            @RequestParam(required = false) Long parentCommentId,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        PageResponse<List<CommentResponse>> response = postService.getDirectCommentOfPost(postId, parentCommentId, pageNo, pageSize);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Lấy danh sách bình luận thành công",
                LocalDateTime.now(),
                response
        );
    }

    @PreAuthorize("hasAnyRole('USER' , 'ADMIN')")


    @PostMapping
    public ResponseData<CreatePostResponse> createPost(
            @RequestBody @Valid CreatePostRequest request) {
        CreatePostResponse response = postService.createPost(request);
        return new ResponseData<>(
                HttpStatus.CREATED.value(),
                "Post created successfully",
                LocalDateTime.now(),
                response
        );
    }

    @PreAuthorize("hasAnyRole('USER' , 'ADMIN')")

    @PutMapping("/{postId}")
    public ResponseData<CreatePostResponse> updatePost(
            @PathVariable Long postId,
            @RequestBody @Valid CreatePostRequest request) {
        CreatePostResponse response = postService.updatePost(postId ,request);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Post updated successfully",
                LocalDateTime.now(),
                response
        );
    }
    @PreAuthorize("hasAnyRole('USER' , 'ADMIN')")

    @DeleteMapping("/post-owner/{postId}")
    public ResponseData<Void> deletePostByOwner(
            @PathVariable Long postId) {
        postService.deletePostByOwner(postId);
        return new ResponseData<>(
                HttpStatus.NO_CONTENT.value(),
                "Post deleted successfully",
                LocalDateTime.now()
        );
    }
    @PreAuthorize("hasAnyRole('ADMIN')")

    @DeleteMapping("/admin/{postId}")
    public ResponseData<Void> deletePostByAdmin(
            @PathVariable Long postId) {
        postService.deletePostByAdmin(postId);
        return new ResponseData<>(
                HttpStatus.NO_CONTENT.value(),
                "Post deleted successfully",
                LocalDateTime.now()
        );
    }

}
