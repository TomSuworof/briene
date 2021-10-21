import http from './http-common'

class ArticlesService {
    getAll() {
        return http.get('/authors')
    }

    getAuthorData(authorName) {
        return http.get(`/authors/${authorName}`)
    }
}

export default new ArticlesService()