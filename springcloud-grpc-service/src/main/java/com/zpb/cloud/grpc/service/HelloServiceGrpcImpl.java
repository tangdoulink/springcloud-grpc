package com.zpb.cloud.grpc.service;

import com.zpb.grpc.proto.HelloGrpc;
import com.zpb.grpc.proto.HelloRequest;
import com.zpb.grpc.proto.HelloResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author       pengbo.zhao
 * @description  hello-service
 * @createDate   2021/12/15 19:59
 * @updateDate   2021/12/15 19:59
 * @version      1.0
 */
@GrpcService
public class HelloServiceGrpcImpl extends HelloGrpc.HelloImplBase {

    private Map<String,String> helloMap;

    private final ConcurrentMap<String, List<HelloRequest>> helloRequestMap = new ConcurrentHashMap<>();

    public static final String DEFAULT = "default";

    @PostConstruct
    public void initHelloList(){
        helloMap = new HashMap<>();
        helloMap.put("zhangsan","张三：你好；欢迎您的到来，今天是"+ new Date() +" ，非常开心，您能够在百忙之中来到这里，让我们非常的荣幸");
        helloMap.put("lisi","李四：你好；欢迎您的到来，今天是"+ new Date() +" ，非常开心，您能够在百忙之中来到这里，让我们非常的荣幸");
        helloMap.put("wangwu","王五：你好；欢迎您的到来，今天是"+ new Date() +" ，非常开心，您能够在百忙之中来到这里，让我们非常的荣幸");
        helloMap.put("default","你好；您打招呼的人不存在，请下次再来");
    }

    @Override
    public void hello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        responseObserver.onNext(checkHelloResponse(request));
        responseObserver.onCompleted();
    }

    private HelloResponse checkHelloResponse(HelloRequest helloRequest){
        String name = helloRequest.getName();
        if(helloMap.containsKey(name)){
            return HelloResponse.newBuilder().setMessage(helloMap.get(name)).build();
        }
        return HelloResponse.newBuilder().setMessage(helloMap.get(DEFAULT)).build();
    }

    @Override
    public void manyHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        helloMap.keySet().forEach(key -> {
            if(key.length()>= 7 ){
                responseObserver.onNext(HelloResponse.newBuilder().setMessage(helloMap.get(key)).build());
            }
        });
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<HelloRequest> sayHello(StreamObserver<HelloResponse> responseObserver) {
        StreamObserver<HelloRequest> streamObserver = new StreamObserver<HelloRequest>() {
            int counter;
            int featureCount;
            HelloResponse hs;
            final long startTime = System.nanoTime();
            @Override
            public void onNext(HelloRequest helloRequest) {
                counter++;
                if(helloMap.containsKey(helloRequest.getName())){
                    featureCount ++ ;
                    hs = HelloResponse.newBuilder().setMessage(helloMap.get(helloRequest.getName())).build();
                }

            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println("处理 sayHello 出现异常 " + throwable);
            }

            @Override
            public void onCompleted() {
                long seconds = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - startTime);
                responseObserver.onNext(hs);
                responseObserver.onCompleted();
                System.err.println("处理数据耗时：" + seconds);
            }
        };
        return streamObserver;
    }

    @Override
    public StreamObserver<HelloRequest> chatHello(StreamObserver<HelloRequest> responseObserver) {

       return new StreamObserver<HelloRequest>(){
            @Override
            public void onNext(HelloRequest helloRequest) {
                List<HelloRequest> helloRequestList = getHelloRequests(helloRequest);

                for(HelloRequest hr : helloRequestList.toArray(new HelloRequest[0])){
                    responseObserver.onNext(hr);
                }
                helloRequestList.add(helloRequest);
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println("处理 sayHello 出现异常 " + throwable);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };

    }

    private List<HelloRequest> getHelloRequests(HelloRequest helloRequest) {

        List<HelloRequest> list1 = Collections.synchronizedList(new ArrayList<>());
        list1.add(HelloRequest.newBuilder().setName("maliu").build());
        list1.add(HelloRequest.newBuilder().setName("tianqi").build());

        List<HelloRequest> list2 = helloRequestMap.putIfAbsent("query",list1);
        return list2 != null ? list2 : list1;
    }
}
