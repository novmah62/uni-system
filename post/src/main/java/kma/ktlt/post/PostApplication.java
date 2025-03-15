package kma.ktlt.post;

import com.opencsv.CSVWriter;
import kma.ktlt.post.domain.common.enumType.CommentStatus;
import kma.ktlt.post.domain.common.enumType.DeleteBy;
import kma.ktlt.post.domain.common.enumType.PostStatus;
import kma.ktlt.post.domain.file.FileService;
import kma.ktlt.post.domain.kafka.CommentDTO;
import kma.ktlt.post.domain.kafka.KafkaProducerService;
import kma.ktlt.post.domain.kafka.PostDTO;
import kma.ktlt.post.domain.post.repository.CommentRepository;
import kma.ktlt.post.domain.post.repository.PostRepository;
import kma.ktlt.post.domain.user.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@SpringBootApplication
public class PostApplication implements CommandLineRunner {
	private final RedisService redisService;
	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final FileService fileService;

	private final KafkaProducerService producerService;
	public static void main(String[] args) {
		SpringApplication.run(PostApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {


//		commentRepository.removeALlCommentOfPostWhenPostRemoved(
//				1L,
//				CommentStatus.AVAILABLE.name(),
//				CommentStatus.DELETED.name(),
//				"adminId",
//				DeleteBy.ADMIN.name()
//		);


//		commentRepository.removeCommentAndAllCommentChildByCommentOwner(
//				1L,CommentStatus.AVAILABLE.name(),CommentStatus.DELETED.name(), "d68c4f48-01c3-4d42-b2c3-39611cb49b46" , DeleteBy.POST_OWNER.name()
//		);



//		commentRepository.removeCommentAndAllCommentChildByAdmin(2L,CommentStatus.AVAILABLE.name(),CommentStatus.DELETED.name(), "d68c4f48-01c3-4d42-b2c3-39611cb49b46" , DeleteBy.POST_OWNER.name());
//		commentRepository.removeCommentAndAllCommentChildByPostOwner(2L,CommentStatus.AVAILABLE.name(), PostStatus.AVAILABLE.name(),CommentStatus.DELETED.name(), "d68c4f48-01c3-4d42-b2c3-39611cb49b46" , DeleteBy.POST_OWNER.name());
//
//		redisService.getAll().stream().forEach(System.out::println);
//		System.out.println(redisService.getUserById("d68c4f48-01c3-4d42-b2c3-39611cb49b46"));
//		commentRepository.removeCommentAndAllCommentChild(2L, CommentStatus.AVAILABLE.name(), CommentStatus.DELETED.name(), "remo---veId" , DeleteBy.MODERATOR.name());
	}
}
