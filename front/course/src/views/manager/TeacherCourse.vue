<template>
    <div>
        <div>
            <el-input style="width:200px;" placeholder="查询课程描述" v-model="courseDescription"></el-input>
            <el-input style="width:200px;margin: 0 5px;" placeholder="查询课程名称" v-model="courseName"></el-input>
            <el-button type="primary" @click="load(1)">查询</el-button>
            <el-button type="danger" @click="reset">重置</el-button>
        </div>
        <div style="margin:10px 0;">
            <el-button type="primary" plain @click="handleAdd">添加课程</el-button>
            <el-button type="danger" plain @click="delBatch">批量删除</el-button>
            <el-button type="info" plain @click="exportBatch">批量导出</el-button>
        </div>
        <el-table :data="courses" stripe :header-cell-style="{backgroundColor:'aliceblue',color:'#666'}" @selection-change="handleSelectionChange">
            <el-table-column
            type="selection"
            width="55" align="center">
            </el-table-column>
            <el-table-column label="课程序号" width="90" prop="courseId"></el-table-column>
            <el-table-column label="课程名称"  prop="courseName"></el-table-column>
            <el-table-column label="课程描述"  prop="courseDescription"></el-table-column>
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
            </el-form>
            <div slot="footer" class="dialog-footer">
            <el-button @click="courseFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="saveInfo">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        data(){
            return{
                courseDescription:'',
                courseName:'',
                courses:[],
                user:JSON.parse(localStorage.getItem('userData')||'{}'),
                courseIds:[],
                pageSize:5,
                pageNum:1,
                total:0,
                form:{},
                courseFormVisible:false,
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
        },
            }
        },
        created(){
            this.load()
        },
        methods:{
            saveInfo(){
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
            del(id){
                this.$confirm('确认删除吗？','提示',{
                    confirmButtonText:'确定',
                    cancelButtonText:'取消',
                    type:'warning'
                }).then(response=>{
                    this.$request.delete('/course/delete/'+id).then(res=>{
                        if(res.code === '200'){
                            this.$message.success('删除成功')
                            this.load(1)
                        }else{
                            this.$message.error(res.msg)
                        }
                    })
                }).catch(()=>{})
            },
            handleEdit(row){
                this.form=JSON.parse(JSON.stringify(row))
                this.courseFormVisible=true
            },
            exportBatch(){
                    if(!this.courseIds.length){
                        window.open(this.$baseUrl+'/course/export?token='+this.user.token+'&courseName='+this.courseName+'&courseDescription='+this.courseDescription)
                    }else{
                        window.open(this.$baseUrl+'/course/export?token='+this.user.token+'&courseIds='+this.courseIds.join(','))
                    }
            },
            delBatch(){
                if(this.courseIds.length === 0){
                    this.$message.warning('请选择的要删除的课程')
                    return
                }
                this.$confirm('确认删除吗？','提示',{
                    confirmButtonText:'确定',
                    cancelButtonText:'取消',
                    type:'warning'
                }).then(response=>{
                    this.$request.delete('/course/delete/batch',{
                        data:this.courseIds
                    }).then(res=>{
                        if(res.code === '200'){
                            this.$message.success('删除成功')
                            this.load(1)
                        }else{
                            this.$message.error(res.msg)
                        }
                    })
                }).catch(()=>{})
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
            handleSelectionChange(rows){
               
                this.courseIds=rows.map(v=>v.courseId)
                console.log(this.courseIds)
            },
            handleAdd(){
                this.form={}
                this.courseFormVisible=true
            },
            reset(){
                this.courseDescription=''
                this.courseName=''
                this.load()
            },
            load(pageNum){
                if(pageNum){
                    this.pageNum=pageNum
                }
                this.$request.get('/course/getCourseByUserId',{
                    params:{
                        pageNum:this.pageNum,
                        pageSize:this.pageSize,
                        courseName:this.courseName,
                        courseDescription:this.courseDescription,
                        teacherId:this.user.id
                    }
                }).then(res=>{
                    if(res.code === '200'){
                        this.courses=res.data.records,
                        this.total=res.data.total
                    }else{
                        this.$message.error(res.msg)
                    }
                    
                })
            },
        }
    }
</script>

<style  scoped>

</style>