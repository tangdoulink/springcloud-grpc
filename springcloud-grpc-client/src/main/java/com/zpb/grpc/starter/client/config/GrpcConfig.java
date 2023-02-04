package com.zpb.grpc.starter.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;

/**
 * @author       pengbo.zhao
 * @description  grpc-config
 * @createDate   2021/12/16 15:09
 * @updateDate   2021/12/16 15:09
 * @version      1.0
 */
@Configuration
public class GrpcConfig {

    @Bean
    public ProtobufHttpMessageConverter protobufHttpMessageConverter() {
        return new ProtobufHttpMessageConverter();
    }

}
