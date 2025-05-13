package cn.edu.zuel.controller;

import cn.edu.zuel.common.Result;
import cn.edu.zuel.model.StudentCourse;
import cn.edu.zuel.model.StudentCourseDTO;
import cn.edu.zuel.service.StudentCourseService;
import cn.edu.zuel.service.UserService;
import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/studentCourse")
public class StudentCourseController {
    @Resource
    private StudentCourseService studentCourseService;
    @Resource
    private UserService userService;

    /*
     * 添加学生课程信息，学生选课或者管理员添加学生选课
     * */
    @PostMapping("/add")
    public Result add(@RequestBody StudentCourse studentCourse) {
        //        未选择课程
        if (studentCourse == null) {
            return Result.error("未选择课程");
        }
        Integer studentId = studentCourse.getStudentId();
        Integer courseId = studentCourse.getCourseId();
        studentCourse.setStudentId(studentId);
        studentCourse.setEnrollmentTime(DateUtil.now());
//        判断是否已经选择了这门课
        if (studentCourseService.hasSelectCourse(studentId, courseId)!=null) {
            return Result.error("选过这门课啦，无法再选");
        }
//        判断该门课程是否与该学生已经选的课程有时间冲突
        if(studentCourseService.hasConflictCourse(studentId,courseId)){
            return Result.error("该门课程与所选课程时间冲突");
        }
        studentCourseService.save(studentCourse);
        return Result.success();
    }

    /*
     * 删除学生课程，适用于学生退课，管理员管理学生选课
     * */
    @DeleteMapping("/delete/{studentCourseId}")
    public Result delete(@PathVariable Integer studentCourseId) {
        if (studentCourseId == null) {
            return Result.error("未选择课程");
        }
        if (studentCourseService.removeById(studentCourseId)) {
            return Result.success();
        }
        return Result.error("退课失败");
    }

    /*
     * 查询学生选课信息
     * */
    @GetMapping("/getAllEnrolledCourses")
    public Result getAllEnrolledCourses(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize) {
        PageInfo<StudentCourseDTO> pageInfo = studentCourseService.getAllEnrolledCourses(pageNum, pageSize);
        List<StudentCourseDTO> list = pageInfo.getList();
        for (StudentCourseDTO li : list) {
            Integer studentId = li.getStudentId();
            String stuName = userService.getRealName(studentId);
            if (stuName != null) {
                li.setStudentName(stuName);
            }
        }
        return Result.success(pageInfo);
    }


    @GetMapping("/getEnrolledCoursesByStudent")
    public Result getEnrolledCoursesByStudent(@RequestParam Integer pageNum,
                                              @RequestParam Integer pageSize,
                                              @RequestParam Integer studentId) {
        PageInfo<StudentCourseDTO> pageInfo = studentCourseService.getEnrolledCoursesByStudent(studentId, pageNum, pageSize);
        return Result.success(pageInfo);
    }
    /*
    * 不需要分页，用于返回学生的课表
    * */
    @GetMapping("/getCourseByStudent")
    public Result getCourseByStudent(@RequestParam Integer studentId){
        return Result.success(studentCourseService.getCourseByStudent(studentId));
    }
}
