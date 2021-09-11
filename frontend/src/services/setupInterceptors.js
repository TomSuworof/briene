import axiosInstance from "../http-common";

const setupInterceptors = (store, router) => {
    axiosInstance.interceptors.response.use(
        (res) => {
            return res;
        },
        async (err) => {
            const originalConfig = err.config;

            if (originalConfig.url !== "/auth/login" && err.response) {
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