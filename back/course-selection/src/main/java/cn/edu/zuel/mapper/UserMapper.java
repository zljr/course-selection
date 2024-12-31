package cn.edu.zuel.mapper;

import cn.edu.zuel.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

public interface UserMapper extends BaseMapper<User> {
    /*
    * 获取真实姓名
    * */
    @Select("select real_name from user where id=#{id};")
    String getRealName(Integer id);
}
