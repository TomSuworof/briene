import http from './http-common';
import authHeader from "@/api/AuthHeader";

class AdminService {
    getUsersPaginated(limit, offset) {
        return http.get('/admin/users',
            {
                headers: authHeader(),
                params: {
                    limit: limit,
                    offset: offset
                }
            });
    }

    getArticlesPaginated(state, limit, offset) {
        return http.get('admin/articles',
            {
                headers: authHeader(),
                params: {
                    state: state,
                    limit: limit,
                    offset: offset
                }
            });
    }

    editUser(action, id) {
        return http.post('admin/edit_user',
            {},
            {
                headers: authHeader(),
                params: {
                    action: action,
                    id: id,
                }
            });
    }
}

export default new AdminService()