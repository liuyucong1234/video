package com.lyc.interceptor;

import com.lyc.utils.JSONResult;
import com.lyc.utils.JsonUtils;
import com.lyc.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

public class MiniInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisOperator redisOperator;

    public static final String redis_session="redis-session";

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //获取前端传来的信息
        String userId=httpServletRequest.getHeader("userId");
        String userToken=httpServletRequest.getHeader("userToken");

        if(StringUtils.isNotBlank(userId)&&StringUtils.isNotBlank(userToken)){

            //获取redis数据库中的信息
            String nowkey=redis_session+":"+userId;
            String nowToken=redisOperator.get(nowkey);
            //判断
            if(StringUtils.isEmpty(nowToken)&&StringUtils.isBlank(nowToken)){
                returnErrorResponse(httpServletResponse, new JSONResult().errorTokenMsg("请登录..."));
                return false;
            }else{
                if(!StringUtils.equals(nowToken,userToken)){
                    returnErrorResponse(httpServletResponse,new JSONResult().errorTokenMsg("账号被挤掉"));
                    return false;
                }
            }
        }else{
            returnErrorResponse(httpServletResponse,new JSONResult().errorTokenMsg("请登录"));
            return false;
        }
        return true;
    }


    /**
     * @Description:返回错误信息
     * @param response
     * @param jsonResult
     * @throws Exception
     */
    public void returnErrorResponse(HttpServletResponse response, JSONResult jsonResult)
    throws Exception{
        OutputStream outputStream=null;
        try{
            response.setCharacterEncoding("utf-8");
            response.setContentType("txt/json");
            outputStream=response.getOutputStream();
            outputStream.write(JsonUtils.objectToJson(jsonResult).getBytes("utf-8"));
            outputStream.flush();

        }finally {
            if(outputStream!=null){
                outputStream.close();
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
