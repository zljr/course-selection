<template>
    <div>
    <div >
        <el-input style="width:200px;" placeholder="查询课程描述" v-model="courseDescription"></el-input>
        <el-input style="width:200px;margin: 0 5px;" placeholder="查询课程名称" v-model="courseName"></el-input>
        <el-button type="primary" @click="load(1)">查询</el-button>
        <el-button type="danger" @click="reset">重置</el-button>
    </div>
    <div style="margin:10px 0;">
        <el-button type="primary" plain @click="handleAdd">新增</el-button>
        <el-button type="danger" plain @click="delBatch">批量删除</el-button>
        <el-button type="info" plain @click="exportBatch">批量导出</el-button>
        <el-upload
        :action="$baseUrl+'/course/import'"
        :headers="{token:user.token}"
        :show-file-list="false" 
        :on-success="handleImport"
        style="display:inline-block;margin-left: 10px;">
        <el-button type="primary" plain>批量导入</el-button>
        </el-upload>
    </div>
        <el-table :data="courses" stripe :header-cell-style="{backgroundColor:'aliceblue',color:'#666'}" @selection-change="handleSelectionChange">
                <el-table-column
                type="selection"
                width="55" align="center">
                </el-table-column>
                <el-table-column label="课程序号" width="90" prop="courseId"></el-table-column>
                <el-table-column label="课程名称" prop="courseName"></el-table-column>
                <el-table-column label="课程描述" prop="courseDescription"></el-table-column>
                <el-table-column label="教师序号" prop="teacherId"></el-table-column>
                <el-table-column label="教师姓名" prop="teacher"></el-table-column>
                <el-table-column label="操作" align="center" width="180">
                    <template v-slot="scope">
                        <el-button type="primary" size="mini" plain @click="handleEdit(scope.row)">编辑</el-button>
                        <el-button type="danger" size="mini" plain @click="del(scope.row.courseId)">删除</el-button>
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
        <el-dialog title="编辑课程信息" :visible.sync="courseFormVisible" style="width:60%;">
            <el-form :model="form" :rules="rules" ref="formRef">

                
                    <el-form-item label="课程名称" label-width="80px" style="padding-right:20px;" prop="courseName">
                        <el-input v-model="form.courseName" placeholder="课程名称" ></el-input>
                    </el-form-item>
                    <el-form-item label="课程描述" prop="courseDescription"  label-width="80px" style="padding-right:20px;">
                        <el-input v-model="form.courseDescription" placeholder="课程描述" type="textarea"></el-input>
                    </el-form-item>
                    <el-form-item label="教师序号" label-width="80px" style="padding-right:20px;" prop="teacherId">
                        <el-input v-model="form.teacherId" placeholder="教师序号" ></el-input>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                <el-button @click="courseFormVisible = false">取 消</el-button>
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
        courses:[],
        user:JSON.parse(localStorage.getItem('userData')||'{}'),
        url:'',
        pageSize:5,
        pageNum:1,
        courseName:'',
        courseDescription:'',
        total:0,
        courseFormVisible:false,
        form:{},
        rules:{
            courseName:[
                {
                    required:true,message:"请输入课程名称",trigger:'blur'
                }
            ],
            courseDescription:[
                {
                    required:true,message:"请输入课程描述",trigger:'blur'
                }
            ],
            teacherId:[
                {
                    required:true,message:"请输入教师序号",trigger:'blur'
                }
            ],
        },
        couseIds:[]
 
    }
},
methods:{
    load(pageNum){
        if(pageNum){
            this.pageNum=pageNum
        }
        request.get('/course/getCourseByPage',{
        params:{
            pageNum:this.pageNum,
            pageSize:this.pageSize,
            courseName:this.courseName,
            courseDescription:this.courseDescription
        }
    }).then(res=>{
        console.log(res.data)
        this.courses=res.data.records,
        this.total=res.data.total
    }).catch(err=>{
        this.$message.error('请登录');
        this.$router.push('/login')
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
        this.courseName='',
        this.courseDescription='',
        this.load()
    },
    saveInfo(){//更新和添加共用一个方法
        this.$refs.formRef.validate(valid=>{
            if(valid){
                this.$request({url:this.form.courseId?'/course/update':'/course/add',method:this.form.courseId?'put':'post',data:this.form}).then(res=>{
                    if(res.code === '200'){
                        this.$message.success('保存成功')
                        this.courseFormVisible=false
                        this.load(1)
                    }else{
                        this.$message.error(res.msg)
                    }
                })
                
            }else{
                this.$message.error(res.msg)
            }
        })
              
            },
            handleAdd(){
                this.form={}
                this.courseFormVisible=true
            },
            handleEdit(row){
                this.form=JSON.parse(JSON.stringify(row))//给form对象赋值，深拷贝
                this.courseFormVisible=true
            },
            del(courseId){
                this.$confirm('确认删除吗?','提示',{
                    confirmButtonText:'确定',
                    cancelButtonText:'取消',
                    type:'warning'

                }).then(response=>{
                    this.$request.delete('/course/delete/'+courseId).then(res=>{
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
                this.couseIds=rows.map(v=>v.courseId)//把一个对象数组（数组里面是对象）变成一个数字数组
                console.log(rows.map(v=>v.courseId))
            },
            delBatch(){
                if(this.couseIds.length === 0){
                    this.$message.warning('请选择要删除的课程')
                    return
                }
                this.$confirm('确认删除吗？','提示',{
                    confirmButtonText:'确定',
                    cancelButtonText:'取消',
                    type:'warning'
                }).then(response=>{
                    this.$request.delete('/course/delete/batch',{
                        data:this.couseIds
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
                    if(!this.couseIds.length){//没有选择的时候，要么全部导出，要么把搜索匹配的数据导出
                        // 这里由于没有用axios，请求头里面没有token，就可以通过url拼接token 的方式来实现
                        window.open(this.$baseUrl+'/course/export?token='+this.user.token+'&courseName='+this.courseName+'&courseDescription='+this.courseDescription)
                    }else{
                        window.open(this.$baseUrl+'/course/export?token='+this.user.token+'&couseIds='+this.couseIds.join(','))
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