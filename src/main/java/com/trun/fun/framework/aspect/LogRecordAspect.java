package com.trun.fun.framework.aspect;

import com.trun.fun.framework.utils.LogUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Controller统一切点日志处理
 */
@Aspect
public class LogRecordAspect {

    @Pointcut("execution(public * com.digitalgd.dmp.controller.*RestController.*(..))")
    @SuppressWarnings("EmptyMethod")
    public void pointCut() {
    }

    @AfterReturning(returning = "ret", pointcut = "pointCut()")
    public void doAfterReturning(Object ret) {

        LogUtils.doAfterReturning(ret);
    }

}
