import http from '../http-common'
import authHeader from "@/services/AuthHeader";

class BookmarksService {
    getBookmarksOfUser() {
        return http.get('/bookmarks/getAll', { headers: authHeader() });
    }

    isArticleInBookmarksOfUser(id) {
        return http.get('/bookmarks/isIn',
            {
                headers: authHeader(),
                params: {
                    id: id
                }
            })
    }

    editBookmark(id, action) {
        return http.post('/bookmarks/edit',
            {},
            {
                headers: authHeader(),
                params : {
                    id: id,
                    action: action
                }
            });
    }
}

export default new BookmarksService()