package kma.ktlt.post.domain.post.service;

import kma.ktlt.post.domain.common.enumType.CommentStatus;
import kma.ktlt.post.domain.common.enumType.DeleteBy;
import kma.ktlt.post.domain.common.enumType.LikeStatus;
import kma.ktlt.post.domain.common.enumType.PostStatus;
import kma.ktlt.post.domain.common.exception.AppErrorCode;
import kma.ktlt.post.domain.common.exception.AppException;
import kma.ktlt.post.domain.post.dto.request.CreateLikeRequest;
import kma.ktlt.post.domain.post.dto.response.LikeResponse;
import kma.ktlt.post.domain.post.entity.Comment;
import kma.ktlt.post.domain.post.entity.Like;
import kma.ktlt.post.domain.post.entity.Post;
import kma.ktlt.post.domain.post.repository.LikeRepository;
import kma.ktlt.post.domain.post.repository.PostRepository;
import kma.ktlt.post.domain.user.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class LikeService {
    UserService userService;
    PostRepository postRepository;
    LikeRepository likeRepository;


    public LikeResponse like(CreateLikeRequest request){
        String userId = userService.getUserAuthenticationName();
        Post post =  postRepository.findPostToCommentOrLike(request.getPostId() , PostStatus.AVAILABLE).orElseThrow(
                ()-> new AppException(AppErrorCode.RESOURCE_NOT_FOUND)
        );

        Like like = likeRepository.findLikeByUserIdAndPostId(userId, request.getPostId()).orElse(null);

        if (like == null) {
            like = Like.builder()
                    .postId(post.getId())
                    .userId(userId)
                    .status(LikeStatus.LIKE)
                    .build();
        } else if (like.getStatus() == LikeStatus.LIKE) {
            throw new AppException(AppErrorCode.ALREADY_LIKED);
        } else {
            like.setStatus(LikeStatus.LIKE);
        }

        return LikeResponse.builder()
                .likeId(likeRepository.save(like).getId())
                .build();
    }

    public void unlike(CreateLikeRequest request){
        String userId = userService.getUserAuthenticationName();
        int rowsUpdated = likeRepository.disLike(userId ,request.getPostId() ,LikeStatus.LIKE , LikeStatus.DISLIKE );
        if (rowsUpdated != 1) {
            return;
        } else {
            throw new AppException(AppErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
}
