package ru.romzhel.eshop.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Aspect
@Configuration
public class AppLoggingAspect {
    private static final Logger logger = LogManager.getLogger(AppLoggingAspect.class);

    @Pointcut("execution(public * ru.romzhel.eshop.controllers.*.*(..))")
    public void callControllerMethod() {
    }

    @Pointcut("execution(public * ru.romzhel.eshop.services.*.*(..))")
    public void callServiceMethod() {
    }

    @Around("callControllerMethod()")
    public Object calcControllersExecTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        logger.trace("==>| {}(), args = {}", methodSignature.getName(), Arrays.toString(proceedingJoinPoint.getArgs()));
        long t0 = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        logger.trace("<==| {}(), template = '{}', params = {}", methodSignature.getName(), result, proceedingJoinPoint.getArgs());
        logger.trace("<=>| {}(), time elapsed: {} msec", methodSignature.getName(), (System.currentTimeMillis() - t0));
        return result;
    }

    @AfterThrowing(value = "callControllerMethod()", throwing = "ex")
    public void loggingControllerException(JoinPoint joinPoint, Throwable ex) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        logger.error("!!!| Exception {}\n{}", methodSignature, ex.getStackTrace());
    }

    @Before("callServiceMethod()")
    public void loggingServices(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        logger.info("-->| {}(), args = {}", methodSignature.getName(), Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "callServiceMethod()", returning = "result")
    public void loggingServicesResult(JoinPoint joinPoint, Object result) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        logger.info("<--| {}(), data = '{}', params = {}", methodSignature.getName(), result, joinPoint.getArgs());
    }

    @Around("callServiceMethod()")
    public Object calcServicesExecTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        long t0 = System.currentTimeMillis();
        Object res = proceedingJoinPoint.proceed();
        logger.info("<->| {}(), time elapsed: {} msec", methodSignature.getName(), (System.currentTimeMillis() - t0));
        return res;
    }

    @AfterThrowing(value = "callServiceMethod()", throwing = "ex")
    public void loggingServiceException(JoinPoint joinPoint, Throwable ex) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        logger.error("!!!| Exception {}\n{}", methodSignature, ex.getStackTrace());
    }
}
