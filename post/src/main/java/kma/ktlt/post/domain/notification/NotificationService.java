package kma.ktlt.post.domain.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import kma.ktlt.post.domain.common.PageResponse;
import kma.ktlt.post.domain.common.enumType.TypeReference;
import kma.ktlt.post.domain.common.exception.AppErrorCode;
import kma.ktlt.post.domain.common.exception.AppException;
import kma.ktlt.post.domain.notification.dto.*;
import kma.ktlt.post.domain.notification.dto.UpdateRequest.OnCommentRemove;
import kma.ktlt.post.domain.notification.dto.UpdateRequest.OnPostRemove;
import kma.ktlt.post.domain.notification.entity.Notification;
import kma.ktlt.post.domain.notification.entity.NotificationRepository;
import kma.ktlt.post.domain.notification.entity.TokenDeviceFirebase;
import kma.ktlt.post.domain.notification.entity.TokenDeviceFirebaseRepository;
import kma.ktlt.post.domain.post.dto.response.feed.CommentResponse;
import kma.ktlt.post.domain.post.entity.Comment;
import kma.ktlt.post.domain.user.RedisService;
import kma.ktlt.post.domain.user.UserDTO;
import kma.ktlt.post.domain.user.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class NotificationService {

    RedisService redisService;
    NotificationRepository notificationRepository;
    TokenDeviceFirebaseRepository tokenDeviceFirebaseRepository;
    UserService userService;
    final FirebaseMessaging firebaseMessaging;
    final ObjectMapper objectMapper;
    public void setTokenDeviceFirebase(SetTokenDeviceRequest request){
        String userId = userService.getUserAuthenticationName();
        tokenDeviceFirebaseRepository.setTokenDeviceFirebase(request.getTokenDevice() , userId);
    }
    //gửi thông báo đến những user khác trong hệ thống
    public void onPostCreated(NotificationPostRequest request) {
        NotificationTemplate template = NotificationTemplate.NEW_POST;

        UserDTO userDTO = redisService.getUserById(request.getSenderId()).orElseThrow(
                () -> new AppException(AppErrorCode.UNCATEGORIZED_EXCEPTION)
        );

        String content = request.getContent();
        String contentSnippet = content.length() < 15 ? content : content.substring(0, 15) + "...";

        // Format nội dung thông báo
        String formattedContent = template.format(userDTO.getFirstName() + " " + userDTO.getLastName(), contentSnippet, request.getPostId());

        // Tạo thông báo
        Notification notification = Notification.builder()
                .senderId(userDTO.getId())
                .type(TypeReference.POST_CREATE)
                .referenceId(request.getPostId())
                .isRead(false)
                .content(formattedContent)
                .build();

        List<TokenDeviceFirebase> tokenDeviceFirebaseList = tokenDeviceFirebaseRepository.findAll();
        tokenDeviceFirebaseList.forEach(tokenDeviceFirebase -> sendNotification(notification , tokenDeviceFirebase));
    }

    //thông báo cho chủ bài viết nếu có like mới
    public void onLikeCreated(NotificationLikeRequest request) {
        // Lấy thông tin người gửi từ Redis
        UserDTO senderDTO = redisService.getUserById(request.getSenderId()).orElseThrow(
                () -> new AppException(AppErrorCode.UNCATEGORIZED_EXCEPTION)
        );

        // Cắt snippet cho nội dung bài viết
        String contentSnippet = request.getContent().length() > 15 ? request.getContent().substring(0, 15) + "..." : request.getContent();

        // Xác định template phù hợp
        NotificationTemplate template = (request.getCurrentLike() == 1)
                ? NotificationTemplate.NEW_LIKE_1
                : NotificationTemplate.NEW_LIKE_N;

        // Format nội dung thông báo
        String formattedContent = (request.getCurrentLike() == 1)
                ? template.format(senderDTO.getFirstName() + " " + senderDTO.getLastName(), contentSnippet)
                : template.format(senderDTO.getFirstName() + " " + senderDTO.getLastName(), String.valueOf(request.getCurrentLike() - 1), contentSnippet);

        // Tạo thông báo
        Notification notification = notificationRepository.findNotification(request.getPostId(), TypeReference.LIKE_POST)
                .orElse(Notification.builder()
                        .senderId(senderDTO.getId())
                        .type(TypeReference.LIKE_POST)
                        .referenceId(request.getPostId())
                        .receiverId(request.getPostOwnerId())
                        .content(formattedContent)
                        .build());

        notification.setContent(formattedContent);
        notification.setRead(false);
        notification.setRemoved(false);



        TokenDeviceFirebase tokenDeviceFirebase = tokenDeviceFirebaseRepository.findTokenOfUser(request.getPostOwnerId());
        sendNotification(notificationRepository.save(notification) , tokenDeviceFirebase);
    }

// gửi thông báo đến chủ bài viết khi có những người trực tiếp bình luận vào , không tính những người khác trả lời bình luận
    public void onCommentCreatedToPostOwner1st(NotificationCommentPostOwnerRequest request) {
        UserDTO senderDTO = redisService.getUserById(request.getSenderId()).orElseThrow(
                () -> new AppException(AppErrorCode.UNCATEGORIZED_EXCEPTION)
        );

        String contentSnippet = request.getContent().length() > 15 ? request.getContent().substring(0, 15) + "..." : request.getContent();

        NotificationTemplate template = (request.getCurrentUserCommentNotReplyUser() == 1)
                ? NotificationTemplate.NEW_COMMENT_1
                : NotificationTemplate.NEW_COMMENT_N;


        String formattedContent = (request.getCurrentUserCommentNotReplyUser() == 1)
                ? template.format(senderDTO.getFirstName() + " " + senderDTO.getLastName(), contentSnippet)
                : template.format(senderDTO.getFirstName() + " " + senderDTO.getLastName(), String.valueOf(request.getCurrentUserCommentNotReplyUser() - 1), contentSnippet);

        Notification notification = notificationRepository.findNotification(request.getPostId(), TypeReference.COMMENT_DIRECT_POST)
                .orElse(Notification.builder()
                        .senderId(senderDTO.getId())
                        .type(TypeReference.COMMENT_DIRECT_POST)
                        .referenceId(request.getPostId())
                        .receiverId(request.getPostOwnerId())
                        .isRead(false)
                        .content(formattedContent)
                        .build());


        notification.setContent(formattedContent);
        notification.setRead(false);
        notification.setRemoved(false);

        TokenDeviceFirebase tokenDeviceFirebase = tokenDeviceFirebaseRepository.findTokenOfUser(request.getPostOwnerId());
        sendNotification(notificationRepository.save(notification) , tokenDeviceFirebase);
    }

    // gửi thông báo đến những ngừoi đã bình luận khi có những người trực tiếp bình luận vào , không tính những người khác trả lời bình luận

    public void onCommentCreatedToOtherComment1st(NotificationCommentCreatedToOtherComment1stRequest request) {
        UserDTO senderDTO = redisService.getUserById(request.getSenderId()).orElseThrow(
                () -> new AppException(AppErrorCode.UNCATEGORIZED_EXCEPTION)
        );

        String contentSnippet = request.getContent().length() > 15 ? request.getContent().substring(0, 15) + "..." : request.getContent();

        NotificationTemplate template = (request.getCurrentUserCommentNotReplyUser() == 1)
                ? NotificationTemplate.NEW_COMMENT_TO_OTHER_1
                : NotificationTemplate.NEW_COMMENT_TO_OTHER_N;

        String formattedContent = (request.getCurrentUserCommentNotReplyUser() == 1)
                ? template.format(senderDTO.getFirstName() + " " + senderDTO.getLastName(), contentSnippet)
                : template.format(senderDTO.getFirstName() + " " + senderDTO.getLastName(), String.valueOf(request.getCurrentUserCommentNotReplyUser() - 1), contentSnippet);



        List<TokenDeviceFirebase> tokenDeviceFirebaseList = tokenDeviceFirebaseRepository
                .findByListUserId(request.getOtherCommenterNotPostOwner());

        tokenDeviceFirebaseList.forEach(tokenDeviceFirebase -> {
            Notification notification = notificationRepository
                    .findNotification(request.getPostId(), TypeReference.OTHER_COMMENTER_NOT_POST_OWNER)
                    .orElse(Notification.builder()
                            .senderId(senderDTO.getId())
                            .type(TypeReference.OTHER_COMMENTER_NOT_POST_OWNER)
                            .referenceId(request.getPostId())
                            .receiverId(tokenDeviceFirebase.getUserId())
                            .isRead(false)
                            .content(formattedContent)
                            .build());

            notification.setContent(formattedContent);
            notification.setRead(false);
            notification.setRemoved(false);


            sendNotification(notificationRepository.save(notification), tokenDeviceFirebase);
        });
    }
    public void onReplyCommentToCommentOwner(NotificationCommentReplyRequest request){
        UserDTO senderDTO = redisService.getUserById(request.getSenderId()).orElseThrow(
                () -> new AppException(AppErrorCode.UNCATEGORIZED_EXCEPTION)
        );

        String contentSnippet = request.getCommentContent().length() > 15 ? request.getCommentContent().substring(0, 15) + "..." : request.getCommentContent();


        NotificationTemplate template = (request.getCurrentUserReply1st() == 1)
                ? NotificationTemplate.REPLY_COMMENT_1
                : NotificationTemplate.REPLY_COMMENT_N;

        String formattedContent = (request.getCurrentUserReply1st() == 1)
                ? template.format(senderDTO.getFirstName() + " " + senderDTO.getLastName(), contentSnippet)
                : template.format(senderDTO.getFirstName() + " " + senderDTO.getLastName(), String.valueOf(request.getCurrentUserReply1st() - 1), contentSnippet);




        Notification notification = notificationRepository.findNotification(request.getParentCommentId(), TypeReference.REPLY_COMMENT
        ).orElse(
                Notification.builder()
                        .senderId(senderDTO.getId())
                        .type(TypeReference.REPLY_COMMENT)
                        .referenceId(request.getParentCommentId())
                        .receiverId(request.getReceiverId())
                        .content(formattedContent)
                        .build()
        );

        notification.setContent(formattedContent);
        notification.setRead(false);
        notification.setRemoved(false);

        TokenDeviceFirebase tokenDeviceFirebase = tokenDeviceFirebaseRepository.findTokenOfUser(request.getReceiverId());
        sendNotification(notificationRepository.save(notification), tokenDeviceFirebase);

    }


    //gửi thông báo trả lời bình

    public void onPostRemove(OnPostRemove remove) {
        //remove notification post created
        notificationRepository.onPostRemove(remove.getPostId() , TypeReference.POST_CREATE);
        //remove notification like created
        notificationRepository.onPostRemove(remove.getPostId() , TypeReference.LIKE_POST);
        //remove notification comment 1st to post owner
        notificationRepository.onPostRemove(remove.getPostId() , TypeReference.COMMENT_DIRECT_POST);
        //remove notification comment 1st to post other commenter
        notificationRepository.onPostRemove(remove.getPostId() , TypeReference.OTHER_COMMENTER_NOT_POST_OWNER);
        //remove reply comment to comment owner

    }


//    public void onCommentRemove(OnCommentRemove remove) {
//        // Nếu parentCommentId == null, nghĩa là đây là comment trực tiếp trên post
//        if (remove.getParentCommentId() == null) {
//            if (remove.getCurrentUserComment() == 0) {
//                // Nếu currentUserComment bằng 0 -> xóa comment: đánh dấu notification đã removed
//                Notification notification = notificationRepository
//                        .findNotification(remove.getPostId(), TypeReference.COMMENT_DIRECT_POST)
//                        .orElseThrow(() -> new RuntimeException("Notification not found for post: " + remove.getPostId()));
//                notification.setRemoved(true);
//                notificationRepository.save(notification);
//            } else {
//                // Nếu currentUserComment khác 0 -> cập nhật lại content cho thông báo của poster và other commenter
//
//                // Lấy thông tin người gửi (sender) từ Redis, nếu không tìm thấy sẽ ném exception
//                UserDTO senderDTO = redisService.getUserById(remove.getLatestCommenterId())
//                        .orElseThrow(() -> new RuntimeException("Sender not found with id: " + remove.getLatestCommenterId()));
//
//                // Giả sử remove có phương thức getContent() để lấy nội dung comment
//                String content = remove.getContent();
//                String contentSnippet = content.length() > 15 ? content.substring(0, 15) + "..." : content;
//
//                // Giả sử remove.getCurrentLike() trả về số lượng like hiện tại
//                NotificationTemplate template = (remove.getCurrentLike() == 1)
//                        ? NotificationTemplate.NEW_LIKE_1
//                        : NotificationTemplate.NEW_LIKE_N;
//
//                // Format nội dung thông báo theo template và thông tin của sender
//                String formattedContent = (remove.getCurrentLike() == 1)
//                        ? template.format(senderDTO.getFirstName() + " " + senderDTO.getLastName(), contentSnippet)
//                        : template.format(senderDTO.getFirstName() + " " + senderDTO.getLastName(),
//                        String.valueOf(remove.getCurrentLike() - 1), contentSnippet);
//
//                // Tìm notification cho Like Post; nếu không có thì tạo mới
//                Notification notification = notificationRepository
//                        .findNotification(remove.getPostId(), TypeReference.LIKE_POST)
//                        .orElse(Notification.builder()
//                                .senderId(senderDTO.getId())
//                                .type(TypeReference.LIKE_POST)
//                                .referenceId(remove.getPostId())
//                                .receiverId(remove.getPostOwnerId())
//                                .content(formattedContent)
//                                .build());
//
//                // Cập nhật lại nội dung và trạng thái đã đọc
//                notification.setContent(formattedContent);
//                notification.setRead(false);
//
//                // Lưu notification đã cập nhật
//                notificationRepository.save(notification);
//
//                // Nếu cần gửi thông báo tới thiết bị, bạn có thể lấy token và gọi hàm sendNotification
//                TokenDeviceFirebase tokenDeviceFirebase = tokenDeviceFirebaseRepository.findTokenOfUser(remove.getPostOwnerId());
//                sendNotification(notification, tokenDeviceFirebase);
//            }
//        } else {
//            // Xử lý trường hợp comment là reply (trả lời comment) ở đây
//            // ...
//        }
//    }



    public void deleteCommentSpam(Comment comment) {
        String contentSnippet = comment.getContent().length() > 15
                ? comment.getContent().substring(0, 15) + "..."
                : comment.getContent();

        // Template chứa placeholder
        String template = "Bình luận của bạn đã vi phạm chính sách: <strong>{contentSnippet}</strong>";

        // Thay thế placeholder bằng nội dung snippet
        String formattedContent = template.replace("{contentSnippet}", contentSnippet);

        Notification notification = Notification.builder()
                .type(TypeReference.COMMENT_SPAM)
                .referenceId(comment.getId())
                .receiverId(comment.getUserId())
                .content(formattedContent)
                .build();

        notificationRepository.save(notification);
        TokenDeviceFirebase tokenDeviceFirebase = tokenDeviceFirebaseRepository.findTokenOfUser(notification.getReceiverId());
        sendNotification(notification, tokenDeviceFirebase);




    }


    @SneakyThrows
    private void sendNotification(Notification notification , TokenDeviceFirebase tokenDevice) {


        String payloadJson = objectMapper.writeValueAsString(notification);

        // Xây dựng FCM message với payload dạng JSON
        Message message = Message.builder()
                .setToken(tokenDevice.getTokenDevice())
                .putData("data", payloadJson)
                .build();
        try {
            System.out.println("send to " + message.toString());
            System.out.println(firebaseMessaging.send(message));
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            log.warn("Error while");
        }
    }


    public CountNotificationResponse countNotificationNotRead() {
        String userId = userService.getUserAuthenticationName();
        int notRed = notificationRepository.countNotificationNotRead(userId);
        return CountNotificationResponse.builder()
                .notRead(notRed)
                .build();
    }

    public PageResponse<List<NotificationResponse>> getNotification(int pageNo, int pageSize) {
        String userId = userService.getUserAuthenticationName();
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<NotificationResponse> notificationResponsePage = notificationRepository.getNotification(pageable,userId);
        List<NotificationResponse> responses = notificationResponsePage.getContent();

        notificationRepository.markReadAll(userId);

        return PageResponse.<List<NotificationResponse>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .items(responses)
                .build();

    }
}