<template>
    <div style="height: 100vh;display: flex; align-items: center; justify-content: center; background-color:deepskyblue;">
      <div style="display: flex;background-color: white; width:80%;border-radius: 5px;overflow: hidden;">
        <div style="flex:1;">
                <img src="../assets/register.jpeg" alt="" style="max-width:100%;height: 100%;">
        </div>
        <div style="flex:1; display: flex;align-items: center;justify-content: center;">
                <el-form :model="user" :rules="rules" style="width:80%;" ref="registerRef">
                    <div style="font-size: 20px;font-weight: bold; text-align: center;margin-bottom: 20px;">欢迎注册选课系统</div>
                    <el-form-item prop="username">
                        <el-input prefix-icon="el-icon-user" size="medium" placeholder="请输入账号"  v-model="user.username" clearable></el-input>
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input prefix-icon="el-icon-lock" size="medium" placeholder="请输入密码"  v-model="user.password" show-password clearable></el-input>
                    </el-form-item>
                    <el-form-item prop="confirmPassword">
                        <el-input prefix-icon="el-icon-lock" size="medium" placeholder="请确认密码"  v-model="user.confirmPassword" show-password clearable></el-input>
                            
                    </el-form-item>
                    <el-form-item prop="role">
                    <el-radio-group v-model="user.role">
                        <el-radio  label="学生">学生</el-radio>
                        <el-radio  label="教师">教师</el-radio>
                    </el-radio-group>
                            
                        
                    </el-form-item>
                    <el-form-item>
                        <el-button @click="register" type="info" style="width:100%;">注册</el-button>
                    </el-form-item>
                    <div style="display:flex ; justify-content: space-between; ">
                        <div>
                            已经有一个账号？请<span style="color: blue; cursor:pointer;" @click="$router.push('/login')">登录</span>
                        </div>

                    </div>
                </el-form>
        </div>
      </div>
    </div>
</template>
<script>


export default{
    name:'Register',

data(){
    // 密码校验
    const validatePassword=(rule, validatePassword, callback)=>{
        if(validatePassword === ''){
            callback(new Error('请确认密码'))
        }else if(validatePassword !==this.user.password){
            callback(new Error('两次密码不一致'))
        }
        callback()
    }
    return{
        user:{
            username:'',
            password:'',
            confirmPassword:'',
            role:''
        },
        // codeMsg:''
        rules:{
            username:[{
                required:true,message:'请输入账号',trigger:'blur'
            }],
            password:[{
                required:true,message:'请输入密码',trigger:'blur'
            }],
            confirmPassword:[{
                validator:validatePassword,trigger:'blur'
            }],
            role:[{
                required:true,message:'请选择角色',trigger:'blur'
            }],
        }
        
    }
},
methods:{
    register(){
        this.$refs['registerRef'].validate((valid)=>{
                if(valid){
                    this.$request.post('/register',this.user).then(res=>{
                        // 当后台返回的code==200的时候
                        if(res.code === '200'){
                            this.$router.push('/login')
                            this.$message.success('注册成功')

                        }else{
                            // element提供的
                            this.$message.error(res.msg)
                        }
                     console.log(res)
        })
                }
        })
   
        
      
    }
}
}

</script>
<style scoped>

</style>