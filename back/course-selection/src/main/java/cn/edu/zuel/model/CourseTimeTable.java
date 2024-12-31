package cn.edu.zuel.model;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("`coursetimetable`")
public class CourseTimeTable {
    @TableId(type= IdType.AUTO)
    @Alias("课程时间表序号")
    private Integer courseTimeTableId;
    @Alias("课程序号")
    private Integer courseId;
    @Alias("时间段序号")
    private Integer timeSlotId;
    @Alias("上课教室")
    private String classroom;
/*    @Alias("课程名称")
    @TableField(exist = false)
    private String courseName;
    @Alias("授课教师")
    @TableField(exist = false)
    private String teacher;
    @Alias("星期几")
    @TableField(exist = false)
    private String dayOfWeek;
    @Alias("开始时间")
    @TableField(exist = false)
    private Time startTime;
    @Alias("结束时间")
    @TableField(exist = false)
    private Time endTime;*/
}
