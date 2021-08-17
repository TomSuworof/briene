import http from '../http-common'

class ArticlesService {
    getAll() {
        return http.get('/authors')
    }

    get(authorName) {
        return http.get(`/authors/${authorName}`)
    }
}

export default new ArticlesService()