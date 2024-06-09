package com.trun.fun.common.pression;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 文本解析类
 *
 * @author mawei
 */
public class Context {
    private String text;

    private boolean isParser;

    public Context(String text, boolean isParser) {
        this.text = text;
        this.isParser = isParser;
    }

    public String getText() {
        return text;
    }

    public boolean isParser() {
        return isParser;
    }

    public String getValue() {
        return this.text;
    }

    public String getSuperText() {
        return this.text.substring(0, this.text.lastIndexOf("."));
    }

    public String getCollectionText() {
        return this.text.substring(0, this.text.lastIndexOf("::"));
    }
    public String getCollectionFieldName() {
        return this.text.substring(this.text.lastIndexOf("::") + 2);
    }
    private String getFieldName() {
        return this.text.substring(this.text.lastIndexOf(".") + 1);
    }

    public String getValue(EvaluationContext ctx) {
        String str = "#{"+this.text+"}";
        try {
            ExpressionParser paser = new SpelExpressionParser();//创建表达式解析器
           if(this.text.indexOf("::")!=-1){
               Object checkIsCollection = paser.parseExpression(getCollectionText()).getValue(ctx);
               //集合的属性
               if (checkIsCollection(checkIsCollection)) {
                   String fieldName[] = getCollectionFieldName().split(",");
                   str = getCollectionFieldValue(checkIsCollection, fieldName);
                   return str;
               }
           }
            Object obj = paser.parseExpression(this.getText()).getValue(ctx);
            if (checkFieldType(obj)) {
                //获取上级对象得到中文说明
                Object superObj = paser.parseExpression(getSuperText()).getValue(ctx);
                //查找对应的中文及值
                String fieldName = getFieldName();
                str = getSuperValue(superObj, fieldName);

            } else {
                StringBuilder buff = new StringBuilder();
                getObjectValue(obj, buff);
                str = buff.toString();

            }

        } catch (Exception e) {

        }
        return str;

    }


    private String getSuperValue(Object param, String fieldName) {
        StringBuilder str = new StringBuilder("");
        try {
            Field[] fields = param.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);//
                Object value = field.get(param);//
                String name = field.getName();
                if (name.equals(fieldName)) {
                    ApiModelProperty propertyName = field.getAnnotation(ApiModelProperty.class);//获取字段中文名称
                    if (propertyName != null) {
                        if (!propertyName.notes().equals("")) {
                            name = propertyName.notes();
                        }
                        if (!propertyName.value().equals("")) {
                            name = propertyName.value();
                        }
                    }
                    str.append(name + "=" + getFieldValue(value)+" ");
                }
            }
        } catch (Exception e) {
            System.err.println("err:" + e);
        }
        return str.toString();
    }

    private String getFieldValue(Object param) {
        StringBuilder str = new StringBuilder("");
        if (param instanceof Integer) {
            int value = ((Integer) param).intValue();
            str.append(value);
        } else if (param instanceof String) {
            String s = (String) param;
            str.append(s);
        } else if (param instanceof Double) {
            double d = ((Double) param).doubleValue();
            str.append(d);
        } else if (param instanceof Float) {
            float f = ((Float) param).floatValue();
            str.append(f);
        } else if (param instanceof Long) {
            long l = ((Long) param).longValue();
            str.append(l);
        } else if (param instanceof Boolean) {
            boolean b = ((Boolean) param).booleanValue();
            str.append(b);
        } else if (param instanceof Date) {
            Date d = (Date) param;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(d);
            str.append(dateString);
        } else if (param instanceof BigDecimal) {
            BigDecimal big = (BigDecimal) param;
            str.append(big.toString());
        }
        return str.toString();
    }


    private void getObjectValue(Object param, StringBuilder str) {
        if (param == null)
            return;
        if (checkFieldType(param)) {
            //无法获取中文解析器 只能取到值
            str.append(getFieldValue(param));
        }
        if (param instanceof Collection) {
            Collection c = (List) param;
            Iterator ite1 = c.iterator();
            while (ite1.hasNext()) {
                Object o = ite1.next();
                if (!checkFieldType(o)) {
                    getObjectValue(o, str);
                } else {
                    //无法获取中文解析器 只能取到值
                    str.append(getFieldValue(o) + ",");
                }

            }
        }
        if (param instanceof Map) {
            Map m = (Map) param;
            for (Object key : m.keySet()) {
                Object o = m.get(key);
                if (!checkFieldType(o)) {
                    getObjectValue(o, str);
                } else {
                    //无法获取中文解析器 只能取到值
                    str.append(getFieldValue(o) + ",");
                }
            }
        } else {
            //迭代字段
            try {
                Field[] fields = param.getClass().getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);//
                    Object value = field.get(param);//
                    String name = field.getName();
                    ApiModelProperty propertyName = field.getAnnotation(ApiModelProperty.class);//获取字段中文名称
                    if (propertyName != null) {
                        if (!propertyName.notes().equals("")) {
                            name = propertyName.notes();
                        }
                        if (!propertyName.value().equals("")) {
                            name = propertyName.value();
                        }
                    }
                    //基础数据类型获取中文名称加值
                    if (LogTextParsingUtil.checkObjectIsSysType(field.getType().getName())) {
                        str.append(name + "=" + value + ",");
                    } else {
                        //集合或其他对象
                        str.append(name + "=");
                        getObjectValue(value, str);
                    }
                }
            } catch (Exception e) {
                System.err.println("err:" + e);
            }
        }

    }


    private boolean checkFieldType(Object param) {
        if (param == null) {
            return true;
        }
        if (param instanceof Integer) {
            return true;
        } else if (param instanceof String) {
            return true;
        } else if (param instanceof Double) {
            return true;
        } else if (param instanceof Float) {
            return true;
        } else if (param instanceof Long) {
            return true;
        } else if (param instanceof Boolean) {
            return true;
        } else if (param instanceof Date) {
            return true;
        } else if (param instanceof BigDecimal) {
            return true;
        }
        return false;
    }

    private boolean checkIsCollection(Object param) {
        if (param instanceof Collection) {
            return true;
        }
        if (param instanceof Map) {
            return true;
        }
        return false;
    }

    private String getCollectionFieldValue(Object param, String[] fieldName) {
        StringBuilder buff = new StringBuilder();
        if (param instanceof Collection) {
            Collection c = (List) param;
            Iterator ite1 = c.iterator();
            while (ite1.hasNext()) {
                Object o = ite1.next();
                if (!checkFieldType(o)) {
                    for (int i = 0; i <fieldName.length ; i++) {
                        buff.append(getSuperValue(o, fieldName[i]));

                    }
                } else {
                    //无法获取中文解析器 只能取到值
                    buff.append(getFieldValue(o) + ",");
                }

            }
        }
        return buff.toString();
    }
}
