import http from '../http-common'
import authHeader from "@/services/AuthHeader";

class ArticlesService {
    getPublishedArticles() {
        return http.get('/articles');
    }

    getPublishedArticlesPaginated(limit, offset) {
        return http.get('/articles/get', {
            params: {
                limit: limit,
                offset: offset,
            }
        });
    }

    getArticleById(id) {
        return http.get(`/articles/${id}`, { headers: authHeader() });
    }

    getRawArticle(id) {
        return http.get(`/articles/${id}?raw=true`, { headers: authHeader() });
    }

    loadArticle(title, content, action) {
        return http.post('/articles/load',
            {},
            {
                headers: authHeader(),
                params : {
                    title: title,
                    content: content,
                    action: action
                }
            });
    }

    getMyArticles(state) {
        return http.get('/articles/my',
            {
                headers: authHeader(),
                params: {
                    state: state,
                }
            });
    }

    delete(id) {
        return http.delete(`/articles/${id}`, { headers: authHeader() });
    }
}

export default new ArticlesService()