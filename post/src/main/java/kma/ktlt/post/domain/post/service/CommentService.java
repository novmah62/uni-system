package kma.ktlt.post.domain.post.service;

import kma.ktlt.post.domain.common.enumType.CommentStatus;
import kma.ktlt.post.domain.common.enumType.DeleteBy;
import kma.ktlt.post.domain.common.enumType.PostStatus;
import kma.ktlt.post.domain.common.exception.AppErrorCode;
import kma.ktlt.post.domain.common.exception.AppException;
import kma.ktlt.post.domain.kafka.CommentDTO;
import kma.ktlt.post.domain.kafka.KafkaProducerService;
import kma.ktlt.post.domain.notification.NotificationService;
import kma.ktlt.post.domain.post.dto.request.CreateCommentRequest;
import kma.ktlt.post.domain.post.dto.response.CreateCommentResponse;
import kma.ktlt.post.domain.post.dto.response.CreatePostResponse;
import kma.ktlt.post.domain.post.entity.Comment;
import kma.ktlt.post.domain.post.entity.Post;
import kma.ktlt.post.domain.post.repository.CommentRepository;
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
public class CommentService {
    CommentRepository commentRepository;
    UserService userService;
    PostRepository postRepository;
    KafkaProducerService kafkaProducerService;
    NotificationService notificationService;

    public CreateCommentResponse createComment(CreateCommentRequest request){
        String userId = userService.getUserAuthenticationName();
        Post post = postRepository.findPostToCommentOrLike(request.getPostId(), PostStatus.AVAILABLE)
                .orElseThrow(() -> new AppException(AppErrorCode.RESOURCE_NOT_FOUND));

        if (request.getParentCommentId() != null) {
            commentRepository.findCommentToReply(request.getParentCommentId(), CommentStatus.AVAILABLE)
                    .orElseThrow(() -> new AppException(AppErrorCode.RESOURCE_NOT_FOUND));
        }

        // Tạo và lưu comment mới
        Comment comment = Comment.builder()
                .postId(request.getPostId())
                .userId(userId)
                .content(request.getContent())
                .status(CommentStatus.AVAILABLE)
                .isUpdated(false)
                .parentCommentId(request.getParentCommentId())
                .build();

        Comment commentSaved  = commentRepository.save(comment);

        CommentDTO commentDTO = CommentDTO.builder()
                .commentId(commentSaved.getId().toString())
                .content(commentSaved.getContent())
                .label("ham")
                .build();

        kafkaProducerService.sendComment(commentDTO);
        return CreateCommentResponse.builder()
                .commentId(commentSaved.getId())
                .build();
    }


    public CreateCommentResponse updateComment(Long id, CreateCommentRequest request){
        String userId = userService.getUserAuthenticationName();
        int rowsUpdated = commentRepository.updateComment(id , userId, request.getContent() , CommentStatus.AVAILABLE);
        if (rowsUpdated == 1) {
            return CreateCommentResponse.builder()
                    .commentId(id)
                    .build();
        } else {
            throw new AppException(AppErrorCode.UNCATEGORIZED_EXCEPTION);
        }

    }

    public void deleteCommentByOwner(Long id){
        String userId = userService.getUserAuthenticationName();
        int rowsUpdated = commentRepository.removeCommentAndAllCommentChildByCommentOwner(id ,
                CommentStatus.AVAILABLE.name(),
                CommentStatus.DELETED.name(),
                userId,
                DeleteBy.COMMENT_OWNER.name()
        );
        if (rowsUpdated != 1) {
            throw new AppException(AppErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }


    public void deleteCommentByPostOwner(Long id){
        String userId = userService.getUserAuthenticationName();
        int rowsUpdated = commentRepository.removeCommentAndAllCommentChildByPostOwner(id ,
                CommentStatus.AVAILABLE.name(),
                CommentStatus.DELETED.name(),
                userId,
                DeleteBy.POST_OWNER.name()
        );
        if (rowsUpdated != 1) {
            throw new AppException(AppErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }


    public void deleteCommentByAdmin(Long id){
        String userId = userService.getUserAuthenticationName();
        int rowsUpdated = commentRepository.removeCommentAndAllCommentChildByAdmin(id ,
                CommentStatus.AVAILABLE.name(),
                CommentStatus.DELETED.name(),
                userId,
                DeleteBy.ADMIN.name()
        );
        if (rowsUpdated != 1) {
            throw new AppException(AppErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public void deleteCommentSpam(Long id){
        int rowsUpdated = commentRepository.removeCommentAndAllCommentChildSpam(id ,
                CommentStatus.AVAILABLE.name(),
                CommentStatus.DELETED.name(),
                DeleteBy.SPAM.name()
        );
        if(rowsUpdated == 1){
            Comment comment = commentRepository.findById(id).get();
            notificationService.deleteCommentSpam(comment);
        }
    }
}
