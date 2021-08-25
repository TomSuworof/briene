import http from '../http-common'

class BookmarksService {
    getBookmarksOfUser(token) {
        return http.get('/bookmarks/getAll', { headers: {"Authorization" : `Bearer ${token}`} });
    }

    isArticleInBookmarksOfUser(id, token) {
        return http.get('/bookmarks/isIn',
            {
                headers: {"Authorization" : `Bearer ${token}`},
                params: {
                    id: id
                }
            })
    }

    editBookmark(id, action, token) {
        return http.post('/bookmarks/edit',
            {},
            {
                headers: {"Authorization" : `Bearer ${token}`},
                params : {
                    id: id,
                    action: action
                }
            });
    }
}

export default new BookmarksService()