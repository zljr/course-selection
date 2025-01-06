package cn.edu.zuel.model;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

import java.sql.Time;

@Data
public class StudentCourseDTO {

    //学生课程id,课程id,选课时间,学生id,学生姓名,教师id,课程名称,教师,周几,开始时间,结束时间,教室
    @Alias("学生课程id")
    private Integer studentCourseId;
    @Alias("课程id")
    private Integer courseId;
    @Alias("选课时间")
    private String enrollmentTime;
    @Alias("学生id")
    private Integer studentId;
    @Alias("学生姓名")
    private String studentName;
    @Alias("教师id")
    private Integer teacherId;
    @Alias("课程名称")
    private String courseName;
    @Alias("教师")
    private String teacher;
    @Alias("周几")
    private String dayOfWeek;
    @Alias("开始时间")
    private Time startTime;
    @Alias("结束时间")
    private Time endTime;
    @Alias("教室")
    private String classroom;

}
