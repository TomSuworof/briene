import http from './http-common';

class TagService {
    getTagsWithExclusion(excludedTags) {
        return http.post('/tags/getTags',
            excludedTags,
            {});
    }
}

export default new TagService();