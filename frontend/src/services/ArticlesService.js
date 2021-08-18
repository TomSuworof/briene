import http from '../http-common'

class ArticlesService {
    getPublishedArticles() {
        return http.get('/articles')
    }

    getArticleById(id) {
        return http.get(`/articles/${id}`)
    }

    // create(data) {
    //     return http.post('/customers', data)
    // }
    //
    // update(id, data) {
    //     return http.put(`/customers/${id}`, data)
    // }
    //
    // delete(id) {
    //     return http.delete(`/customers/${id}`)
    // }
}

export default new ArticlesService()