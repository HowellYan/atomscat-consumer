package com.atomscat.consumer.config.aop;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.atomscat.consumer.config.annotation.RateLimiter;
import com.atomscat.consumer.config.redis.RedisRaterLimiter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class RateLimiterAspect {

    @Autowired
    private RedisRaterLimiter redisRaterLimiter;

    @Pointcut("@annotation(com.atomscat.consumer.config.annotation.RateLimiter)")
    public void controllerRateLimiterAspect() {
    }

    @Around("controllerRateLimiterAspect()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取相关参数
        Object[] arguments = joinPoint.getArgs();
        try {
            //获取目标类名
            String targetName = joinPoint.getTarget().getClass().getName();
            //获取方法名
            String methodName = joinPoint.getSignature().getName();
            //生成类对象
            Class targetClass = Class.forName(targetName);
            //获取该类中的方法
            Method[] methods = targetClass.getMethods();
            Integer limit = null;
            Integer timeout = null;
            for (Method method : methods) {
                if (!method.getName().equals(methodName)) {
                    continue;
                }
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length != arguments.length) {
                    //比较方法中参数个数与从切点中获取的参数个数是否相同，原因是方法可以重载哦
                    continue;
                }
                limit = method.getAnnotation(RateLimiter.class).limit();
                timeout = method.getAnnotation(RateLimiter.class).timeout();
            }
            log.info("targetClass: " + targetClass.getCanonicalName());
            log.info("methodName: " + methodName);
            log.info("limit: " + limit);
            log.info("timeout: " + timeout);
            String key = targetClass.getCanonicalName() + "." + methodName;
            String token = redisRaterLimiter.bucket(key, limit, timeout);
            if (StringUtils.isBlank(token)) {
                //throw new Exception("当前访问人数太多啦，请稍后再试");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Object retVal = joinPoint.proceed(arguments);
        return retVal;
    }
}
