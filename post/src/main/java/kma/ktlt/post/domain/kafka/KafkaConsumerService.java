package kma.ktlt.post.domain.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import kma.ktlt.post.domain.post.service.CommentService;
import kma.ktlt.post.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
//    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
//
//    @KafkaListener(topics = "keycloak-events", groupId = "spring-consumer-group")
//    public void listen(String message) {
//        logger.info("Received message in group - spring-consumer-group: {}", message);
//    }


    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final   CommentService commentService;

    private final PostService postService;


    // Giả sử phản hồi gửi từ spam classifier có cấu trúc giống CommentDTO
    @KafkaListener(topics = "spam-comments", groupId = "feedback-group")
    public void listenSpamFeedback(String message) {
        try {
            // Nếu message là JSON, chuyển đổi về object CommentDTO
            CommentDTO feedback = objectMapper.readValue(message, CommentDTO.class);
            logger.info("Received spam feedback: {}", feedback);

            if(feedback.getLabel().equals("spam")){
                commentService.deleteCommentSpam(Long.valueOf(feedback.getCommentId()));
            }

        } catch (Exception e) {
            logger.error("Error processing feedback message: {}", message, e);
        }
    }

    @KafkaListener(topics = "spam-posts", groupId = "feedback-group")
    public void listenSpamFeedback1(String message) {
        try {
            // Nếu message là JSON, chuyển đổi về object CommentDTO
            PostDTO feedback = objectMapper.readValue(message, PostDTO.class);
            logger.info("Received spam feedback: {}", feedback);

            if(feedback.getLabel().equals("spam")){
                postService.deletePostBySpam(Long.valueOf(feedback.getPostId()));
            }

        } catch (Exception e) {
            logger.error("Error processing feedback message: {}", message, e);
        }
    }
}
