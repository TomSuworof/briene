import http from '../http-common'

class ArticlesService {
    getPublishedArticles() {
        return http.get('/articles');
    }

    getArticleById(id) {
        return http.get(`/articles/${id}`);
    }

    publishArticle(title, content, state) {
        return http.post('/articles/load', {
            title: title,
            content: content,
            state: state,
        });
    }

    delete(id) {
        return http.delete(`/articles/${id}`);
    }
}

export default new ArticlesService()