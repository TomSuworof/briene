import http from '../http-common'

class AdminService {
    getAllUsers(token) {
        return http.get('admin/users',
            {
                headers: {"Authorization" : `Bearer ${token}`},
            });
    }

    getAllArticles(state, token) {
        return http.get('admin/articles',
            {
                headers: {"Authorization" : `Bearer ${token}`},
                params: {
                    state: state,
                }
            });
    }

    editUser(action, id, token) {
        return http.post('admin/edit_user',
            {},
            {
                headers: {"Authorization" : `Bearer ${token}`},
                params: {
                    action: action,
                    id: id,
                }
            });
    }
}

export default new AdminService()