package cn.edu.zuel.service;

import cn.edu.zuel.mapper.CourseTimeMapper;
import cn.edu.zuel.model.CourseTimeTable;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CourseTimeService extends ServiceImpl<CourseTimeMapper, CourseTimeTable> {
}
