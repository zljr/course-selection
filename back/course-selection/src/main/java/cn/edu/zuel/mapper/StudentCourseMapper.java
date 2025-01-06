package cn.edu.zuel.mapper;

import cn.edu.zuel.model.StudentCourse;
import cn.edu.zuel.model.StudentCourseDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface StudentCourseMapper extends BaseMapper<StudentCourse> {
//    判断学生是否已经选择这门课
    @Select("select *from studentcourse where student_id=#{studentId} and course_id=#{courseId}")
    StudentCourse hasSelectCourse(Integer studentId,Integer courseId);
    @Select("select sc.student_course_id,sc.course_id,sc.enrollment_time,sc.student_id,c.teacher_id,c.course_name,u.real_name as teacher,ts.day_of_week," +
            "ts.start_time,ts.end_time,ct.classroom from studentcourse sc " +
            "join course c on sc.course_id=c.course_id " +
            "join user u on c.teacher_id=u.id " +
            "join coursetimetable ct on c.course_id=ct.course_id " +
            "join timeslots ts on ct.time_slot_id=ts.time_slot_id ")
    List<StudentCourseDTO> getAllEnrolledCourses();
    @Select("select sc.student_course_id,sc.course_id,sc.enrollment_time,sc.student_id,c.teacher_id,c.course_name,u.real_name as teacher,ts.day_of_week," +
            "ts.start_time,ts.end_time,ct.classroom from studentcourse sc " +
            "join course c on sc.course_id=c.course_id " +
            "join user u on c.teacher_id=u.id " +
            "join coursetimetable ct on c.course_id=ct.course_id " +
            "join timeslots ts on ct.time_slot_id=ts.time_slot_id where sc.student_id=#{stuId}")
    List<StudentCourseDTO> getEnrolledCoursesByStudent(Integer stuId);

    @Select("select exists(" +
            "select 1 from(" +
            "select ct.time_slot_id from studentcourse sc join coursetimetable ct on sc.course_id=ct.course_id " +
            "where sc.student_id=#{studentId}) as existing_slots " +
            "join(select ct.time_slot_id from coursetimetable ct where ct.course_id=#{courseId}) as new_course_slots " +
            "on existing_slots.time_slot_id=new_course_slots.time_slot_id)")
    Boolean hasConflictCourse(Integer studentId,Integer courseId);
    /*
    * 统计某门课的选课人数
    * */
    @Select("select count(*) as total_records from studentcourse where course_id=#{courseId}")
    int selectCountOfCourse(Integer courseId);
}
