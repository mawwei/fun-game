package com.trun.fun.framework.mybatisplus;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.AbstractSqlInjector;
import com.baomidou.mybatisplus.core.injector.methods.*;
import com.baomidou.mybatisplus.extension.injector.methods.additional.InsertBatchSomeColumn;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * MybatisPlusSql注入器
 * </p>
 *
 * @author Mawei
 */
public class MybatisPlusSqlInjector extends AbstractSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList() {
        return Stream.of(
                new Insert(),
                new InsertBatchSomeColumn(t -> true),
                new Delete(),
                new DeleteById(),
                new Update(),
                new UpdateById(),
                new UpdateAllColumnById(),
                new SelectById(),
                new SelectCount(),
                new SelectObjs(),
                new SelectList(),
                new SelectPage()
        ).collect(Collectors.toList());
    }

}
