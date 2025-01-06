package cn.edu.zuel.mapper;

import cn.edu.zuel.model.Course;
import cn.edu.zuel.model.CourseDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CourseMapper extends BaseMapper<Course> {
    @Select("select *from course where teacher_id=#{userId}")
    List<Course> getCourseByUserId(Integer userId);

    @Select("select c.course_id,c.course_name,u.id as teacher_id,u.real_name as teacher," +
            "ts.day_of_week,ts.start_time,ts.end_time,ct.classroom " +
            "from course c join user u on c.teacher_id=u.id " +
            "join coursetimetable ct on c.course_id=ct.course_id " +
            "join timeslots ts on ct.time_slot_id=ts.time_slot_id")
    List<CourseDTO> getAllCourses();

    @Select("select c.course_id,c.course_name,u.id as teacher_id,u.real_name as teacher," +
            "ts.day_of_week,ts.start_time,ts.end_time,ct.classroom " +
            "from course c join user u on c.teacher_id=u.id " +
            "join coursetimetable ct on c.course_id=ct.course_id " +
            "join timeslots ts on ct.time_slot_id=ts.time_slot_id where u.id=#{id}")
    List<CourseDTO> getCourseByUser(Integer id);


}
