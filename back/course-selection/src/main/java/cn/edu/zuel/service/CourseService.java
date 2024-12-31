package cn.edu.zuel.service;

import cn.edu.zuel.mapper.CourseMapper;
import cn.edu.zuel.model.Course;
import cn.edu.zuel.model.CourseDTO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CourseService extends ServiceImpl<CourseMapper, Course> {
    @Resource
    private CourseMapper courseMapper;
    public List<Course> getCourseByUserId(Integer userId){
        return courseMapper.getCourseByUserId(userId);
    }

    public PageInfo<CourseDTO> getAllCourses(Integer pageNum, Integer pageSize){
        /*
        * 开启分页功能
        * */
        PageHelper.startPage(pageNum,pageSize);
        List<CourseDTO> courses=courseMapper.getAllCourses();
        /*
        * 使用PageInfo包装查询结果
        * */
        return new PageInfo<>(courses);
    }
    public List<CourseDTO> getCourseByUser(Integer id){
        return courseMapper.getCourseByUser(id);
    }
    public List<CourseDTO> getAllCourses(){
        return courseMapper.getAllCourses();
    }
}
