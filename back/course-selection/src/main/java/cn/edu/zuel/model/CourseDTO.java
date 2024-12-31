package cn.edu.zuel.model;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

@Data
public class CourseDTO {
    @Alias("课程id")
    private Integer courseId;
    @Alias("课程名称")
    private String courseName;
    @Alias("教师id")
    private Integer teacherId;
    @Alias("教师")
    private String teacher;
    @Alias("周几")
    private String dayOfWeek;
    @Alias("开始时间")
    private String startTime;
    @Alias("结束时间")
    private String endTime;
    @Alias("上课教室")
    private String classroom;
}
