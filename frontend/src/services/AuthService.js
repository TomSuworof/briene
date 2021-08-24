import http from '../http-common'

class AuthService {
    login(user) {
        return http
            .post('/auth/login', {
                username: user.username,
                password: user.password
            })
            .then(response => {
                if (response.data.token) {
                    localStorage.setItem('user', JSON.stringify(response.data));
                }

                return response.data;
            });
    }

    logout() {
        localStorage.removeItem('user');
    }

    register(user) {
        return http.post('auth/signup', {
            username: user.username,
            email: user.email,
            secretQuestion: user.secretQuestion,
            secretAnswer: user.secretAnswer,
            password: user.password
        });
    }
}

export default new AuthService();