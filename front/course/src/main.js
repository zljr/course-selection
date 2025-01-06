import Vue from 'vue'
import App from './App.vue'
import router from './router'
import '@/assets/css/global.css'
import '@/assets/css/iconfont/iconfont.css'
import '@/assets/css/info/iconfont.css'
import '@/assets/css/courseInfo/iconfont.css'
import '@/assets/css/courseTable/iconfont.css'
import '@/assets/css/course/iconfont.css'
import '@/assets/css/courseperson/iconfont.css'
import '@/assets/css/selectCourse/iconfont.css'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css';
// 全局引入自己封装的request.js
import request from '@/utils/request';
// 代码高亮的样式
import 'highlight.js/styles/monokai-sublime.css'
Vue.config.productionTip = false

Vue.use(ElementUI,{size:'small'});

Vue.prototype.$request=request
Vue.prototype.$baseUrl=process.env.VUE_APP_BASEURL

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
