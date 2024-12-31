package cn.edu.zuel.controller;

import cn.edu.zuel.annotation.AuthAccess;
import cn.edu.zuel.common.Result;
import cn.edu.zuel.model.Course;
import cn.edu.zuel.model.CourseDTO;
import cn.edu.zuel.model.User;
import cn.edu.zuel.service.CourseService;
import cn.edu.zuel.service.UserService;
import cn.edu.zuel.utils.TokenUtils;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Resource
    private CourseService courseService;

    @Resource
    private UserService userService;
    /*
    * 添加课程
    * */
    @PostMapping("/add")
    public Result add(@RequestBody Course course){
        User currentUser = TokenUtils.getCurrentUser();
        Integer teacherId =currentUser.getId();
        if(userService.getById(teacherId)==null){
            return Result.error("不存在该老师");
        }
        course.setTeacherId(teacherId);
        course.setTeacher(userService.getRealName(teacherId));
        try{
            courseService.save(course);
        }catch (Exception e){
            return Result.error("该课程已经存在！");
        }
        return Result.success(course);
    }
    /*
     * 查询所有课程(分页，用于返回所有课程以及相关信息)
     * */
    @GetMapping("/getAllCourses")
    public Result getAllCourses(@RequestParam Integer pageNum, @RequestParam Integer pageSize){
        PageInfo<CourseDTO> pageInfo=courseService.getAllCourses(pageNum,pageSize);
        return Result.success(pageInfo);

    }
    /*
    * 查询用户的课表
    * */
    @AuthAccess
    @GetMapping("/getCourseByUser")
    public Result getCourseByUser(@RequestParam Integer id){
        List<CourseDTO> courseByUser = courseService.getCourseByUser(id);
        return Result.success(courseByUser);
    }
    /*
    * 查询所有的课表
    * */
    @GetMapping("/getAllCourseTable")
    public Result getAllCourseTable(){
        List<CourseDTO> allCourses = courseService.getAllCourses();
        return Result.success(allCourses);
    }

    /*
     * 查询课程
     * */
    @GetMapping("/getCourseById/{courseId}")
    public Result getCourseById(@PathVariable Integer courseId){
        Course selectCourse=courseService.getById(courseId);
        User selectUser = userService.getById(selectCourse.getTeacherId());
        if(selectCourse==null){
            return Result.error("该课程不存在");
        }
        if(selectUser!=null){
            selectCourse.setTeacher(selectUser.getRealName());
        }
        return Result.success(selectCourse);
    }
    /*
     * 分页模糊查询课程
     * */
    @GetMapping("/getCourseByPage")
    public Result getCourseByPage(@RequestParam Integer pageNum,
                                  @RequestParam Integer pageSize,
                                  @RequestParam String courseName,
                                  @RequestParam String courseDescription){
        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<Course>().orderByDesc("course_id");
        courseQueryWrapper.like(!StrUtil.isBlank(courseName),"course_name",courseName);
        courseQueryWrapper.like(!StrUtil.isBlank(courseDescription),"course_description",courseDescription);
        Page<Course> coursePage = courseService.page(new Page<>(pageNum, pageSize),courseQueryWrapper);
        List<Course> courseRecords = coursePage.getRecords();
        for(Course record:courseRecords){
            Integer teacherId = record.getTeacherId();
            User user = userService.getById(teacherId);
            if(user!=null){
                record.setTeacher(user.getRealName());
            }
        }
        return Result.success(coursePage);

    }
    /*
    * 根据教师id查询课程
    * */
    @GetMapping("/getCourseByUserId")
    public Result getCourseByUserId(@RequestParam Integer teacherId,
                                    @RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize,
                                    @RequestParam String courseName,
                                    @RequestParam String courseDescription){
        if(teacherId==null){
            return Result.error("用户id为空");
        }
        if(userService.getById(teacherId)==null){
            return Result.error("不存在该用户");
        }
        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<Course>().orderByDesc("course_id");
        courseQueryWrapper.eq("teacher_id",teacherId);
        courseQueryWrapper.like(!StrUtil.isBlank(courseName),"course_name",courseName);
        courseQueryWrapper.like(!StrUtil.isBlank(courseDescription),"course_description",courseDescription);
        Page<Course> coursePage = courseService.page(new Page<>(pageNum, pageSize),courseQueryWrapper);
        List<Course> courseRecords = coursePage.getRecords();
        for(Course record:courseRecords){
            User user = userService.getById(teacherId);
                record.setTeacher(user.getRealName());
        }
        return Result.success(coursePage);
    }
    /*
     * 更新课程信息
     * */
    @PutMapping("/update")
    public Result update(@RequestBody Course course){
        User currentUser = TokenUtils.getCurrentUser();
        Integer teacherId =currentUser.getId();
        if(userService.getById(teacherId)==null){
            return Result.error("不存在该老师");
        }
        if(courseService.updateById(course)){
            return Result.success(course);
        }
        return Result.error("修改课程信息失败！");
    }
    /*
     * 删除课程信息
     * */
    @DeleteMapping("/delete/{courseId}")
    public Result delete(@PathVariable Integer courseId){
        if(courseService.removeById(courseId)){
            return Result.success("删除课程成功！");
        }
        return Result.error("删除课程失败！");
    }
    /*
     * 删除课程信息
     * */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> courseIds){
        if(courseService.removeBatchByIds(courseIds)){
            return Result.success("批量删除成功！");
        }
        return Result.error("批量删除失败！");
    }
    /*
     * 导出课程信息
     * */
    @GetMapping("/export")
    public void export(@RequestParam(required = false) String courseName,
                       @RequestParam(required = false) String courseDescription,
                       @RequestParam(required = false) String courseIds,
                       HttpServletResponse response) throws IOException {//通过流的形式导出，所以是void，这俩参数可以没有
        ExcelWriter writer = ExcelUtil.getWriter(true);
        List<Course> list;
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        if(StrUtil.isNotBlank(courseIds)){
            /*
             * 传来的是字符串，先转化为字符串数组再转化为整型数组再转化为List集合
             * */
            List<Integer> collect = Arrays.stream(courseIds.split(",")).map(Integer::valueOf).collect(Collectors.toList());
            queryWrapper.in("course_id",collect);
        }else{
            queryWrapper.like(StrUtil.isNotBlank(courseName),"course_name",courseName);
            queryWrapper.like(StrUtil.isNotBlank(courseDescription),"course_description",courseDescription);
        /*
        这里很巧妙，如果说没有使用模糊搜索匹配，这里就默认查询全部，如果有的话就根据查询到的结果进行返回
        * */
        }
        list=courseService.list(queryWrapper);

        writer.write(list,true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode("课程信息表","UTF-8")+".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream,true);//把writer对象中的数据刷新到输出数据流中去,true代表刷新完成之后是否关闭输出流
        writer.close();
        outputStream.flush();
        outputStream.close();

    }
    /*
     * 导入课程信息
     * */
    @PostMapping("/import")
    public Result importData(MultipartFile file) throws IOException {
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        List<Course> courseList= reader.readAll(Course.class);
        try{
            courseService.saveBatch(courseList);
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("数据批量上传错误");
        }

        return Result.success();
    }
}
