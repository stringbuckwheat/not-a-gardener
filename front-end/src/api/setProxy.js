const {proxy} = require('http-proxy-middleware');

module.exports = function (app) {
  app.use(
    proxy('/', {
      target: process.env.REACT_APP_API_URL,
      changeOrigin: true
    })
  );
};
