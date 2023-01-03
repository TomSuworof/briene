import http from './http-common'
import authHeader from "@/api/AuthHeader";

class AuthorsService {
    getAuthorData(authorName, limit, offset) {
        return http.get(`/authors/${authorName}`,
            {
                params: {
                    limit: limit,
                    offset: offset
                }
            });
    }

    subscribe(authorName) {
        return http.post('/authors/subscribe',
            {},
            {
                headers: authHeader(),
                params: {
                    author: authorName
                }
            });
    }

    unsubscribe(authorName) {
        return http.post('/authors/unsubscribe',
            {},
            {
                headers: authHeader(),
                params: {
                    author: authorName
                }
            });
    }

    getFollowingStatus(authorName) {
        return http.get('/authors/isFollowing',
            {
                headers: authHeader(),
                params: {
                    authorName: authorName
                }
            });
    }
}

export default new AuthorsService()