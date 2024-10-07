const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})

module.exports = {
  configureWebpack: {
    devServer: {
      port: 8082,
      // open: true,
      host: "localhost",
      proxy: {
        '/api': {
          target: 'http://121.41.109.201:8080',
          ws: true,
          changeOrigin: true,
          pathRewrite: {
            '^/api': '/'
          }
        }
      }
    }
  }
};