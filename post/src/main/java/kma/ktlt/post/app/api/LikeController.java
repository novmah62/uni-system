package kma.ktlt.post.app.api;

import jakarta.validation.Valid;
import kma.ktlt.post.domain.common.ResponseData;
import kma.ktlt.post.domain.post.dto.request.CreateCommentRequest;
import kma.ktlt.post.domain.post.dto.request.CreateLikeRequest;
import kma.ktlt.post.domain.post.dto.response.CreateCommentResponse;
import kma.ktlt.post.domain.post.dto.response.LikeResponse;
import kma.ktlt.post.domain.post.service.LikeService;
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
@RequestMapping("/api/v1/likes")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class LikeController {
    LikeService likeService;


    @PreAuthorize("hasAnyRole('USER' , 'ADMIN')")

    @PutMapping("/like")
    public ResponseData<LikeResponse> like(CreateLikeRequest request) {
        LikeResponse response = likeService.like(request);
        return new ResponseData<>(
                HttpStatus.CREATED.value(),
                "Post created successfully",
                LocalDateTime.now(),
                response
        );
    }

    @PreAuthorize("hasAnyRole('USER' , 'ADMIN')")

    @PutMapping("/unlike")
    public ResponseData<LikeResponse> unlike(CreateLikeRequest request) {
        likeService.unlike(request);
        return new ResponseData<>(
                HttpStatus.CREATED.value(),
                "Liked successfully",
                LocalDateTime.now()
        );
    }
}
