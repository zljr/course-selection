<template>
    <div>
    <div >
        <el-input style="width:200px;" placeholder="查询真实姓名" v-model="realName"></el-input>
        <el-input style="width:200px;margin: 0 5px;" placeholder="查询用户名" v-model="username"></el-input>
        <el-button type="primary" @click="load(1)">查询</el-button>
        <el-button type="danger" @click="reset">重置</el-button>
    </div>
    <div style="margin:10px 0;">
        <el-button type="primary" plain @click="handleAdd">新增</el-button>
        <el-button type="danger" plain @click="delBatch">批量删除</el-button>
        <el-button type="info" plain @click="exportBatch">批量导出</el-button>
        <el-upload
        :action="$baseUrl+'/user/import'"
        :headers="{token:user.token}"
        :show-file-list="false" 
        :on-success="handleImport"
        style="display:inline-block;margin-left: 10px;">
        <el-button type="primary" plain>批量导入</el-button>
        </el-upload>
    </div>
        <el-table :data="users" stripe :header-cell-style="{backgroundColor:'aliceblue',color:'#666'}" @selection-change="handleSelectionChange">
                <el-table-column
                type="selection"
                width="55" align="center">
                </el-table-column>
                <el-table-column label="序号" width="70" prop="id"></el-table-column>
                <el-table-column label="姓名" prop="realName"></el-table-column>
                <el-table-column label="用户名" prop="username"></el-table-column>
                <el-table-column label="性别" prop="sex"></el-table-column>
                <el-table-column label="电话" prop="telephone"></el-table-column>
                <el-table-column label="文件上传" >
                    <template v-slot="scope">
                      <el-upload
                  :action="$baseUrl+'/file/upload'"
                  :show-file-list="false"
                  :headers="{token:user.token}"
                  :on-success="(row,file,fileList)=>handleFileTableUpload(scope.row,file,fileList)">
                  <!-- 这里用row这个参数把原本的response参数覆盖掉 -->
                  <el-button size="mini" type="primary">点击上传</el-button>
                  </el-upload>
                    </template>
                  </el-table-column>
                <el-table-column label="头像" >
                    <template v-slot="scope">
                        <div style="display: flex;align-items: center;">
                            <el-image :src="scope.row.avatar" v-if="scope.row.avatar" style="width:50px;height: 50px;" :preview-src-list="[scope.row.avatar]"></el-image>
                        </div>
                       
                    </template>
                </el-table-column>
                <el-table-column label="角色" prop="role"></el-table-column>
                <el-table-column label="创建时间" prop="createTime"></el-table-column>
                <el-table-column label="修改时间" prop="updateTime"></el-table-column>
                <el-table-column label="操作" align="center" width="180">
                    <template v-slot="scope">
                        <el-button type="primary" size="mini" plain @click="handleEdit(scope.row)">编辑</el-button>
                        <el-button type="danger" size="mini" plain @click="del(scope.row.id)">删除</el-button>
                    </template>
                </el-table-column>
        </el-table>
        <div style="margin: 10px 0;">
            <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pageNum"
      :page-sizes="[5, 10, 15, 20]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      align="center">
    </el-pagination>
        </div>
        <el-dialog title="编辑用户" :visible.sync="userFormVisible" style="width:60%;">
            <el-form :model="form" :rules="rules" ref="formRef">

                
                    <el-form-item label="用户名" label-width="80px" style="padding-right:20px;" prop="username">
                        <el-input v-model="form.username" placeholder="用户名" ></el-input>
                    </el-form-item>
                    <el-form-item label="真实姓名" prop="realName"  label-width="80px" style="padding-right:20px;">
                        <el-input v-model="form.realName" placeholder="真实姓名"></el-input>
                    </el-form-item>
                    <el-form-item label="性别" prop="sex"  label-width="80px" style="padding-right:20px;">
                        <el-radio-group v-model="form.sex">
                            <el-radio label="男"></el-radio>
                            <el-radio label="女"></el-radio>   
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="电话" prop="telephone"  label-width="80px" style="padding-right:20px;">
                        <el-input v-model="form.telephone" placeholder="电话"></el-input>
                    </el-form-item>
                    <el-form-item label="角色" prop="role"  label-width="80px" style="padding-right:20px;"> 
                        <el-radio-group v-model="form.role">
                            <el-radio label="教师"></el-radio>
                            <el-radio label="学生"></el-radio>   
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="头像" label-width="80px" style="padding-right:20px;">
                            
                            <el-upload
                    class="avatar-uploader"
                    :action="$baseUrl+'/file/upload'"
                    :headers="{token:user.token}"
                    :file-list="form.avatar?[form.avatar]:[]"
                    list-type="picture"
                    :on-success="handleAvatarSuccess">
                    <el-button type="primary">上传头像</el-button>
                    </el-upload>

                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                <el-button @click="userFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveInfo">确 定</el-button>
                </div>
        </el-dialog>
        
    </div>
