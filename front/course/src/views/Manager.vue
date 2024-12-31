<template>
  <div class="home">
    <!-- 左侧 -->
<el-container>
  <!-- 侧边栏 -->
   <el-aside :width="asideWidth" style="min-height: 100vh;background-color: #001529;">
    <!-- 取消侧边栏的滑动，改为浏览器窗口高度 -->
    <div style="height:60px; display:flex; align-items: center;justify-content: center; ">
      <img src="@/assets/xuanke.png" alt="" style="width:200px;height:60px;">
      <!-- <transition name="el-fade-in">
        <span style="color:#fff;font-size: 20px;" v-show="!isCollapse" >xhf</span>
      </transition> -->
      
    </div>
    <el-menu :default-openeds="['info']" :collapse="isCollapse" :collapse-transition="false" router background-color=" #001529" text-color="rgba(255,255,255,0.65)" active-text-color="#fff" style="border: none;" :default-active="$route.path">
      <!-- collapse-transition,收缩的时候动画太丑，关掉 -->
      <!--background-color:设置菜单栏背景颜色，并不是在style里面设置的，text-color:文本颜色  active-text-color:激活时的文本颜色，点击这个或者是浏览器路径这这个的路由  -->
      <!-- $route.path为当前浏览器正在访问的路由 -->
      <!-- 设置首页默认高亮(会变成蓝色)，即地址为"/"的页面 -->
      <!-- 取消右边框 -->
      <!-- 系统首页 -->
      <el-menu-item index="/home">
        <!-- 当且仅当标签为el-menu-item的时候，index属性才会生效 ,会跟defaule-active对应上-->
        
        <i class="el-icon-s-home"></i>
        <span slot="title">系统首页</span>
      
      </el-menu-item>

      <!-- 信息管理，二级菜单,这里的index是什么作用的 -->
        <el-submenu index="info" v-if="user.role === '管理员'">
          <template slot="title">
            <i class="el-icon-menu"></i>
              <span slot="title">信息管理</span>
          </template>
     <el-menu-item index="/user">
        <i class="el-icon-xinxi"></i>
        <span slot="title">用户信息</span>
      </el-menu-item>
      <el-menu-item index="/course">
        <i class="el-icon-kecheng"></i>
        <span slot="title">课程信息</span>
      </el-menu-item>
      <el-menu-item index="/allCourse">
        <i class="el-icon-kecheng"></i>
        <span slot="title">全部课程</span>
      </el-menu-item>
      <el-menu-item index="/allCourseTable">
        <i class="el-icon-kecheng"></i>
        <span slot="title">全部课表</span>
      </el-menu-item>
      </el-submenu>
      <el-menu-item index="/teacherCourse" v-if="user.role === '教师'">
        <!-- 当且仅当标签为el-menu-item的时候，index属性才会生效 ,会跟defaule-active对应上-->
        
        <i class="el-icon-kecheng"></i>
        <span slot="title">我的课程</span>
      
      </el-menu-item>
      <el-menu-item index="/myCourseTable" v-if="user.role === '教师'">
        <!-- 当且仅当标签为el-menu-item的时候，index属性才会生效 ,会跟defaule-active对应上-->
        
        <i class="el-icon-kecheng"></i>
        <span slot="title">我的课表</span>
      
      </el-menu-item>
    </el-menu>
 
   </el-aside>

<el-container>
  <!-- 头部 -->
  <el-header>
    <i :class="collapseIcon" style="font-size: 26px;" @click="handleCollapse"></i>
    <!-- 面包屑 -->
    <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-left: 20px;">
  <el-breadcrumb-item :to="{ path: '/' }">系统首页</el-breadcrumb-item>
  <el-breadcrumb-item :to="{path:$route.path}" >{{$route.meta.name}}</el-breadcrumb-item>
</el-breadcrumb>
<!-- logo和个人信息 -->
 <div style="flex:1;width:0;display: flex; align-items: center; justify-content: flex-end;">
  <i class="el-icon-quanping" style="font-size: 26px; margin: 0 5px;" @click="handleFull"></i>
  <el-dropdown>
    <div style="display: flex;align-items: center; cursor: pointer;" >
      <!-- cursor:修改鼠标样式 -->
      <img :src="user.avatar || require('@/assets/defaultAvatar.png')" alt="" style="height: 40px;width:40px;border-radius: 50%;margin: 0 5px;">
      <span>{{user.realName}}</span>
    </div>
    
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item @click.native="$router.push('/personInfo')">个人信息</el-dropdown-item>
      <el-dropdown-item @click.native="$router.push('/password')">修改密码</el-dropdown-item>
      <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>

 </div>
  </el-header>
  <!-- 主体区域 -->
  <el-main>
    <router-view  @update:user="updateUser"/>
  </el-main>
