package com.trun.fun.common.pression;

import org.springframework.expression.ParseException;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.LiteralExpression;

import java.util.ArrayList;
import java.util.List;

/**
 * 文本解析器
 * @author mawei
 */
public class ContentTextParser {
    private  String expressionString;

    private ParserContext context;


    public  Context[] getContens() throws ParseException {
        List<Context> expressions = new ArrayList<>();
        String prefix = context.getExpressionPrefix();
        String suffix = context.getExpressionSuffix();
        int startIdx = 0;

        while (startIdx < expressionString.length()) {
            int prefixIndex = expressionString.indexOf(prefix, startIdx);
            if (prefixIndex >= startIdx) {
                // an inner expression was found - this is a composite
                if (prefixIndex > startIdx) {
                    Context exp=new Context(new LiteralExpression(expressionString.substring(startIdx, prefixIndex)).getValue(),false);
                    expressions.add(exp);
                }
                int afterPrefixIndex = prefixIndex + prefix.length();
                int suffixIndex = LogTextParsingUtil.skipToCorrectEndSuffix(suffix, expressionString, afterPrefixIndex);
                if (suffixIndex == -1) {
                    throw new ParseException(expressionString, prefixIndex,
                            "No ending suffix '" + suffix + "' for expression starting at character " +
                                    prefixIndex + ": " + expressionString.substring(prefixIndex));
                }
                if (suffixIndex == afterPrefixIndex) {
                    throw new ParseException(expressionString, prefixIndex,
                            "No expression defined within delimiter '" + prefix + suffix +
                                    "' at character " + prefixIndex);
                }
                String expr = expressionString.substring(prefixIndex + prefix.length(), suffixIndex);
                expr = expr.trim();
                if (expr.isEmpty()) {
                    throw new ParseException(expressionString, prefixIndex,
                            "No expression defined within delimiter '" + prefix + suffix +
                                    "' at character " + prefixIndex);
                }
                Context exp=new Context(expr,true);
                expressions.add(exp);
                startIdx = suffixIndex + suffix.length();
            }
            else {
                Context exp=new Context(new LiteralExpression(expressionString.substring(startIdx)).getValue(),false);
                expressions.add(exp);
                startIdx = expressionString.length();
            }
        }

        return expressions.toArray(new Context[0]);
    }

    public ContentTextParser(String templateStr, ParserContext context) {
        this.expressionString = templateStr;
        this.context = context;
    }
}
