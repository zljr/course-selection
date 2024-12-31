<template>
    <div>
        <el-card style="width:50%;">
            <el-form :rules="rules" ref="formRef" :model="user">
                <el-form-item label="原密码" label-width="80px" style="padding-right: 20px;" prop="password">
                    <el-input v-model="user.password" placeholder="原密码" show-password></el-input>
                </el-form-item>
                <el-form-item label="新密码" label-width="80px" style="padding-right: 20px;" prop="newPassword">
                    <el-input v-model="user.newPassword" placeholder="新密码" show-password></el-input>
                </el-form-item>
                <el-form-item label="确认密码" label-width="80px" style="padding-right: 20px;" prop="confirmPassword">
                    <el-input v-model="user.confirmPassword" placeholder="确认新密码" show-password></el-input>
                </el-form-item>
                <div style="text-align: center;margin-bottom: 20px;">
                        <el-button type="primary" @click="update">确认修改</el-button>
                </div>
            </el-form>
        </el-card>
    </div>

</template>

<script>
    export default {
        data(){
            const validatePassword=(rule,value,callback)=>{
                if(value === ''){
                    callback(new Error('请输入新密码'))
                }else if(value !== this.user.newPassword){
                    callback(new Error('两次密码不一致'))
                }else{
                    callback()
                }
            }
            return{
                user:JSON.parse(localStorage.getItem('userData')||'{}'),
                rules:{
            password:[{
                required:true,message:'请输入原始密码',trigger:'blur'
            }],
            newPassword:[{
                required:true,message:'请输入新密码',trigger:'blur'
            }],
            confirmPassword:[{
                validator:validatePassword,required:true,trigger:'blur'
            }]
        }
            }
        },
        methods:{
            update(){
               
                this.$refs.formRef.validate(valid=>{
                    if(valid){
                        this.user.password=this.user.newPassword
                        console.log(this.user.password)
                        this.$request.put('/user/update',this.user).then(res=>{
                    if(res.code === '200'){
                        this.$message.success('修改成功')
                        this.$router.push('/login')
                    }else{
                        this.$message.error(res.msg)
                    }
                })
                    }
                })
            
            },

        }
    }
</script>

<style scoped>
:deep(.el-form-item__label){/**这里Vue2采用:deep()来进行样式穿透，这是因为element-ui为了对样式进行保护做的，所以自己设置的话需要样式穿透 */
    font-weight:bolder !important;
}
</style>