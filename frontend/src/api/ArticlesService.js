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

    getArticleForRender(url) {
        return http.get(`/articles/${url}`, { headers: authHeader() });
    }

    getArticleRaw(id) {
        return http.get(`/articles/edit/${id}`, { headers: authHeader() });
    }

    getSharedArticle(id) {
        return http.get(`/articles/share/${id}`);
    }

    getNextArticle(url) {
        return http.get('/articles/next', { params: { url } });
    }

    getPrevArticle(url) {
        return http.get('/articles/prev', { params: { url } });
    }

    getSuggestedArticles(url) {
        return http.get('/articles/suggested', { params: { url } });
    }

    uploadArticle(id, title, content, summary, action, tags, url) {
        return http.post('/articles/upload',
            {
                id,
                title,
                content,
                summary,
                tags,
                url
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