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

    getNextArticle(id) {
        return http.get('/articles/next', { params: { id } });
    }

    getPrevArticle(id) {
        return http.get('/articles/prev', { params: { id } });
    }

    uploadArticle(title, content, summary, action, tags) {
        return http.post('/articles/upload',
            {
                title,
                content,
                summary,
                tags
            },
            {
                headers: authHeader(),
                params : {
                    action: action
                }
            });
    }

    getMyArticlesPaginated(state, limit, offset) {
        return http.get('/articles/my_articles',
            {
                headers: authHeader(),
                params: {
                    state: state,
                    limit: limit,
                    offset: offset
                }
            });
    }

    delete(id) {
        return http.delete(`/articles/${id}`, { headers: authHeader() });
    }
}

export default new ArticlesService()