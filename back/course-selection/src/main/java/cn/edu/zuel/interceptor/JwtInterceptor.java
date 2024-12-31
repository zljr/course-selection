package cn.edu.zuel.interceptor;

import cn.edu.zuel.annotation.AuthAccess;
import cn.edu.zuel.exception.ServiceException;
import cn.edu.zuel.mapper.UserMapper;
import cn.edu.zuel.model.User;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtInterceptor implements HandlerInterceptor {
    @Resource
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token=request.getHeader("token");
        if(StrUtil.isBlank(token)){
            token=request.getParameter("token");
        }
//        通过上面两种方式获取token
//        如果是注解方法则直接通过
        if(handler instanceof HandlerMethod){
           AuthAccess annotation = ((HandlerMethod) handler).getMethodAnnotation(AuthAccess.class);
           if(annotation!=null){
               return true;
           }
        }
//        token为空则重新登录
        if(StrUtil.isBlank(token)){
            throw new ServiceException("401","请登录");
        }
//        解码token
        String userId;
        try{
            userId= JWT.decode(token).getAudience().get(0);
        }
        catch (JWTDecodeException j){
            throw new ServiceException("401","请登录");
        }
//若不存在该用户则登录
        User user = userMapper.selectById(Integer.valueOf(userId));
        if(user==null){
            throw new ServiceException("401","请登录");
        }
//        验证token
        JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try{
            jwtVerifier.verify(token);
        }catch (JWTVerificationException j){
            throw new ServiceException("401","请登录");
        }
        return true;
    }
}
