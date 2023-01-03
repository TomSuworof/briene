import axiosInstance from "./http-common";

const setupInterceptors = (store, router) => {
    axiosInstance.interceptors.response.use(
        (res) => {
            return res;
        },
        async (err) => {
            const originalConfig = err.config;

            // we should exclude urls 'auth/login', 'bookmarks/isIn', 'authors/isFollowing'
            // because NotAuthorized error on these endpoints does not make sense
            if (originalConfig.url !== "/auth/login" &&
                originalConfig.url !== "/bookmarks/isIn" &&
                originalConfig.url !== "/authors/isFollowing" &&
                err.response) {
                // Access Token was expired
                if (err.response.status === 401 || err.response.status === 400) {
                    originalConfig._retry = true;
                    store.dispatch("auth/logout");
                    router.push('/login');
                }
            }
            return Promise.reject(err);
        }
    );
}

export default setupInterceptors;