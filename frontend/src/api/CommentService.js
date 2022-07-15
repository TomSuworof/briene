import http from './http-common'
import authHeader from "@/api/AuthHeader";

class CommentService {
    uploadComment(articleId, message) {
        return http.post('/comments/upload',
            {
                articleId,
                message
            },
            {
                headers: authHeader(),
            });
    }

    delete(id) {
        return http.delete(`/comments/${id}`, { headers: authHeader() });
    }
}

export default new CommentService()