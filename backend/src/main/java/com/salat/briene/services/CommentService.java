package com.salat.briene.services;

import com.salat.briene.entities.Article;
import com.salat.briene.entities.Comment;
import com.salat.briene.entities.User;
import com.salat.briene.payload.request.CommentUploadRequest;
import com.salat.briene.payload.response.CommentDTO;
import com.salat.briene.repositories.ArticleRepository;
import com.salat.briene.repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.mail.EmailException;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    private final ArticleService articleService;
    private final MailService mailService;

    public CommentDTO uploadComment(User user, CommentUploadRequest commentUploadRequest) throws EmailException {
        Article article = articleService.getArticleById(commentUploadRequest.getArticleId());

        Comment comment = new Comment(commentUploadRequest.getMessage(), user);
        commentRepository.save(comment);

        article.getComments().add(comment);
        articleRepository.save(article);

        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                mailService.sendNotificationAboutComment(article, comment);
            } catch (EmailException e) {
                throw new RuntimeException(e);
            }
        });

        return new CommentDTO(comment);
    }
}
