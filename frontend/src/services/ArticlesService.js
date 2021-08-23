import http from '../http-common'

class ArticlesService {
    getPublishedArticles() {
        return http.get('/articles');
    }

    getArticleById(id, accessToken) {
        return http.get(`/articles/${id}`, {headers: {"Authorization" : `Bearer ${accessToken}`}});
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