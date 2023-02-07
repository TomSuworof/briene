import http from './http-common';

class ImageService {
    getImage(id) {
        return http.get('/images/get',
            {
                params: {
                    id: id
                }
            });
    }

    uploadImage(content) {
        return http.post("/images/upload",
            {
                content: content
            });
    }
}

export default new ImageService();