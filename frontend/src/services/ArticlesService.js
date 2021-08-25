import http from '../http-common'

class ArticlesService {
    getPublishedArticles() {
        return http.get('/articles');
    }

    getArticleById(id, token) {
        return http.get(`/articles/${id}`, { headers: {"Authorization" : `Bearer ${token}`} });
    }

    getRawArticle(id, token) {
        return http.get(`/articles/${id}?raw=true`, { headers: {"Authorization" : `Bearer ${token}`} });
    }

    loadArticle(title, content, action, token) {
        return http.post('/articles/load',
            {},
            {
                headers: {"Authorization" : `Bearer ${token}`},
                params : {
                    title: title,
                    content: content,
                    action: action
                }
            });
    }

    getMyArticles(state, token) {
        return http.get('/articles/my',
            {
                headers: {"Authorization" : `Bearer ${token}`},
                params: {
                    state: state,
                }
            });
    }

    delete(id, token) {
        return http.delete(`/articles/${id}`, { headers: {"Authorization" : `Bearer ${token}`} });
    }
}

export default new ArticlesService()