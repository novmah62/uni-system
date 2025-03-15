package kma.ktlt.post.domain.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    @Autowired
    private  KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();


    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendComment(CommentDTO comment) {

        try {
            String commentJson = objectMapper.writeValueAsString(comment);
            kafkaTemplate.send("comments", commentJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPost(PostDTO postDTO) {

        try {
            String commentJson = objectMapper.writeValueAsString(postDTO);
            kafkaTemplate.send("posts", commentJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
