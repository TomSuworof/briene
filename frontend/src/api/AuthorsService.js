import http from './http-common'

class ArticlesService {
    getAll() {
        return http.get('/authors')
    }

    getAuthorData(authorName, limit, offset) {
        return http.get(`/authors/${authorName}`, {
            params: {
                limit: limit,
                offset: offset
            }
        })
    }
}

export default new ArticlesService()