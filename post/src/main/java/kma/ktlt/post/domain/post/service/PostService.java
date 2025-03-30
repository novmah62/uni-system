package kma.ktlt.post.domain.post.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import kma.ktlt.post.domain.common.PageResponse;
import kma.ktlt.post.domain.common.enumType.*;
import kma.ktlt.post.domain.common.exception.AppErrorCode;
import kma.ktlt.post.domain.common.exception.AppException;
import kma.ktlt.post.domain.kafka.KafkaProducerService;
import kma.ktlt.post.domain.kafka.PostDTO;
import kma.ktlt.post.domain.post.dto.request.CreatePostRequest;
import kma.ktlt.post.domain.post.dto.response.CreatePostResponse;
import kma.ktlt.post.domain.post.dto.response.feed.CommentResponse;
import kma.ktlt.post.domain.post.dto.response.feed.PostResponse;
import kma.ktlt.post.domain.post.dto.response.feed.UserResponse;
import kma.ktlt.post.domain.post.entity.Post;
import kma.ktlt.post.domain.post.repository.CommentRepository;
import kma.ktlt.post.domain.post.repository.LikeRepository;
import kma.ktlt.post.domain.post.repository.PostRepository;
import kma.ktlt.post.domain.user.RedisService;
import kma.ktlt.post.domain.user.UserDTO;
import kma.ktlt.post.domain.user.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService {

    PostRepository postRepository;
    UserService userService;
    LikeRepository likeRepository;
    CommentRepository commentRepository;
    @PersistenceContext
    private EntityManager entityManager;
    RedisService redisService;
    KafkaProducerService kafkaProducerService;


    public CreatePostResponse createPost(CreatePostRequest request) {
        String userId = userService.getUserAuthenticationName();
        Post post = Post.builder()
                .content(request.getContent())
                .userId(userId)
                .isUpdated(false)
                .status(PostStatus.AVAILABLE)
                .build();

        Post postSaved = postRepository.save(post);

        PostDTO postDTO = PostDTO.builder()
                .postId(postSaved.getId().toString())
                .content(postSaved.getContent())
                .build();

        kafkaProducerService.sendPost(postDTO);
        return CreatePostResponse.builder()
                .postId(postRepository.save(post).getId())
                .build();
    }


    public CreatePostResponse updatePost(Long postId, CreatePostRequest request) {
        String userId = userService.getUserAuthenticationName();
        int rowsUpdated = postRepository.updatePostByOwner(postId, userId, request.getContent(), PostStatus.AVAILABLE);
        if (rowsUpdated == 1) {
            return CreatePostResponse.builder()
                    .postId(postId)
                    .build();
        } else {
            throw new AppException(AppErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public void deletePostByOwner(Long postId) {
        String userId = userService.getUserAuthenticationName();
        int rowsUpdated = postRepository.deletePostByOwner(postId, userId, PostStatus.AVAILABLE, PostStatus.AVAILABLE, DeleteBy.POST_OWNER);
        if (rowsUpdated == 1) {
            commentRepository.removeALlCommentOfPostWhenPostRemoved(postId, CommentStatus.AVAILABLE.name(), CommentStatus.DELETED.name(), userId, DeleteBy.POST_OWNER.name());
            likeRepository.onDeletePost(postId, LikeStatus.DELETED, LikeStatus.DELETED, userId, DeleteBy.POST_OWNER);
        } else {
            throw new AppException(AppErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public void deletePostByAdmin(Long postId) {
        String userId = userService.getUserAuthenticationName();
        UserDTO userDTO = new UserDTO();
        int rowsUpdated = postRepository.deletePostByAdmin(postId, userId, PostStatus.AVAILABLE, PostStatus.AVAILABLE, DeleteBy.ADMIN);
        if (rowsUpdated == 1) {
            likeRepository.onDeletePost(postId, LikeStatus.DELETED, LikeStatus.DELETED, userId, DeleteBy.ADMIN);
            commentRepository.removeALlCommentOfPostWhenPostRemoved(postId, CommentStatus.AVAILABLE.name(), CommentStatus.DELETED.name(), userId, DeleteBy.ADMIN.name());
        } else {
            throw new AppException(AppErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public PageResponse<List<PostResponse>> getNewFeed(int pageNo, int pageSize, String userId,
                                                       String keyword, LocalDate fromDate, LocalDate toDate,
                                                       List<String> sortBys, Integer minLikes, Integer maxLikes) {

        String dynamicQuery = buildDynamicQuery(pageNo, pageSize, userId, keyword, fromDate, toDate, sortBys, minLikes, maxLikes);
        Query query = entityManager.createNativeQuery(dynamicQuery, Tuple.class);

        if (userId != null) query.setParameter("userId", userId);
        if (keyword != null) query.setParameter("keyword", keyword);
        if (fromDate != null) query.setParameter("fromDate", fromDate);
        if (toDate != null) query.setParameter("toDate", toDate);
        if (minLikes != null) query.setParameter("minLikes", minLikes);
        if (maxLikes != null) query.setParameter("maxLikes", maxLikes);

        List<Tuple> results = query.getResultList();

        List<PostResponse> postResponses = results.stream()
                .map(tuple -> PostResponse.builder()
                        .postId(tuple.get("id", Long.class))
                        .userId(tuple.get("user_id", String.class))
                        .content(tuple.get("content", String.class))
                        .likeCount(tuple.get("likeCount", Long.class))
                        .commentCount(tuple.get("commentCount", Long.class))
                        .updatedAt(((Timestamp) tuple.get("updated_at")).toLocalDateTime())
                        .isUpdated(tuple.get("is_updated", Boolean.class))
                        .build())
                .toList();


        postResponses.forEach(postResponse -> {
            Optional<UserDTO> userDTO1 = Optional.empty();
            UserDTO userDTO = new UserDTO();
            if (!postResponse.getUserId().isEmpty()) {
                userDTO1 = redisService.getUserById(postResponse.getUserId());
            }
            if(userDTO1.isPresent()){
                userDTO = userDTO1.get();
            }


            if (userDTO != null) {
                UserResponse userResponse = UserResponse.builder()
                        .id(userDTO.getId())
                        .username(userDTO.getUsername())
                        .firstName(userDTO.getFirstName())
                        .lastName(userDTO.getLastName())
                        .email(userDTO.getEmail())
                        .build();
                postResponse.setUserInfo(userResponse);
            }
        });

        long totalElements = results.size();
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        return PageResponse.<List<PostResponse>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
//                .totalPages(totalPages)
//                .totalElements(totalElements)
                .items(postResponses)
                .build();
    }


    public String buildDynamicQuery(
            int pageNo, int pageSize, String userId,
            String keyword, LocalDate fromDate, LocalDate toDate,
            List<String> sortBys, Integer minLikes, Integer maxLikes) {

        StringBuilder query = new StringBuilder(
                "SELECT p.id, p.user_id, p.content, " +
                        "COUNT(DISTINCT l.id) AS likeCount, " +
                        "COUNT(DISTINCT c.id) AS commentCount, " +
                        "p.updated_at, p.is_updated " +
                        "FROM posts p " +
                        "LEFT JOIN likes l ON p.id = l.post_id AND l.status = 'LIKE' " +
                        "LEFT JOIN comments c ON p.id = c.post_id AND c.status = 'AVAILABLE' " +
                        "WHERE p.status = 'AVAILABLE'"
        );

        if (userId != null) query.append(" AND p.user_id = :userId");
        if (keyword != null) query.append(" AND p.content LIKE CONCAT('%', :keyword, '%')");
        if (fromDate != null) query.append(" AND p.created_at >= :fromDate");
        if (toDate != null) query.append(" AND p.created_at <= :toDate");

        query.append(" GROUP BY p.id");

        if (minLikes != null || maxLikes != null) {
            query.append(" HAVING ");
            if (minLikes != null) query.append(" COUNT(DISTINCT l.id) >= :minLikes ");
            if (minLikes != null && maxLikes != null) query.append(" AND ");
            if (maxLikes != null) query.append(" COUNT(DISTINCT l.id) <= :maxLikes ");
        }

        if (sortBys != null && !sortBys.isEmpty()) {
            String orderByClause = sortBys.stream()
                    .map(s -> {
                        PostSort sort = PostSort.valueOf(s);
                        return switch (sort.getField()) {
                            case "like" -> "likeCount " + sort.getDirection();
                            case "comment" -> "commentCount " + sort.getDirection();
                            default -> "p." + sort.getField() + " " + sort.getDirection();
                        };
                    })
                    .collect(Collectors.joining(", "));
            query.append(" ORDER BY ").append(orderByClause);
        } else {
            query.append(" ORDER BY p.updated_at DESC");
        }

        query.append(" LIMIT ").append(pageSize)
                .append(" OFFSET ").append(pageNo * pageSize);

        return query.toString();
    }

    public PageResponse<List<CommentResponse>> getDirectCommentOfPost(Long postId, Long parentCommentId, int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        System.out.println(parentCommentId);
        List<CommentResponse> result = commentRepository.getDirectCommentOfPost(postId, parentCommentId, CommentStatus.AVAILABLE, pageable).getContent();

        result.forEach(commentResponse -> {
            String userIdR = commentResponse.getUserId();
            redisService.getUserById(userIdR).ifPresent(userDTO -> {
                UserResponse userResponse = UserResponse.builder()
                        .id(userDTO.getId())
                        .username(userDTO.getUsername())
                        .firstName(userDTO.getFirstName())
                        .lastName(userDTO.getLastName())
                        .email(userDTO.getEmail())
                        .build();
                commentResponse.setUserInfo(userResponse);
            });
        });

        return PageResponse.<List<CommentResponse>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .items(result)
                .build();
    }

    public void deletePostBySpam(Long id) {
        int rowUpdated = postRepository.deletePostBySpam(id, PostStatus.AVAILABLE, PostStatus.DELETED, DeleteBy.SPAM);
        if (rowUpdated == 1) {
            commentRepository.removeALlCommentOfPostWhenPostRemoved(id, CommentStatus.AVAILABLE.name(), CommentStatus.DELETED.name(), "spam", DeleteBy.SPAM.name());
        }
    }
}
