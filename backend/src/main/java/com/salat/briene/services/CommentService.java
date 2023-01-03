package com.salat.briene.services;

import com.salat.briene.entities.Article;
import com.salat.briene.entities.Comment;
import com.salat.briene.entities.User;
import com.salat.briene.payload.request.CommentUploadRequest;
import com.salat.briene.payload.response.CommentDTO;
import com.salat.briene.payload.response.PageResponseDTO;
import com.salat.briene.repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.mail.EmailException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleService articleService;
    private final MailService mailService;

    public PageResponseDTO<CommentDTO> getPageWithCommentsByArticleUrl(String articleUrl, Integer limit, Integer offset) {
        Page<Comment> comments = commentRepository.getCommentsByArticle_Url(articleUrl, getDefaultPageable(limit, offset));

        // return response after filtering
        return new PageResponseDTO<>(
                offset > 0 && comments.getTotalElements() > 0,
                (offset + limit) < comments.getTotalElements(),
                comments.getContent().stream().map(CommentDTO::new).toList(),
                comments.getTotalElements());
    }

    public CommentDTO uploadComment(User user, CommentUploadRequest commentUploadRequest) {
        Article article = articleService.getArticleById(commentUploadRequest.getArticleId());

        Comment comment = new Comment(commentUploadRequest.getMessage(), user, article);
        commentRepository.save(comment);

        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                mailService.sendNotificationAboutComment(article, comment);
            } catch (EmailException e) {
                throw new RuntimeException(e);
            }
        });

        return new CommentDTO(comment);
    }

    private Pageable getDefaultPageable(Integer limit, Integer offset) {
        return PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.ASC, "publicationDate"));
    }
}
