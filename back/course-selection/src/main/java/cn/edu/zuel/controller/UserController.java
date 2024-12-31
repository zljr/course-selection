package cn.edu.zuel.controller;

import cn.edu.zuel.common.Result;
import cn.edu.zuel.exception.ServiceException;
import cn.edu.zuel.model.User;
import cn.edu.zuel.service.UserService;
import cn.edu.zuel.utils.TokenUtils;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    /*
    * 新增用户
    * */
    @PostMapping("/add")
    public Result add(@RequestBody User user){
        try{
            userService.save(user);
        }catch (Exception e){
            return Result.error("用户名已存在！");
        }
        return Result.success(user);
    }

    /*
    * 修改用户信息
    * */
    @PutMapping("/update")
    public Result update(@RequestBody User user){
        if(userService.updateById(user)){
            return Result.success(user);
        }
        return Result.error("更新用户信息失败");
    }
    /*
    * 删除用户
    * */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        /*
        * 验证要删除的用户是否是当前用户，如果是则无法删除
        * */
        User currentUser = TokenUtils.getCurrentUser();
        if(id.equals(currentUser.getId())){
            throw new ServiceException("无法删除该用户");
        }
        if(userService.removeById(id)){
            return Result.success();
        }
        return Result.error("删除用户失败！");
    }
    /*
    * 批量删除用户
    * */
    @DeleteMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids){
        User currentUser = TokenUtils.getCurrentUser();
        if(currentUser!=null&&currentUser.getId()!=null&&ids.contains(currentUser.getId())){
            throw new ServiceException("无法删除该用户");
        }
        userService.removeByIds(ids);
        return Result.success("批量删除成功");
    }
    /*
    * 查询所有用户信息
    * */
    @GetMapping("/getAllUser")
    public Result getAllUsers(){
        List<User> userList;
        try{
            userList=userService.list(new QueryWrapper<User>().orderByDesc("id"));
        }catch (Exception e){
            return Result.error("未查找到相关用户信息");
        }
        return Result.success(userList);
    }
    /*
    *根据id查询
    *
    **/
    @GetMapping("/getUserById/{id}")
    public Result getUserById(@PathVariable Integer id){
        if(userService.getById(id)==null){
            return Result.error("该用户不存在！");
        }
        return Result.success(userService.getById(id));
    }
    /*
    * 根据用户名查询
    * */
    @GetMapping("/getByUsername/{username}")
    public Result getByUsername(@PathVariable String username){
        if(userService.selectByUsername(username)==null){
            return Result.error("该用户不存在！");
        }
        return Result.success(userService.selectByUsername(username));
    }
    /*
    * 分页多条件模糊查询
    * */
    @GetMapping("/getUserByPage")
    public Result getUserByPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam String username,
                                @RequestParam String realName){
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().orderByDesc("id");
        queryWrapper.like(!StrUtil.isBlank(username),"username",username);
        queryWrapper.like(!StrUtil.isBlank(realName),"real_name",realName);

        Page<User> userPage = userService.page(new Page<>(pageNum, pageSize), queryWrapper);
        return Result.success(userPage);
    }
    /*
    * 获取真实姓名
    * */
    @GetMapping("/getRealName/{id}")
    public Result getRealName(@PathVariable Integer id){
        if(id==null){
            return Result.error("未查询到该用户名");
        }
        String realName = userService.getRealName(id);
        return Result.success(realName);
    }

    @GetMapping("/export")
    public void export(@RequestParam(required = false) String username,
                       @RequestParam(required = false) String realName,
                       @RequestParam(required = false) String ids,
                       HttpServletResponse response) throws IOException {//通过流的形式导出，所以是void，这俩参数可以没有
        ExcelWriter writer = ExcelUtil.getWriter(true);
        List<User> list;
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StrUtil.isNotBlank(ids)){
            /*
             * 传来的是字符串，先转化为字符串数组再转化为整型数组再转化为List集合
             * */
            List<Integer> collect = Arrays.stream(ids.split(",")).map(Integer::valueOf).collect(Collectors.toList());
            queryWrapper.in("id",collect);
        }else{
            queryWrapper.like(StrUtil.isNotBlank(username),"username",username);
            queryWrapper.like(StrUtil.isNotBlank(realName),"real_name",realName);
        /*
        这里很巧妙，如果说没有使用模糊搜索匹配，这里就默认查询全部，如果有的话就根据查询到的结果进行返回
        * */
        }
        list=userService.list(queryWrapper);

        writer.write(list,true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode("用户信息表","UTF-8")+".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream,true);//把writer对象中的数据刷新到输出数据流中去,true代表刷新完成之后是否关闭输出流
        writer.close();
        outputStream.flush();
        outputStream.close();

    }

    @PostMapping("/import")
    public Result importData(MultipartFile file) throws IOException {
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        List<User> userList = reader.readAll(User.class);
        try{
            userService.saveBatch(userList);
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("数据批量上传错误");
        }

        return Result.success();
    }
}
