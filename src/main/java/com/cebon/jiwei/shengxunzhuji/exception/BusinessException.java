package com.cebon.jiwei.shengxunzhuji.exception;

import com.cebon.jiwei.shengxunzhuji.exception.enums.ExtensibleEnum;
import lombok.Getter;

/**
 * @author: bugProvider
 * @date: 2019/10/3 21:19
 * @description: 基础异常
 */
@Getter
public class BusinessException extends RuntimeException {
    private Integer code;

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ExtensibleEnum extensibleEnum) {
        super(extensibleEnum.getMessage());
        this.code = extensibleEnum.getCode();
    }

}
