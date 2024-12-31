package cn.edu.zuel.service;

import cn.edu.zuel.exception.ServiceException;
import cn.edu.zuel.mapper.UserMapper;
import cn.edu.zuel.model.User;
import cn.edu.zuel.utils.TokenUtils;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
/*
* 提供处理用户信息的方法
* */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    @Resource
    private UserMapper userMapper;

    @Override
    public boolean save(User entity) {
        if(StrUtil.isBlank(entity.getRealName())){
            /*
            * 如果真实姓名为空，则默认为用户名
            * */
            entity.setRealName(entity.getUsername());
        }
        if(StrUtil.isBlank(entity.getPassword())){
            /*
            * 若密码为空，则设置为123456
            * */
            entity.setPassword("123456");
        }
        if(StrUtil.isBlank(entity.getRole())){
            /*
            *
            * 如果角色为空，默认为学生*/
            entity.setRole("学生");
        }
        return super.save(entity);
    }

    @Override
    public boolean updateById(User entity) {
        entity.setUpdateTime(DateUtil.now());
        return super.updateById(entity);
    }

    public User selectByUsername(String username){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return this.getOne(queryWrapper);

    }

    public User login(User user){
        User selectUser = selectByUsername(user.getUsername());
        if(selectUser == null){
            throw new ServiceException("用户名或密码错误");
        }
        if(!user.getPassword().equals(selectUser.getPassword())){
            throw new ServiceException("用户名或密码错误");
        }
        /*
        * 生成token
        * */
        String token=TokenUtils.genToken(selectUser.getId().toString(),selectUser.getPassword());
        selectUser.setToken(token);
        return selectUser;
    }

    public User register(User user){
        /*
        * 看注册的用户是否已经存在
        * */
        User selectUser = selectByUsername(user.getUsername());
        if(selectUser!=null){
            throw new ServiceException("该账号已经存在");
        }
        user.setRealName(user.getUsername());
        user.setCreateTime(DateUtil.now());
        userMapper.insert(user);
        return user;
    }
    public void resetPassword(User user){
        User selectUser = selectByUsername(user.getUsername());
        if(selectUser ==null){
            throw new ServiceException("该用户不存在");
        }
        if(selectUser.getTelephone().equals(user.getTelephone())){
            throw new ServiceException("验证错误");
        }
        selectUser.setPassword("123456");
        updateById(selectUser);
    }
    /*
    * 获取真实姓名
    * */
    public String getRealName(Integer id){
        return userMapper.getRealName(id);
    }
}
