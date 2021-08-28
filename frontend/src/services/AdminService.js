import http from '../http-common'
import authHeader from "@/services/AuthHeader";

class AdminService {
    getAllUsers() {
        return http.get('admin/users',
            {
                headers: authHeader(),
            });
    }

    getAllArticles(state) {
        return http.get('admin/articles',
            {
                headers: authHeader(),
                params: {
                    state: state,
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