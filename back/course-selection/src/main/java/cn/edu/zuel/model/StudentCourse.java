package cn.edu.zuel.model;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("`studentcourse`")
public class StudentCourse {
    @TableId(type = IdType.AUTO)
    @Alias("学生课程表序号")
    private Integer studentCourseId;
    @Alias("学生序号")
    private Integer studentId;
    @Alias("课程序号")
    private Integer courseId;
    @Alias("选课时间")
    private String enrollmentTime;
}
