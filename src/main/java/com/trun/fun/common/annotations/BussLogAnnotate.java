package com.trun.fun.common.annotations;



import com.trun.fun.common.enums.LogTypeEnum;

import java.lang.annotation.*;

/**
 * 业务日志注解
 * 实现往ES输出日志
 * 注解实现AOP编程。利用swagger注解获取相关字段信息。
 * 请求日志上下文无法抽出通用。一般上下文是 执行方法中文名称+执行结果（中文）+执行入参（中文）
 * 注解可使用模板模式 设置属性contentType=TEMPLATE及可。文本写content=${name} 会自动输出该日志入“参中文名称=值"内容。前提是该字段名称需要有swagger的ApiModelProperty注解说明
 * 如模板不满足日志文本要求 设置属性contentType=CUSTOM 默认为CUSTOM。在方法内拼接参数。拼接如下：
 * BussLogContentHolder.setConText(content); 注解会自动将文本及日志信息输出至ES。
 *
 *
 * 警告：使用日志注解需要写swagger中文注解。如请求类 请求方法中文 参数的中文说明注解。找不到中午注解会输出null ""等信息。
 *
 * @author mawei
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface BussLogAnnotate {

    String content() default "";//日志文本
    LogTypeEnum logType() default LogTypeEnum.MANUAL;//日志类型
    String bussType();//业务类型
    String pcDesc()default "";//系统名称
}
