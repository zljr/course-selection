const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer:{

    port:7010
  
   },
  
   chainWebpack:config =>{
  
    config.plugin('html')
  
    .tap(args =>{
  
     args[0].title="选课系统";
  
     return args;
    })
  }
})
