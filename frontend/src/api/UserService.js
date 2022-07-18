import http from './http-common'
import authHeader from "@/api/AuthHeader";

class UserService {
    editUser(id, password, newUserData) {
        return http.post('/users/edit',
            newUserData,
            {
                headers: authHeader(),
                params: {
                    id: id,
                    password: password
                }
            });
    }
}

export default new UserService();