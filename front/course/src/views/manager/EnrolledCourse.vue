<template>
    <div>
        <div>
            <el-table :data="enrolledCourse" stripe :header-cell-style="{backgroundColor:'aliceblue',color:'#666'}">
                <el-table-column label="序号" prop="studentCourseId" width="50"></el-table-column>
                <el-table-column label="课程序号" prop="courseId"></el-table-column>
                <el-table-column label="选课时间" prop="enrollmentTime"></el-table-column>
                <el-table-column label="课程名称" prop="courseName"></el-table-column>
                <el-table-column label="教师" prop="teacher"></el-table-column>
                <el-table-column label="周几" prop="dayOfWeek"></el-table-column>
                <el-table-column label="上课时间" prop="startTime"></el-table-column>
                <el-table-column label="下课时间" prop="endTime"></el-table-column>
                <el-table-column label="教室" prop="classroom"></el-table-column>
                <el-table-column label="操作" aligin="center" width="100">
                    <template v-slot="scope">
                        <el-button type="danger" size="mini" plain @click="dropCourse(scope.row)">退课</el-button>
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
        </div>
    </div>
</template>

<script>
    export default {
        name:'EnrolledCourse',
        data(){
            return{
                enrolledCourse:[],
                user:JSON.parse(localStorage.getItem('userData')||'{}'),
                pageSize:5,
                pageNum:1,
                total:0
            }
        },
        methods:{
            dropCourse(row){
                this.$request.delete('/studentCourse/delete/'+row.studentCourseId).then(res=>{
                    if(res.code === '200'){
                        this.$message.success('退课成功');
                        this.load()
                    }else{
                        this.$message.error(res.msg);
                    }
                })
            },
            load(pageNum){
                if(pageNum){
                    this.pageNum=pageNum
                }
                this.$request.get('/studentCourse/getEnrolledCoursesByStudent',{
                    params:{   
                    pageNum:this.pageNum,
                    pageSize:this.pageSize,
                    studentId:this.user.id
                    }
                }).then(res=>{
                    this.enrolledCourse=res.data.list,
                    this.total=res.data.total
                }).catch(error=>{
                    this.$message.error('请登录');
                    this.$router.push('/login')
                })
            },
            handleCurrentChange(pageNum){
                this.load(pageNum)
            },
            handleSizeChange(pageSize){
                this.pageSize=pageSize
                this.load()
            }
        },
        created(){
            this.load()
        }
    }
</script>

<style  scoped>

</style>