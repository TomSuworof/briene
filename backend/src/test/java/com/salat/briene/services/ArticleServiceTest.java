// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.salat.briene.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.salat.briene.entities.Article;
import com.salat.briene.entities.ArticleState;
import com.salat.briene.entities.RoleEnum;
import com.salat.briene.entities.User;
import com.salat.briene.exceptions.ArticleNotFoundException;
import com.salat.briene.repositories.ArticleRepository;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {
    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private TagService tagService;

    @Mock
    private AuthorService authorService;

    @Mock
    private SearchService searchService;

    @Test
    public void testSavePublishedArticle() {
        ArticleService articleService = new ArticleService(articleRepository, tagService, authorService, searchService);

        Article newArticle = new Article();
        newArticle.setState(ArticleState.PUBLISHED);
        newArticle.setAuthor(new User());

        assertDoesNotThrow(() -> articleService.saveArticle(newArticle));
    }

    @Test
    public void testSaveDraftArticle() {
        ArticleService articleService = new ArticleService(articleRepository, tagService, authorService, searchService);

        Article newArticle = new Article();
        newArticle.setId(UUID.randomUUID());

        assertDoesNotThrow(() -> articleService.saveDraft(newArticle));
    }

    @Test
    public void testPublish() {
        ArticleService articleService = new ArticleService(articleRepository, tagService, authorService, searchService);

        Article newArticle = new Article();
        newArticle.setTitle("Test Title");
        newArticle.setState(ArticleState.PUBLISHED);
        newArticle.setUrl("test-url");
        newArticle.setAuthor(new User());

        assertDoesNotThrow(() -> articleService.publish(newArticle));
    }

    @Test
    public void testDeleteArticleById() {
        ArticleService articleService = new ArticleService(articleRepository, tagService, authorService, searchService);

        UUID articleId = UUID.randomUUID();

        when(articleRepository.findById(articleId)).thenReturn(Optional.of(new Article()));

        assertDoesNotThrow(() -> articleService.deleteArticleById(articleId));
    }

    @Test
    public void testGetArticleById() {
        ArticleService articleService = new ArticleService(articleRepository, tagService, authorService, searchService);

        UUID id = UUID.randomUUID();

        when(articleRepository.findById(id)).thenReturn(Optional.of(new Article()));

        Article article = articleService.getArticleById(id);

        assertNotNull(article);
    }

    @Test
    public void testGetArticleByUrl() {
        ArticleService articleService = new ArticleService(articleRepository, tagService, authorService, searchService);

        String url = "test-url";

        when(articleRepository.findByUrl(url)).thenReturn(Optional.of(new Article()));

        Article article = articleService.getArticleByUrl(url);

        assertNotNull(article);
    }

    @Test
    public void testGetNextArticleFromEmptyListOfArticles() {
        ArticleService articleService = new ArticleService(articleRepository, tagService, authorService, searchService);

        String url = "test-url";
        String authorUsername = "test-author";

        User author = new User();
        author.setUsername(authorUsername);

        Article article = new Article();
        article.setUrl(url);
        article.setAuthor(author);

        when(articleRepository.findByUrl(url))
                .thenReturn(Optional.of(article));
        when(articleRepository.findArticlesByStateAndAuthor_Username(ArticleState.PUBLISHED, authorUsername))
                .thenReturn(new ArrayList<>() {
                    {
                        add(article);
                    }
                });

        assertThrows(ArticleNotFoundException.class, () -> articleService.getNextArticle(url));
    }

    @Test
    public void testGetPreviousArticleFromEmptyListOfArticles() {
        ArticleService articleService = new ArticleService(articleRepository, tagService, authorService, searchService);

        String url = "test-url";
        String authorUsername = "test-author";

        User author = new User();
        author.setUsername(authorUsername);

        Article article = new Article();
        article.setUrl(url);
        article.setAuthor(author);

        when(articleRepository.findByUrl(url))
                .thenReturn(Optional.of(article));
        when(articleRepository.findArticlesByStateAndAuthor_Username(ArticleState.PUBLISHED, authorUsername))
                .thenReturn(new ArrayList<>() {
                    {
                        add(article);
                    }
                });

        assertThrows(ArticleNotFoundException.class, () -> articleService.getPreviousArticle(url));
    }

    @Test
    public void testCanUserAuthorEditArticle() {
        ArticleService articleService = new ArticleService(articleRepository, tagService, authorService, searchService);

        User user = new User();
        user.setRoles(Set.of(RoleEnum.USER.getAsObject()));

        Article article = new Article();
        article.setAuthor(user);

        assertEquals(true, articleService.canUserEditArticle(user, article));
    }

    @Test
    public void testCanUserNotAuthorEditArticle() {
        ArticleService articleService = new ArticleService(articleRepository, tagService, authorService, searchService);

        User user = new User();
        user.setRoles(Set.of(RoleEnum.USER.getAsObject()));

        Article article = new Article();

        assertEquals(false, articleService.canUserEditArticle(user, article));
    }

    @Test
    public void testCanUserAdminEditArticle() {
        ArticleService articleService = new ArticleService(articleRepository, tagService, authorService, searchService);

        User user = new User();
        user.setRoles(Set.of(RoleEnum.ADMIN.getAsObject()));

        Article article = new Article();

        assertEquals(true, articleService.canUserEditArticle(user, article));
    }
}
