module.exports = {
    devServer: {
        port: 8080,
        proxy: {
            '^/api': {
                target: 'http://briene-backend:8081', // change to localhost, if not running in docker
                ws: true,
                changeOrigin: true
            }
        }
    },
    assetsDir: 'static'
};