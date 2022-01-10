import http from './http-common'

class SearchService {
    search(query, limit, offset) {
        return http.get('search',
            {
                params: {
                    query,
                    limit,
                    offset,
                }
            });
    }
}

export default new SearchService();