<template>
  <div>
    <div style="font-size: 30px;font-weight: bold;text-align: center;margin-bottom: 20px;">我的课表</div>
    <el-table :data="tableData" border>
      <el-table-column prop="timeSlot" label="时间段" width="180">
        <template slot-scope="scope">
          {{ scope.row.timeSlot }}
        </template>
      </el-table-column>
      <el-table-column
        v-for="(day, index) in weekDays"
        :key="index"
        :label="day"
        :prop="'courses.' + day">
        <template slot-scope="scope">
          <div v-if="scope.row.courses[day]">
            <el-tag
              v-for="(course, courseIndex) in scope.row.courses[day]"
              :key="courseIndex"
              
              v-html="formatCourseInfo(course)">
            </el-tag>
          </div>
          <div v-else>-</div>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import request from '@/utils/request';
export default {
  data() {
    return {
      weekDays: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'], // 假设一周七天都显示
      timeSlots: [
        '第一大节 08:30-11:50',
        '第二大节 14:00-17:20',
        '第三大节 18:30-21:30'
      ],
      tableData: [],
      backendData: {},
      user: JSON.parse(localStorage.getItem('userData') || {})
    }
  },
  created() {
    this.getData()
  },
  methods: {
    organizeData() {
      this.tableData = this.timeSlots.map(timeSlot => {
        const dayCourses = this.weekDays.reduce((acc, day) => {
          acc[day] = this.backendData[day]
            ? this.backendData[day].filter(course => {
                const startTime = course.startTime.substring(0, 5); // 取到小时和分钟
                const slotStartTime = timeSlot
                  .match(/(\d+:\d+)-(\d+:\d+)/)[1] // 匹配时间段中的开始时间
                  .replace(':', ''); // 移除冒号以便比较
                return startTime.replace(':', '') === slotStartTime;
              })
            : [];
          return acc;
        }, {});
        return { timeSlot, courses: dayCourses };
      });
    },
    getData() {
      this.$request.get('/course/getCourseByUser', {
        params: {
          id: this.user.id
        }
      }).then(res => {
        if (res.code === '200') {
          const data = res.data;
          const groupedBydayOfWeek = data.reduce((groups, item) => {
            const dayOfWeekKey = item.dayOfWeek;
            if (!groups[dayOfWeekKey]) {
              groups[dayOfWeekKey] = [];
            }
            groups[dayOfWeekKey].push(item);
            return groups;
          }, {});
          this.backendData = groupedBydayOfWeek;
          this.organizeData();
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    formatCourseInfo(course) {
      return `${course.courseName}<br>${course.classroom}`;
    }
  }
}
</script>

<style scoped>

</style>