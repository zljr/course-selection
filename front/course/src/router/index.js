import Vue from 'vue'
import VueRouter from 'vue-router'










Vue.use(VueRouter)

const routes = [
          {
            path: '/',
            name: 'manager',
            redirect:'/home',//这里是有斜杠的，重定向到主页
            component: ()=>import('../views/Manager.vue'),
            children:[{
              path:'home',//不用加斜杠，会自动把父级路由和子级路由拼接起来
              name:'home',
              meta:{
                name:'系统首页'
              },
              component:()=>import('../views/manager/Home.vue')
            },{
              path:'403',
              name:'auth',
              meta:{
                name:'无权限'
              },
              component:()=>import('../views/manager/Auth.vue')
            },
            {
              path:'user',
              name:'user',
              meta:{
                name:'用户管理'
              },
              component:()=>import('../views/manager/User.vue')
            },{
              path:'personInfo',
              name:'personInfo',
              meta:{
                name:'个人信息'
              },
              component:()=>import('../views/manager/PersonInfo.vue')
            },{
              path:'password',
              name:'password',
              meta:{
                name:'修改密码'
              },
              component:()=>import('../views/manager/Password.vue')
            },
            {
              path:'course',
              name:'course',
              meta:{
                name:'课程信息'
              },
              component:()=>import('../views/manager/Course.vue')
            },
            {
              path:'teacherCourse',
              name:'teacherCourse',
              meta:{
                name:'教师课程'
              },
              component:()=>import('../views/manager/TeacherCourse.vue')
            },{
              path:'myCourseTable',
              name:'myCourseTable',
              meta:{
                name:'我的课表'
              },
              component:()=>import('../views/manager/MyCourseTable.vue')
            },{
              path:'allCourse',
              name:'allCourse',
              meta:{
                name:'全部课程'
              },
              component:()=>import('../views/manager/AllCourse.vue')
            },{
              path:'allCourseTable',
              name:'allCourseTable',
              meta:{
                name:'全部课表'
              },
              component:()=>import('../views/manager/AllCourseTable.vue')
            },{
              path:'enrolledCourse',
              name:'enrolledCourse',
              meta:{
                name:'已选课程'
              },
              component:()=>import('../views/manager/EnrolledCourse.vue')
            },{
              path:'toEnrollCourse',
              name:'toEnrollCourse',
              meta:{
                name:'可选课程',
              },
              component:()=>import('../views/manager/ToEnrollCourse.vue')
            },{
              path:'studentCourseTable',
              name:'studentCourseTable',
              meta:{
                name:'我的课表'
              },
              component:()=>import('../views/manager/StudentCourseTable.vue')
            },{
              path:'teacherCourseCount',
              name:'teacherCourseCount',
              meta:{
                name:'选课人数统计'
              },
              component:()=>import('../views/manager/TeacherCourseCount.vue')
            }
          ]
          },
          {
            path: '/about',
            name: 'about',
            meta:{
              name:'关于'
            },
            component: ()=> import('../views/AboutView.vue')
          },
          {
            path:'/login',
            name:'login',
            meta:{
              name:'登录'
            },
            component:()=>import('../views/Login.vue')
          },
          {
            path:'/register',
            name:'register',
            meta:{
              name:'注册'
            },
            component:()=>import('../views/Register.vue')
          },
          {
            path:'*',
            name:'404',
            meta:{
              name:'找不到页面'
            },
            component:()=>import('../views/404.vue')
          },
      

]

const router = new VueRouter({
  mode: 'hash',
  base: process.env.BASE_URL,
  routes
})
// 重复点击当前的路由的时候会报错，这里来处理一下
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location){
  return originalPush.call(this,location).catch(error=>error)
}

router.beforeEach((to,from,next)=>{
  //to是到达的路由
  //from是来源的路由
  //next是帮助跳转的函数
  let adminPath=['/user', '/admin']
  let allowUser=['教师','管理员']
  let user=JSON.parse(localStorage.getItem('userData')||'{}')
  if((!allowUser.includes(user.role))&&adminPath.includes(to.path)){
    next('/403')
  }else{
    next()
  }
})

export default router
