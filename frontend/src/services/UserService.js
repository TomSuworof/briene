import http from '../http-common'
import authHeader from "@/services/AuthHeader";

class UserService {
    editUser(id, password, newUserData) {
        return http.post('/users/edit',
            {},
            {
                headers: authHeader(),
                params: {
                    id: id,
                    password: password,
                    email: newUserData.email,
                    bio: newUserData.bio,
                    passwordNew: newUserData.passwordNew,
                }
            });
    }
}

export default new UserService();