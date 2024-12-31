<template>
    <div style="height: 100vh;display: flex; align-items: center; justify-content: center; background-color: cadetblue;">
      <div style="display: flex;background-color: white; width:80%;border-radius: 5px;overflow: hidden;">
        <div style="flex:1;">
                <img src="../assets//login.jpeg" alt="" style="max-width:100%;height: 100%;">
        </div>
        <div style="flex:1; display: flex;align-items: center;justify-content: center;">
                <el-form :model="user" :rules="rules" style="width:80%;" ref="loginRef">
                    <div style="font-size: 20px;font-weight: bold; text-align: center;margin-bottom: 20px;">欢迎登录选课系统</div>
                    <el-form-item prop="username">
                        <el-input prefix-icon="el-icon-user" size="medium" placeholder="请输入账号"  v-model="user.username" clearable></el-input>
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input prefix-icon="el-icon-lock" size="medium" placeholder="请输入密码"  v-model="user.password" show-password clearable></el-input>
                    </el-form-item>
                    <el-form-item prop="code">
                            <div style="display: flex;">
                                <el-input prefix-icon="el-icon-circle-check" style="flex:1;" size="medium" v-model="user.code" placeholder="请输入验证码"></el-input>
                                <div style="flex:1; background-color: rgba(95, 158, 160,.2);height: 36px;" ><ValidCode @update:value="getCode"/></div>
                            </div>
                            
                    </el-form-item>
                    <el-form-item>
                        <el-button @click="login" type="primary" style="width:100%;">登录</el-button>
                    </el-form-item>
                    <div style="display:flex ; justify-content: space-between; ">
                        <div>
                            还没有账号？请<span style="color: blue; cursor:pointer;" @click="$router.push('/register')">注册</span>
                        </div>
                    
                    <div style="color:green;cursor: pointer;" @click="handleForgetPass">忘记密码？</div>
                    </div>
                </el-form>
        </div>
      </div>
      <!-- 忘记密码对话框 -->
        <el-dialog title="忘记密码" :visible.sync="forgetPasswordDialogVis" width="30%">
    <el-form :model="forgetUserForm" label-width="80px" style="padding-right:20px;">
        <el-form-item label="用户名" >
        <el-input v-model="forgetUserForm.username" autocomplete="off" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="手机号" >
        <el-input v-model="forgetUserForm.telephone" autocomplete="off" placeholder="请输入手机号"></el-input>
        </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
        <el-button @click="forgetPasswordDialogVis = false">取 消</el-button>
        <el-button type="primary" @click="resetPassword">确 定</el-button>
    </div>
    </el-dialog>
    </div>
</template>
<script>
import ValidCode from '@/components/ValidCode.vue';

export default{
    name:'Login',
    components:{
        ValidCode
    },
data(){
    // 验证码校验
    const validateCode=(rule, value, callback)=>{
        if(value === ''){
            callback(new Error('请输入验证码'))
        }else if(value.toLocaleLowerCase()!==this.realCode.toLocaleLowerCase()){
            callback(new Error('验证码错误'))
        }
        callback()
    }
    
    return{
       realCode:'',
       forgetUserForm:{},
       forgetPasswordDialogVis:false,
        user:{
            username:'',
            code:'',
            password:''
        
        },
        // codeMsg:''
        rules:{
            username:[{
                required:true,message:'请输入账号',trigger:'blur'
            }],
            password:[{
                required:true,message:'请输入密码',trigger:'blur'
            }],
            code:[{
                validator:validateCode,trigger:'blur'
            }]
        }
        
    }
},
methods:{
    login(){
        this.$refs['loginRef'].validate((valid)=>{
                if(valid){
                    this.$request.post('/login',this.user).then(res=>{
                        // 当后台返回的code==200的时候，要存储用户信息，再跳转
                        if(res.code === '200'){
                            this.$router.push('/')
                            this.$message.success('登录成功')
                            localStorage.setItem("userData",JSON.stringify(res.data))//存储用户信息
                        }else{
                            // element提供的
                            this.$message.error(res.msg)
                        }
                     console.log(res)
        })
                }
        })
   
        
      
    },
    getCode(code){
        this.realCode=code
    },
    resetPassword(){
        this.$request.put('/password',this.forgetUserForm).then(res=>{
            if(res.code === '200'){
                this.$message.success('重置密码成功')
                this.forgetPasswordDialogVis=false
            }else{
                this.$message.error(res.msg)
            }
        })
    },
    handleForgetPass(){
        this.forgetUserForm={}
        this.forgetPasswordDialogVis=true
    }
}
}

</script>
<style scoped>

</style>