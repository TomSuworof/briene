import http from './http-common'
import authHeader from "@/api/AuthHeader";

class AvatarsService {
    getUserAvatar(username) {
        return http.get(`/avatars/${username}`);
    }
}

export default new AvatarsService()