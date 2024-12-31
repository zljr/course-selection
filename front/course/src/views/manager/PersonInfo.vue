<template>
    <div>
        <el-card style="width:50%;">
                <el-form :model="userInfo" >
                    <div style="margin: 15px;text-align: center;">
                        <el-upload
                    class="avatar-uploader"
                    :action="$baseUrl+'/file/upload'"
                    :headers="{token:userInfo.token}"
                    :show-file-list="false"
                    :on-success="handleAvatarSuccess">
                    <img v-if="userInfo.avatar" :src="userInfo.avatar" class="avatar">
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                    </div>
                
                    <el-form-item label="用户名" label-width="80px" style="padding-right:20px;" prop="username">
                        <el-input v-model="userInfo.username" disabled ></el-input>
                    </el-form-item>
                    <el-form-item label="姓名" prop="realName"  label-width="80px" style="padding-right:20px;">
                        <el-input v-model="userInfo.realName" placeholder="姓名"></el-input>
                    </el-form-item>
                    <el-form-item label="性别" prop="sex"  label-width="80px" style="padding-right:20px;">
                        <el-radio-group v-model="userInfo.sex">
                            <el-radio label="男"></el-radio>
                            <el-radio label="女"></el-radio>   
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="电话" prop="telephone"  label-width="80px" style="padding-right:20px;">
                        <el-input v-model="userInfo.telephone" placeholder="电话"></el-input>
                    </el-form-item>
                    <div style="text-align: center;margin-bottom: 20px;">
                        <el-button type="primary" @click="saveInfo">保存</el-button>
                    </div>
                </el-form>
        </el-card>
    </div>
</template>

<script>
    export default {
        name:'PersonInfo',
        data(){
            return{
                    userInfo:JSON.parse(localStorage.getItem('userData')||'{}')
            }
        },
        methods:{
            handleAvatarSuccess(res,file,fileList){
                console.log(res)
                this.userInfo.avatar=res.data
            },
            // 先修改data里的头像信息和保存头像到后端文件夹中，这时候页面上的头像直接就变成换了之后的，当保存的时候，
            // 保存成功了就先把图片路径保存到数据库，然后更新localStorage中的数据
            
            saveInfo(){
                this.$request.put('/user/update',this.userInfo).then(res=>{
                    if(res.code === '200'){
                        this.$message.success('修改成功')
                        localStorage.setItem('userData',JSON.stringify(this.userInfo))
                        // 触发父级事件更新
                        this.$emit('update:user',this.userInfo)
                    }else{
                        this.$message.error(res.msg)
                    }
                })
            }
        }
    }
</script>

<style  scoped>
:deep(.el-form-item__label){/**这里要进行样式穿透（因为elementui） */
    font-weight:bolder;
}
:deep(.avatar-uploader .el-upload) {
    border: 1px dashed #d9d9d9;
    border-radius: 50%;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
 :deep(.avatar-uploader .el-upload:hover) {
    border-color: #409EFF;
  }
  :deep(.el-upload){
    border-radius: 50%;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
    border-radius:50%;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
    border-radius:50%;/*给图片设置圆角，并且要给其父级也设置，不如周围会有空缺也能点到 */
  }
</style>