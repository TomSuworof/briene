import http from './http-common'

class PasswordResetService {
    sendPasswordResetRequest(username) {
        return http.post('password_reset/create_request',
            {},
            {
                params: {
                    username: username,
                }
            });
    }

    changePassword(code, newPassword) {
        return http.post('password_reset/change_password',
            {
                requestId: code,
                newPassword: newPassword
            });
    }
}

export default new PasswordResetService()