module.exports = {
    devServer: {
        port: 8080,
        proxy: {
            '^/api': {
                target: process.env.NODE_ENV === 'docker' ? 'http://briene-backend:8081' : "http://localhost:8081",
                ws: true,
                changeOrigin: true
            }
        }
    },
    assetsDir: 'static'
};