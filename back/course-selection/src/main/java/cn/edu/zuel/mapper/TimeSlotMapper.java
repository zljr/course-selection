package cn.edu.zuel.mapper;

import cn.edu.zuel.model.TimeSlot;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface TimeSlotMapper extends BaseMapper<TimeSlot> {
    @Select("select *from timeslots where is_available=1;")
    List<TimeSlot> getAvailableTimeSlot();
    @Update("update timeslots set is_available=#{isAva} where time_slot_id=#{id};")
    Boolean setAvailable(Boolean isAva,Integer id);
}
