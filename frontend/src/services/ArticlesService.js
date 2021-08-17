import http from '../http-common'

class ArticlesService {
    getAll() {
        return http.get('/articles')
    }

    get(id) {
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