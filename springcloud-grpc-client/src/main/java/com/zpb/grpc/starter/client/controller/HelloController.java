package com.zpb.grpc.starter.client.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.zpb.grpc.proto.HelloGrpc;
import com.zpb.grpc.proto.HelloRequest;
import com.zpb.grpc.proto.HelloResponse;
import com.zpb.grpc.starter.client.constant.Result;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @author       pengbo.zhao
 * @description  hello-controller
 * @createDate   2021/12/15 21:13
 * @updateDate   2021/12/15 21:13
 * @version      1.0
 */
@RestController
@RequestMapping("h")
public class HelloController {

    @Resource
    private ObjectMapper objectMapper;

    @GrpcClient("cloud-grpc-service")
    private HelloGrpc.HelloBlockingStub helloBlockingStub;

    // @PostMapping(value = "hello")
    // @PostMapping(value = "hello",produces = "application/x-protobuf;charset=utf-8")
    @PostMapping(value = "hello",produces = "application/json;charset=utf-8")
    // public Result<HelloResponse> getNegotiationType(@RequestParam("name") String name) throws JsonProcessingException, InvalidProtocolBufferException {
    public HelloResponse getNegotiationType(@RequestParam("name") String name) throws JsonProcessingException, InvalidProtocolBufferException {
        HelloResponse helloResponse = helloBlockingStub.hello(HelloRequest.newBuilder().setName(name).build());
        String print = JsonFormat.printer().print(helloResponse);
        // JsonFormat.parser().
        System.err.println(print);
        // return Result.succ(helloResponse);
        return helloResponse;
    }

}
