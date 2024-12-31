package cn.edu.zuel.model;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Time;
@Data
@TableName("`timeslots`")
public class TimeSlot {
    @TableId(type= IdType.AUTO)
    @Alias("时间段序号")
    private Integer timeSlotId;
    @Alias("星期几")
    private String dayOfWeek;
    @Alias("开始时间")
    private Time startTime;
    @Alias("结束时间")
    private Time endTime;
    @Alias("是否空闲")
    private Boolean isAvailable;
}
