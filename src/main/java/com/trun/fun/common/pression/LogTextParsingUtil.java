package com.trun.fun.common.pression;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

public class LogTextParsingUtil {



    public static String parseExpression(String exp, String objName,Object obj){
        StringBuffer buf=new StringBuffer();
        if(exp==null){
            return  buf.toString();
        }
        EvaluationContext ctx = new StandardEvaluationContext();
        ctx.setVariable(objName, obj);

        Context[] expressions=new ContentTextParser(exp,new TemplateParserContext()).getContens();
        for (int i = 0; i <expressions.length ; i++) {
            if (expressions[i].isParser()) {
                buf.append(expressions[i].getValue(ctx));
            } else {
                buf.append(expressions[i].getValue());
            }
        }
          return buf.toString();
    }

    public static String parseExpressionBySpringEL(String exp,String objName,Object obj){
        if(exp==null){
            return  "";
        }
        ExpressionParser paser = new SpelExpressionParser();
        EvaluationContext ctx = new StandardEvaluationContext();
        ctx.setVariable(objName,obj);
        String  content =paser.parseExpression(exp,new TemplateParserContext()).getValue(ctx,String.class);
        return content;
    }

    public static int skipToCorrectEndSuffix(String suffix, String expressionString, int afterPrefixIndex)
            throws ParseException {

        // Chew on the expression text - relying on the rules:
        // brackets must be in pairs: () [] {}
        // string literals are "..." or '...' and these may contain unmatched brackets
        int pos = afterPrefixIndex;
        int maxlen = expressionString.length();
        int nextSuffix = expressionString.indexOf(suffix, afterPrefixIndex);
        if (nextSuffix == -1) {
            return -1; // the suffix is missing
        }
        Deque<Bracket> stack = new ArrayDeque<>();
        while (pos < maxlen) {
            if (isSuffixHere(expressionString, pos, suffix) && stack.isEmpty()) {
                break;
            }
            char ch = expressionString.charAt(pos);
            switch (ch) {
                case '{':
                case '[':
                case '(':
                    stack.push(new Bracket(ch, pos));
                    break;
                case '}':
                case ']':
                case ')':
                    if (stack.isEmpty()) {
                        throw new ParseException(expressionString, pos, "Found closing '" + ch +
                                "' at position " + pos + " without an opening '" +
                                LogTextParsingUtil.Bracket.theOpenBracketFor(ch) + "'");
                    }
                    LogTextParsingUtil.Bracket p = stack.pop();
                    if (!p.compatibleWithCloseBracket(ch)) {
                        throw new ParseException(expressionString, pos, "Found closing '" + ch +
                                "' at position " + pos + " but most recent opening is '" + p.bracket +
                                "' at position " + p.pos);
                    }
                    break;
                case '\'':
                case '"':
                    // jump to the end of the literal
                    int endLiteral = expressionString.indexOf(ch, pos + 1);
                    if (endLiteral == -1) {
                        throw new ParseException(expressionString, pos,
                                "Found non terminating string literal starting at position " + pos);
                    }
                    pos = endLiteral;
                    break;
            }
            pos++;
        }
        if (!stack.isEmpty()) {
            LogTextParsingUtil.Bracket p = stack.pop();
            throw new ParseException(expressionString, p.pos, "Missing closing '" +
                   LogTextParsingUtil.Bracket.theCloseBracketFor(p.bracket) + "' for '" + p.bracket + "' at position " + p.pos);
        }
        if (!isSuffixHere(expressionString, pos, suffix)) {
            return -1;
        }
        return pos;
    }


    private static boolean isSuffixHere(String expressionString, int pos, String suffix) {
        int suffixPosition = 0;
        for (int i = 0; i < suffix.length() && pos < expressionString.length(); i++) {
            if (expressionString.charAt(pos++) != suffix.charAt(suffixPosition++)) {
                return false;
            }
        }
        if (suffixPosition != suffix.length()) {
            // the expressionString ran out before the suffix could entirely be found
            return false;
        }
        return true;
    }

    private static class Bracket {

        char bracket;

        int pos;

        Bracket(char bracket, int pos) {
            this.bracket = bracket;
            this.pos = pos;
        }

        boolean compatibleWithCloseBracket(char closeBracket) {
            if (this.bracket == '{') {
                return closeBracket == '}';
            }
            else if (this.bracket == '[') {
                return closeBracket == ']';
            }
            return closeBracket == ')';
        }

        static char theOpenBracketFor(char closeBracket) {
            if (closeBracket == '}') {
                return '{';
            }
            else if (closeBracket == ']') {
                return '[';
            }
            return '(';
        }

        static char theCloseBracketFor(char openBracket) {
            if (openBracket == '{') {
                return '}';
            }
            else if (openBracket == '[') {
                return ']';
            }
            return ')';
        }
    }



    /**
     * 获取所有入参中文释义
     *
     * @param cls
     * @param resultMap
     * @return
     */
    public static Map<String, String> chineseDescription(Class cls, Map<String, String> resultMap) {
        try {
            if (cls == null) {
                //如果object是null/基本数据类型/包装类/日期类型，则不需要在递归调用
                resultMap.put("Object", null);
            }

            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                ApiModelProperty propertyName = field.getAnnotation(ApiModelProperty.class);//获取注解中文名称
                if (propertyName != null) {
                    resultMap.put(field.getName(), propertyName.notes());
                }
                if (field.getType().getName().equals("java.util.List")) {
                    continue;
                }
                if (field.getType().getName().equals("java.util.Map")) {
                    continue;
                }
                if (field.getType().getName().equals("java.util.Set")) {
                    continue;
                }
                //基础类型获取字段值
                if (!checkObjectIsSysType(field.getType().getName())) {
                    chineseDescription(Class.forName(field.getType().getName()), resultMap);
                }
            }

        } catch (Exception e) {
            System.err.println("err:" + e);
        }
        return resultMap;
    }

    /**
     * 检查object是否为java的基本数据类型/包装类/java.util.Date/java.sql.Date
     *
     * @param objType
     * @return
     */
    public static boolean checkObjectIsSysType(String objType) {
        if ("byte".equals(objType) || "short".equals(objType) || "int".equals(objType) || "long".equals(objType) || "double".equals(objType) || "float".equals(objType) || "boolean".equals(objType)) {
            return true;
        } else if ("java.lang.Byte".equals(objType) || "java.lang.Short".equals(objType) || "java.lang.Integer".equals(objType) || "java.lang.Long".equals(objType) || "java.lang.Double".equals(objType) || "java.lang.Float".equals(objType) || "java.lang.Boolean".equals(objType) || "java.lang.String".equals(objType) || "java.math.BigDecimal".equals(objType) || "java.util.Date".equals(objType)) {
            return true;
        } else {
            return false;
        }

    }



}
