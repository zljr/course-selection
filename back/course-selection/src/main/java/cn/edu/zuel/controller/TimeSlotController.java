package cn.edu.zuel.controller;

import cn.edu.zuel.common.Result;
import cn.edu.zuel.model.TimeSlot;
import cn.edu.zuel.service.TimeSlotService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/timeslot")
public class TimeSlotController {
    @Resource
    private TimeSlotService timeSlotService;

    /*
    * 获取所有的时间段
    * */
    @GetMapping("/getAllTimeSlot")
    public Result getAllTimeSlot(){
        List<TimeSlot> timeSlotList = timeSlotService.list();
        return Result.success(timeSlotList);
    }

    /*
    * 获取空闲时间段
    * */
    @GetMapping("/getAvailableTimeSlot")
    public Result getAvailableTimeSlot(){
        List<TimeSlot> availableTimeSlot = timeSlotService.getAvailableTimeSlot();
        return Result.success(availableTimeSlot);
    }
    /*
    * 更新空闲时间段
    * */
    @PutMapping("/update")
    public Result update(@RequestBody TimeSlot timeSlot){
        if(timeSlotService.updateById(timeSlot)){
            return Result.success(timeSlot);
        }
        return Result.error("更新时间段信息失败");
    }

    /*
    * 修改时间段状态
    * */
    @PutMapping("/setAvailable")
    public Result setAvailable(@RequestParam Boolean isAva,
                               @RequestParam Integer timeSlotId){
        if(timeSlotService.setAvailable(isAva, timeSlotId)){
            return Result.success();
        }
        return Result.error("修改空闲时间状态失败");
    }
}
