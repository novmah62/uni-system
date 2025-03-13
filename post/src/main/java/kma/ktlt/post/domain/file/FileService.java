package kma.ktlt.post.domain.file;

import com.opencsv.CSVWriter;
import kma.ktlt.post.domain.common.enumType.DeleteBy;
import kma.ktlt.post.domain.post.entity.Comment;
import kma.ktlt.post.domain.post.entity.Post;
import kma.ktlt.post.domain.post.repository.CommentRepository;
import kma.ktlt.post.domain.post.repository.PostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileService {

    final CommentRepository commentRepository;
    private final PostRepository postRepository;

    private void writeSpamPost(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String nowStr = LocalDateTime.now().format(formatter);

        // Xây dựng tên file CSV
        StringBuilder csvFile = new StringBuilder();
        csvFile.append("posts");
        csvFile.append(nowStr);
        csvFile.append(".csv");

        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile.toString()))) {
            // Ghi tiêu đề cột (header)
            String[] header = {"postId", "content", "label"};
            writer.writeNext(header);

            List<Post> spamPosts = postRepository.findSpamPost(DeleteBy.ADMIN);
            spamPosts.forEach(spam -> {
                String[] row = {
                        spam.getId().toString(),
                        spam.getContent(),
                        "spam"
                };
                writer.writeNext(row);
            });

            System.out.println("CSV file written successfully: " + csvFile.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeSpamComment(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String nowStr = LocalDateTime.now().format(formatter);

        // Xây dựng tên file CSV
        StringBuilder csvFile = new StringBuilder();
        csvFile.append("comments");
        csvFile.append(nowStr);
        csvFile.append(".csv");

        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile.toString()))) {
            // Ghi tiêu đề cột (header)
            String[] header = {"commentId", "content", "label"};
            writer.writeNext(header);

            List<Comment> spamComments = commentRepository.findSpamComment(DeleteBy.ADMIN);
            spamComments.forEach(spamComment -> {
                String[] row = {
                        spamComment.getId().toString(),
                        spamComment.getContent(),
                        "spam"
                };
                writer.writeNext(row);
            });

            System.out.println("CSV file written successfully: " + csvFile.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