</template>

<script>
import request from '@/utils/request';
    export default {
mounted(){

  
},
created(){
    this.load()
},
data(){
    return{
        users:[],
        user:JSON.parse(localStorage.getItem('userData')||'{}'),
        url:'',
        pageSize:5,
        pageNum:1,
        username:'',
        realName:'',
        total:0,
        userFormVisible:false,
        form:{},
        rules:{
            username:[
                {
                    required:true,message:"请输入用户名",trigger:'blur'
                }
            ],
            realName:[
                {
                    required:true,message:"请输入真实姓名",trigger:'blur'
                }
            ],
        },
        ids:[]
 
    }
},
methods:{
    handleFileTableUpload(row,file,fileList){
      console.log(row,file,fileList)
        row.avatar=file.response.data
        // 触发更新保存图片
        request.put('/user/update',row).then(res=>{
          if(res.code === '200'){
            this.$message.success('上传成功')
          }else{
            this.$message.error(res.msg)
          }
        })
    },
    load(pageNum){
        if(pageNum){
            this.pageNum=pageNum
        }
        request.get('/user/getUserByPage',{
        params:{
            pageNum:this.pageNum,
            pageSize:this.pageSize,
            username:this.username,
            realName:this.realName
        }
    }).then(res=>{
        this.users=res.data.records,
        this.total=res.data.total
    })
    },
    handleCurrentChange(pageNum){
        // this.pageNum=pageNum
        // this.load()
        this.load(pageNum)
    },
    handleSizeChange(pageSize){
        this.pageSize=pageSize
        this.load()
    },
    reset(){
        this.username='',
        this.realName='',
        this.load()
    },
    saveInfo(){//更新和添加共用一个方法
        this.$refs.formRef.validate(valid=>{
            if(valid){
                this.$request({url:this.form.id?'/user/update':'/user/add',method:this.form.id?'put':'post',data:this.form}).then(res=>{
                    if(res.code === '200'){
                        this.$message.success('保存成功')
                        this.userFormVisible=false
                        this.load(1)
                    }
                })
                
            }else{
                this.$message.error(res.msg)
            }
        })
              
            },
            handleAvatarSuccess(res,file,fileList){
                console.log(res)
                this.form.avatar=res.data
            },
            handleAdd(){
                this.form={role:'学生'}//只有新增这里需要默认一个角色
                this.userFormVisible=true
            },
            handleEdit(row){
                this.form=JSON.parse(JSON.stringify(row))//给form对象赋值，深拷贝
                this.userFormVisible=true
            },
            del(id){
                this.$confirm('确认删除吗?','提示',{
                    confirmButtonText:'确定',
                    cancelButtonText:'取消',
                    type:'warning'

                }).then(response=>{
                    this.$request.delete('/user/delete/'+id).then(res=>{
                            if(res.code === '200'){
                                this.$message.success('操作成功')
                                this.load(1)
                            }else{
                                this.$message.error(res.msg)
                            }
                    })
                }).catch(()=>{
                        
                    })
            },
            handleSelectionChange(rows){//当前选中的所有行数据
                console.log(rows)
                this.ids=rows.map(v=>v.id)//把一个对象数组（数组里面是对象）变成一个数字数组
            },
            delBatch(){
                if(this.ids.length === 0){
                    this.$message.warning('请选择要删除的用户')
                    return
                }
                this.$confirm('确认删除吗？','提示',{
                    confirmButtonText:'确定',
                    cancelButtonText:'取消',
                    type:'warning'
                }).then(response=>{
                    this.$request.delete('/user/delete/batch',{
                        data:this.ids
                    }).then(res=>{
                        if(res.code === '200'){
                            this.$message.success('操作成功')
                            this.load(1)
                        }else{
                            this.$message.error(res.msg)
                        }
                    })
                }).catch(()=>{})
            },
            exportBatch(){//批量导出数据
                    if(!this.ids.length){//没有选择的时候，要么全部导出，要么把搜索匹配的数据导出
                        // 这里由于没有用axios，请求头里面没有token，就可以通过url拼接token 的方式来实现
                        window.open(this.$baseUrl+'/user/export?token='+this.user.token+'&username='+this.username+'&realName='+this.realName)
                    }else{
                        window.open(this.$baseUrl+'/user/export?token='+this.user.token+'&ids='+this.ids.join(','))
                    }
            },
            handleImport(res,file,fileList){
                if(res.code === '200'){
                    this.$message.success('导入成功')
                }else{
                    this.$message.error(res.msg)
                }
                this.load(1)
            }

}

    }
</script>

<style scoped>

</style>