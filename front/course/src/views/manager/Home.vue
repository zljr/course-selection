<template>
    <div>
        <div style="box-shadow: 0 0 10px rgba(0,0,0,.1);padding: 10px 20px;border-radius: 5px; margin-bottom: 10px;">
          <span v-if="(0<currentHours)&&(currentHours<8)">早上好，</span>
          <span v-else-if="(8<currentHours)&&(currentHours<12)">上午好，</span>
          <span v-else-if="(12<currentHours)&&(currentHours<18)">下午好，</span>
          <span v-else>晚上好，</span>
        {{user.realName}}<span v-if="user.role === '教师'">老师</span>欢迎来到选课系统！
        </div>
    <div style="display: flex;">
      <el-card style="width:100%;">
          <div slot="header" class="clearfix">
            <span>预祝选课顺利</span>
          </div>
          <div>
            阿巴巴巴
            <div style="margin-top:20px;">
              <div style="margin: 10px 0;"><strong>主题色</strong></div>
              <el-button type="primary">按钮</el-button>
              <el-button type="warning">按钮</el-button>
              <el-button type="success">按钮</el-button>
              <el-button type="danger">按钮</el-button>
              <el-button type="info">按钮</el-button>
            </div>
          </div>
      </el-card>
      
    </div>

    
</div>
</template>

<script>
    export default {
        data(){
            return{
                user: JSON.parse(localStorage.getItem('userData')||'{}'),
                currentHours:new Date().getHours()
            }
        }
    }
</script>

<style scoped>

</style>