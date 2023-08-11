package com.mju.service.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JsonHttpMessageConverterConfig {

    @Bean
    @Primary//提升优先级
    public HttpMessageConverters fastJsonHttpMessageConverters(){
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setDateFormat("yyyy-MM-dd HH:mm:ss");
        config.setSerializerFeatures(
                SerializerFeature.WriteNullListAsEmpty,//判断这个集合是不是为空，如果为空返回空字符串
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.PrettyFormat,//数据格式化
                SerializerFeature.MapSortField,//进行排序
                SerializerFeature.DisableCircularReferenceDetect   //关闭循环引用
        );
        converter.setFastJsonConfig(config);
        return new HttpMessageConverters(converter);
    }
}
