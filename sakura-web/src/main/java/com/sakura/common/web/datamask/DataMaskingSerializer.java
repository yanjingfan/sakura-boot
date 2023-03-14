package com.sakura.common.web.datamask;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.util.Objects;

/**
 * @auther yangfan
 * @date 2023/3/13
 * @describle 来源：https://blog.csdn.net/sco5282/article/details/126642636
 * 序列化注解自定义实现
 * JsonSerializer<String>：指定String 类型，serialize()方法用于将修改后的数据载入
 */
public final class DataMaskingSerializer extends JsonSerializer<String> implements ContextualSerializer {
    private DataMaskEnum dataMaskEnum;

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(dataMaskEnum.function().apply(value));
    }

    /**
     * 获取属性上的注解属性
     */
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        DataMask annotation = property.getAnnotation(DataMask.class);
        if (Objects.nonNull(annotation)&&Objects.equals(String.class, property.getType().getRawClass())) {
            this.dataMaskEnum = annotation.function();
            return this;
        }
        return prov.findValueSerializer(property.getType(), property);
    }
}
