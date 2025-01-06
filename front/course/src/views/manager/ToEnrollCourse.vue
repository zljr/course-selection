<template>
    <div>
        <div>
            <el-table :data="courses" stripe :header-cell-style="{backgroundColor:'aliceblue',color:'#666'}" @selection-change="handleSelectionChange">
                <el-table-column
                type="selection"
                width="55" align="center">
                </el-table-column>
                <el-table-column label="课程序号" width="90" prop="courseId"></el-table-column>
                <el-table-column label="课程名称" prop="courseName" sortable></el-table-column>
                <el-table-column label="教师序号" prop="teacherId" sortable></el-table-column>
                <el-table-column label="教师姓名" prop="teacher" sortable></el-table-column>
                <el-table-column label="周几" prop="dayOfWeek" sortable></el-table-column>
                <el-table-column label="上课时间" prop="startTime" sortable></el-table-column>
                <el-table-column label="下课时间" prop="endTime"></el-table-column>
                <el-table-column label="上课教室" prop="classroom" sortable></el-table-column>
                <el-table-column label="操作" align="center" width="180">
                    <template v-slot="scope">
                        <el-button type="primary" size="mini" plain @click="handleEnroll(scope.row)">选课</el-button>
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
        name:'ToEnrollCourse',
        data(){
            return{
                courses:[],
                user:JSON.parse(localStorage.getItem('userData')||'{}'),
                pageSize:5,
                pageNum:1,
                total:0,
            }
        },
        methods:{
        handleEnroll(row){
            let studentCourse={
                courseId:row.courseId,
                studentId:this.user.id
            };
            this.$request.post('/studentCourse/add',studentCourse).then(res=>{
                if(res.code === '200'){
                    this.$message.success('选课成功');
                    this.load()
                }else{
                    this.$message.error(res.msg)
                }
            })
        },
            load(pageNum){
            if(pageNum){
                this.pageNum=pageNum
            }
            this.$request.get('/course/getAllCourses',{
            params:{
                pageNum:this.pageNum,
                pageSize:this.pageSize,
            }
    }).then(res=>{
        console.log(res.data)
        this.courses=res.data.list,
        this.total=res.data.total
    }).catch(error=>{
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
    handleSelectionChange(rows){//当前选中的所有行数据
                console.log(rows)
                this.couseIds=rows.map(v=>v.courseId)//把一个对象数组（数组里面是对象）变成一个数字数组
                console.log(rows.map(v=>v.courseId))
            },
        },
        created(){
    this.load()
},
    }
</script>

<style  scoped>

</style>