import http from '../http-common'
import authHeader from './AuthHeader';

const API_URL = 'http://localhost:8080/api/test/';

class UserService {
    getPersonalArea() {
        return http.get(API_URL + 'user', { headers: authHeader() });
    }

    getAdminBoard() {
        return http.get(API_URL + 'admin', { headers: authHeader() });
    }
} // todo create something useful

export default new UserService();