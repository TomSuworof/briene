package com.salat.briene.services;

import com.salat.briene.entities.Article;
import com.salat.briene.entities.Comment;
import com.salat.briene.entities.User;
import com.salat.briene.payload.request.CommentUploadRequest;
import com.salat.briene.payload.response.CommentDTO;
import com.salat.briene.repositories.ArticleRepository;
import com.salat.briene.repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    private final ArticleService articleService;

    public CommentDTO uploadComment(User user, CommentUploadRequest commentUploadRequest) {
        Article article = articleService.getArticleById(commentUploadRequest.getArticleId());

        Comment comment = new Comment(commentUploadRequest.getMessage(), user);
        commentRepository.save(comment);

        article.getComments().add(comment);
        articleRepository.save(article);

        return new CommentDTO(comment);
    }
}
