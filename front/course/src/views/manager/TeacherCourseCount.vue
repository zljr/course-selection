<template>
    <div>
        <el-card>
            <div id="bar" ref="bar" style="height:400px;width:50%;"></div>
        </el-card>
    </div>
</template>

<script>
const bar={
    title: {
    text: '选课人数统计',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis'
  },
    xAxis: {
    type: 'category',
    data: [],
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      data: [],
      type: 'bar'
    }
  ]
};
import * as echarts from 'echarts';
    export default {
        data(){
            return{
                user:JSON.parse(localStorage.getItem('userData')||'{}')
            }
        },
        methods:{
            getBarData(option,chart){
                this.$request.get('/course/getEnrollCountByTeacher',{
                    params:{
                        id:this.user.id
                    }
                }).then(res=>{
                    if(res.code === '200'){
                        let courseName=res.data.map(item=>item['课程名称']);
                        let count=res.data.map(item=>item['总人数']);
                        option.xAxis.data=courseName;
                        option.series[0].data=count;
                        chart.setOption(option);
                    }else{
                        this.$message.error(res.msg);
                    }
                }).catch(err=>{
                this.$message.error('请登录');
                this.$router.push('/login')
            })
            }
        },
        mounted(){
            let barDom=document.getElementById('bar');
            let barChart=echarts.init(barDom);
            this.getBarData(bar,barChart);
        }
    }
</script>

<style  scoped>

</style>