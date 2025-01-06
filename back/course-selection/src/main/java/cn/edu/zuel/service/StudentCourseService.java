package cn.edu.zuel.service;

import cn.edu.zuel.mapper.CourseMapper;
import cn.edu.zuel.mapper.StudentCourseMapper;
import cn.edu.zuel.model.StudentCourse;
import cn.edu.zuel.model.StudentCourseDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StudentCourseService extends ServiceImpl<StudentCourseMapper, StudentCourse> {
    @Resource
    private StudentCourseMapper studentCourseMapper;
/*    @Resource
    private CourseMapper courseMapper;*/
    public  StudentCourse hasSelectCourse(Integer studentId,Integer courseId){
        return studentCourseMapper.hasSelectCourse(studentId,courseId);
    }
    /*
    * 分页返回所有选课信息
    * */
    public PageInfo<StudentCourseDTO> getAllEnrolledCourses(Integer pageNum,Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<StudentCourseDTO> allEnrolledCourses=studentCourseMapper.getAllEnrolledCourses();
        return new PageInfo<>(allEnrolledCourses);
    }
/*
* 分页返回
* */
    public PageInfo<StudentCourseDTO> getEnrolledCoursesByStudent(Integer stuId,
                                                                  Integer pageNum,
                                                                  Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<StudentCourseDTO> enrolledCoursesByStudent = studentCourseMapper.getEnrolledCoursesByStudent(stuId);
        return new PageInfo<>(enrolledCoursesByStudent);
    }
    public List<StudentCourseDTO> getCourseByStudent(Integer stuId){
        return studentCourseMapper.getEnrolledCoursesByStudent(stuId);
    }
    public Boolean hasConflictCourse(Integer stuId,Integer courseId){
        return studentCourseMapper.hasConflictCourse(stuId,courseId);
    }
    public int selectCountOfCourse(Integer courseId){
        return studentCourseMapper.selectCountOfCourse(courseId);
    }
}
