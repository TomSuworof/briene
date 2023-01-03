import http from './http-common'
import authHeader from "@/api/AuthHeader";

class BookmarksService {
    getBookmarksOfUser() {
        return http.get('/bookmarks/getAll', { headers: authHeader() });
    }

    isArticleInBookmarksOfUser(url) {
        return http.get('/bookmarks/isIn',
            {
                headers: authHeader(),
                params: {
                    url: url
                }
            });
    }

    editBookmark(url, action) {
        return http.post('/bookmarks/edit',
            {},
            {
                headers: authHeader(),
                params : {
                    url: url,
                    action: action
                }
            });
    }
}

export default new BookmarksService()