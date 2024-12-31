package cn.edu.zuel.controller;

import cn.edu.zuel.common.Result;
import cn.edu.zuel.exception.ServiceException;
import cn.edu.zuel.model.CourseTimeTable;
import cn.edu.zuel.service.CourseTimeService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/courseTime")
public class CourseTimeController {
    @Resource
    private CourseTimeService courseTimeService;

    @PostMapping("/add")
    public Result add(@RequestBody CourseTimeTable courseTimeTable){
        try{
            if(courseTimeService.save(courseTimeTable)){
                return Result.success(courseTimeTable);
            }
        }catch (DuplicateKeyException dke){
            /*
            * 如果有两门课在同一个教室的同一个时间段上
            * */
            dke.printStackTrace();
            throw new ServiceException("该时间段的教室已经被占用");
        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("插入课程时间失败");
        }
        return Result.error("插入课程时间失败");
    }
    @PutMapping("/update")
    public Result update(@RequestBody CourseTimeTable courseTimeTable){
        try{
            if(courseTimeService.save(courseTimeTable)){
                return Result.success(courseTimeTable);
            }
        }catch (DuplicateKeyException e){
            /*
             * 如果有两门课在同一个教室的同一个时间段上
             * */
            e.printStackTrace();
            throw new ServiceException("该时间段的教室已经被占用");
        }catch (Exception e){
            throw new ServiceException("插入课程时间失败");
        }
        return Result.error("更新课程时间失败");
    }
    /*
    *
    * 删除课程时间信息*/
    @DeleteMapping("/delete/{courseTimeTableId}")
    public Result delete(@PathVariable Integer courseTimeTableId){
        if(courseTimeService.removeById(courseTimeTableId)){
            return Result.success();
        }
        return Result.error("删除课程时间信息失败");
    }

}
