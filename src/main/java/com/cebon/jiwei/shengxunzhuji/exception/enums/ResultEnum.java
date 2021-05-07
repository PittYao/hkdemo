package com.cebon.jiwei.shengxunzhuji.exception.enums;

import lombok.Getter;

@Getter
public enum ResultEnum implements ExtensibleEnum {

    SUCCESS(200, "成功"),
    ERROR(500, "服务异常"),
    ERROR_Customize(1000, "自定义异常消息"),
    PARAM_VERIFICATION_FAILED(10001, "参数校验异常"),

    GET_SUCCESS(200, "查询成功"),
    POST_SUCCESS(200, "添加成功"),
    PUT_SUCCESS(200, "修改成功"),
    DELETE_SUCCESS(200, "删除成功"),


    ZIP_ERROR(500, "解码zip异常"),
    HTTP_ERROR(500, "访问远程链接异常");

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
