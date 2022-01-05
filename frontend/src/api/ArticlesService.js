import http from './http-common'
import authHeader from "@/api/AuthHeader";

class ArticlesService {
    getPublishedArticlesPaginated(limit, offset) {
        return http.get('/articles/get', {
            params: {
                limit: limit,
                offset: offset,
            }
        });
    }

    getArticleForRender(id) {
        return http.get(`/articles/${id}`, { headers: authHeader() });
    }

    getArticleRaw(id) {
        return http.get(`/articles/${id}?raw=true`, { headers: authHeader() });
    }

    uploadArticle(title, content, summary, action) {
        return http.post('/articles/upload',
            {
                title: title,
                content: content,
                summary: summary,
            },
            {
                headers: authHeader(),
                params : {
                    action: action
                }
            });
    }

    getMyArticles(state) {
        return http.get('/articles/my_articles',
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