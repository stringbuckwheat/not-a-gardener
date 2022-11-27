const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = app => {
  app.use(
    createProxyMiddleware(
      ['/', '/idCheck'],
      {
        target: 'http://localhost:8080',
        changeOrigin: true,
        ws: true,
        router: {
          '/': 'http://localhost:8080',
          '/idCheck': 'http://localhost:8080'
        }
      }
    )
  )
}
