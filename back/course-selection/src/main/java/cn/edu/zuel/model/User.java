package cn.edu.zuel.model;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
/*
* 使用lombok的Data注解可以不用写get和set方法
* */
@TableName("`user`")
public class User {
    /*
    * 用户id(主键)*/
    @TableId(type= IdType.AUTO)
    @Alias("序号")
    private Integer id;
    /*
    * 用户名*/
    @Alias("用户名")
    private String username;
    /*
    * 用户密码*/
    @Alias("密码")
    private String password;
    /*
    * 真实姓名*/
    @Alias("真实姓名")
    private String realName;
    /*
    * 性别*/
    @Alias("性别")
    private String sex;
    /*
    * 联系方式
    * */
    @Alias("电话")
    private String telephone;
    /*
    *
    * 角色
    * */
    @Alias("角色")
    private String role;
    /*
    * 创建时间*/
    @Alias("创建时间")
    private String createTime;
    /*
    * 更新时间*/
    @Alias("修改时间")
    private String updateTime;
    /*
    * 头像
    * */
    @Alias("头像")
    private String avatar;

    @TableField(exist = false)
    private String token;

}
