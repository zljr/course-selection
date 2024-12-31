package cn.edu.zuel.service;

import cn.edu.zuel.mapper.TimeSlotMapper;
import cn.edu.zuel.model.TimeSlot;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TimeSlotService extends ServiceImpl<TimeSlotMapper, TimeSlot> {
    @Resource
    private TimeSlotMapper timeSlotMapper;
    public List<TimeSlot> getAvailableTimeSlot(){
        return timeSlotMapper.getAvailableTimeSlot();
    }
    public Boolean setAvailable(Boolean isAva,Integer id){
        return timeSlotMapper.setAvailable(isAva,id);
    }
}
