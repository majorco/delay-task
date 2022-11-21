package com.majorco.conf;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers.DateDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xxxiao
 **/
@Configuration
public class ClassInfoSerializer {

  @Bean
  public ObjectMapper classInfoObjectMapper() {
    final ObjectMapper om = new ObjectMapper();
    //指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
    om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    // jdk8 time 适配,依赖 jackson-datatype-jsr310
    final JavaTimeModule javaTimeModule = new JavaTimeModule();
    javaTimeModule.addDeserializer(Date.class, new DateDeserializer());
    javaTimeModule.addSerializer(Date.class, new DateSerializer());
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
    javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
    om.registerModule(javaTimeModule);
    /*
     * LaissezFaireSubTypeValidator 序列化时将对象全类名一起保存下来,用户反序列化
     * ObjectMapper.DefaultTyping.NON_FINAL 类必须是非final修饰的
     * JsonTypeInfo.As.PROPERTY 多态时作为一个额外的属性，跟POJO同级
     */
    om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
        ObjectMapper.DefaultTyping.NON_FINAL,
        JsonTypeInfo.As.PROPERTY);
    //null值不参与序列化
    om.setDefaultPropertyInclusion(Include.NON_NULL);
    return om;
  }
}