</el-container>

 </el-container>
  </div>
</template>

<script>

import request from '@/utils/request';
export default {
  name: 'HomeView',
  components: {
    
  },
  data(){
    return{
      asideWidth:'200px',
      isCollapse:false,
      collapseIcon:'el-icon-s-fold',
      user:JSON.parse(localStorage.getItem('userData')||'{}'),
      url:''
    }
  },
  methods:{
    // preview(url){
    //     window.open(url)
    // },
    // handleMultipleFileUpload(){

    // },
    // handleFileTableUpload(row,file,fileList){
    //   console.log(row,file,fileList)
    //     row.avatar=file.response.data
    //     // 触发更新保存图片
    //     request.put('/user/update',row).then(res=>{
    //       if(res.code === '200'){
    //         this.$message.success('上传成功')
    //       }else{
    //         this.$message.error(res.msg)
    //       }
    //     })
    // },
    // handleFileUpload(response,file,fileList){
    //     this.fileList=fileList;

    // },
    logout(){
      localStorage.removeItem('userData')//清除当前的token和用户数据
      this.$router.push('/login')
    },
    handleCollapse(){

      this.isCollapse=!this.isCollapse,
      // 设置收缩，不收缩
      this.asideWidth=this.isCollapse?'64px':'200px'
      // 根据是否收缩来定asideWidth值，收缩的时候为64
      this.collapseIcon=this.isCollapse?'el-icon-s-unfold':'el-icon-s-fold'
    },
    handleFull(){
      document.documentElement.requestFullscreen()
    },
    updateUser(userInfo){//更新子组件user里的数据进来，personInfo修改头像的头像
      this.user=JSON.parse(JSON.stringify(userInfo))
// 对象的深度拷贝，如果是this.user=userInfo,那么userInfo改变的时候，user也会改变，因为他们指向的是同一个对象，所以需要深度拷贝
    }
  },
  mounted(){
    if(!this.user.id){
      this.$router.push('/login')
    }
    
  }
}
</script>
<style scoped>
.menu{
  display: flex;
  justify-content: center;
  align-items: center;
}
/* 每个一级菜单项以及子菜单项设置背景颜色 */
.el-menu--inline .el-menu-item{
  background-color: #000c17 !important;
  padding-left: 49px !important;
}
/*  每个一级菜单项以及子菜单项设置鼠标悬浮上去的颜色*/
.el-submenu__title:hover,.el-menu-item:hover{
  color:#fff !important;
}
/* 鼠标划到有二级菜单的一级菜单项的时候，让图标也变成白色 */
.el-submenu__title:hover i{
  color: #fff !important;
}
.el-menu-item.is-active{
  background-color: #1890ff !important;
  /* 激活菜单项的时候，背景变蓝色 */
  border-radius: 5px;
  width:calc(100% -4px);
  padding-left: 4px;
}
.el-menu-item.is-active i, .el-menu-item.is-active .el-tooltip{
  margin-left: -4px;
}
.el-menu-item{
  height: 40px !important;
  line-height: 40px !important;
  margin: 4px;
  /*为了与激活时的菜单项对齐 */
}
.el-submenu__title{
  height: 40px !important;
  line-height: 40px !important;
  margin: 4px;
   /*为了与激活时的菜单项对齐 */
}
.el-menu--inline{
  background-color: #000c17;
  /* 由于有margin，因此需要重设置颜色 */
}

.el-submenu .el-menu-item{
  min-width: 0 !important;
}
.el-menu--inline .el-menu-item.is-active{
  padding-left: 45px;
}
/* 设置侧边栏收缩动画 */
.el-aside{
  transition: width .3s;
  box-shadow: 2px 0 6px rgba(0,21,41,.35);
}
.el-header{
  height:60px;
  box-shadow: 2px 0 6px rgba(0,21,41,.35);
  display: flex;
  align-items: center;
}


</style>