package com.trun.fun.common.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.trun.fun.common.annotations.DesensitizationAnnotation;
import com.trun.fun.common.enums.DesensitizationTypeEnum;
import com.trun.fun.common.utils.DesensitizationUtils;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.Objects;

/**
 * json数据序列化
 * 此处扩展实现数据脱敏
 */
@JsonComponent
public class DesensitizationSerialize extends  JsonSerializer<String> implements ContextualSerializer {


    private DesensitizationTypeEnum type;

    public DesensitizationSerialize() {
    }

    public DesensitizationSerialize(final DesensitizationTypeEnum type) {
        this.type = type;
    }

    @Override
    public void serialize(final String s, final JsonGenerator jsonGenerator,
                          final SerializerProvider serializerProvider) throws IOException, JsonProcessingException {

        if(type!=null){
            switch (this.type) {
                case CHINESE_NAME: {
                    jsonGenerator.writeString(DesensitizationUtils.chineseName(s));
                    break;
                }
                case ID_CARD: {
                    jsonGenerator.writeString(DesensitizationUtils.idCardNum(s));
                    break;
                }
                case FIXED_PHONE: {
                    jsonGenerator.writeString(DesensitizationUtils.fixedPhone(s));
                    break;
                }
                case MOBILE_PHONE: {
                    jsonGenerator.writeString(DesensitizationUtils.mobilePhone(s));
                    break;
                }
                case ADDRESS: {
                    jsonGenerator.writeString(DesensitizationUtils.address(s, 4));
                    break;
                }
                case EMAIL: {
                    jsonGenerator.writeString(DesensitizationUtils.email(s));
                    break;
                }
                case BANK_CARD: {
                    jsonGenerator.writeString(DesensitizationUtils.bankCard(s));
                    break;
                }
                case CNAPS_CODE: {
                    jsonGenerator.writeString(DesensitizationUtils.cnapsCode(s));
                    break;
                }
                case IP_LIST: {
                    jsonGenerator.writeString(DesensitizationUtils.cnapsCode(s));
                    break;
                }
            }

        }else {
            jsonGenerator.writeString(s);
        }
    }


    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (property != null) {
            if (Objects.equals(property.getType().getRawClass(), String.class)) {
                DesensitizationAnnotation sensitiveInfo = property.getAnnotation(DesensitizationAnnotation.class);
                if (sensitiveInfo == null) {
                    sensitiveInfo = property.getContextAnnotation(DesensitizationAnnotation.class);
                }
                // 如果能得到注解，就将注解的 value 传入 SensitiveInfoSerialize
                if (sensitiveInfo != null) {

                    return new DesensitizationSerialize(sensitiveInfo.value());
                }
            }
            //return prov.findValueSerializer(property.getType(), property);
            return new DesensitizationSerialize();
        }

        return prov.findNullValueSerializer(property);
    }
}