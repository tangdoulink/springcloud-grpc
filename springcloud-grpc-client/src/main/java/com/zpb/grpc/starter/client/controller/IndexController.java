package com.zpb.grpc.starter.client.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zpb.grpc.proto.*;
import net.devh.boot.grpc.client.config.GrpcChannelsProperties;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author       pengbo.zhao
 * @description  index
 * @createDate   2021/12/14 21:22
 * @updateDate   2021/12/14 21:22
 * @version      1.0
 */
@RestController
@RequestMapping("index")
public class IndexController {

    @Value("${grpc.client.cloud-grpc-service.negotiation-type}")
    private String negotiationType;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private GrpcChannelsProperties grpcChannelsProperties;

    @GrpcClient("cloud-grpc-service")
    private UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

    @GetMapping("type")
    public String getNegotiationType() throws JsonProcessingException {
        System.err.println(objectMapper.writeValueAsString(grpcChannelsProperties));
        return negotiationType;
    }

    @GetMapping("/get")
    public String get(@RequestParam("id") Integer id) {
        // 创建请求
        UserGetRequest request = UserGetRequest.newBuilder().setId(id).build();
        // 执行 gRPC 请求
        UserGetResponse response = userServiceBlockingStub.get(request);
        // 响应
        return response.getName();
    }

    @PostMapping("/create")
    public Integer create(@RequestParam("name") String name,
                          @RequestParam("gender") Integer gender) {
        // 创建请求
        UserCreateRequest request = UserCreateRequest.newBuilder()
                .setName(name).setGender(gender).build();

        // 执行 gRPC 请求
        UserCreateResponse response = userServiceBlockingStub.create(request);

        // 响应
        return response.getId();
    }
}
