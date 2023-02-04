package com.zpb.grpc.starter.client.constant;


import java.io.Serializable;

/**
 * @author       pengbo.zhao
 * @description  返回结果
 * @createDate   2021/1/7 5:10 下午
 * @updateDate   2021/1/7 5:10 下午
 * @version      1.0
 */

public class Result<T> implements Serializable {

    private static final long serialVersionUID = -919381528300072930L;

    /**
     * 状态码
     */
    public String code;

    /**
     * 返回信息
     */
    private String msg;

    /**
     * 返回结果
     */
    private T data;

    public Result(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Result<T> succ() {
        return succ(null);
    }

    public static <T> Result<T> succ(T data) {
        return succ("200", "ok", data);
    }

    public static <T> Result<T> succ(String code, String msg) {
        return succ(code, msg, null);
    }

    public static <T> Result<T> succ(String code, String msg, T data) {
        return new Result<>(code, msg, data);
    }

    public static <T> Result<T> fail() {
        return fail(null);
    }

    public static <T> Result<T> fail(T data) {
        return fail("500", "fail", data);
    }

    public static <T> Result<T> fail(String code, String msg) {
        return fail(code, msg, null);
    }

    public static <T> Result<T> fail(String code, String msg, T data) {
        return new Result<>(code, msg, data);
    }

}
