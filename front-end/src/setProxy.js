const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
    app.use(
        proxy('/', {
            target: 'http://localhost:8080', // Spring 포트
            changeOrigin: true
        })
    );
};
