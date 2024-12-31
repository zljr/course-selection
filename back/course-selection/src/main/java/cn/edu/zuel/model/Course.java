package cn.edu.zuel.model;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Time;

@Data
@TableName("`course`")
public class Course {
    @TableId(type= IdType.AUTO)
    @Alias("课程id")
    private Integer courseId;
    @Alias("课程名")
    private String courseName;
    @Alias("课程描述")
    private String courseDescription;
    @Alias("教师id")
    private Integer teacherId;
    @Alias("授课教师")
    @TableField(exist = false)
    private String teacher;
    @Alias("周几")
    @TableField(exist = false)
    private String dayOfWeek;
    @Alias("开始时间")
    @TableField(exist = false)
    private Time startTime;
    @Alias("结束时间")
    @TableField(exist = false)
    private Time endTime;
    @Alias("上课教室")
    @TableField(exist = false)
    private String classroom;
}
