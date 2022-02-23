import http from './http-common';

class TagService {
    getTagsWithExclusion(excludedTags) {
        return http.post('/tags/getTags',
            excludedTags,
            {});
    }

    getArticlesByTag(tag, limit, offset) {
        return http.get('/tags/getArticlesByTag',
            {
                params: {
                    tag: tag,
                    limit: limit,
                    offset: offset,
                }
            });
    }
}

export default new TagService();