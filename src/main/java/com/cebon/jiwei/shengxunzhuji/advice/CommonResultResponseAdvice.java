package com.cebon.jiwei.shengxunzhuji.advice;

import com.cebon.jiwei.shengxunzhuji.exception.enums.ResultEnum;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author: bugProvider
 * @date: 2019/10/9 09:45
 * @description: json统一格式响应
 */
@RestControllerAdvice()
public class CommonResultResponseAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (body instanceof CommonResult) {
            return body;
        }

        // 确定请求方法的要返回的默认消息
        HttpMethod method = serverHttpRequest.getMethod();
        ResultEnum resultEnum = ResultEnum.SUCCESS;
        if (method != null) {
            switch (method) {
                case GET:
                    resultEnum = ResultEnum.GET_SUCCESS;
                    break;
                case POST:
                    resultEnum = ResultEnum.POST_SUCCESS;
                    break;
                case PUT:
                    resultEnum = ResultEnum.PUT_SUCCESS;
                    break;
                case DELETE:
                    resultEnum = ResultEnum.DELETE_SUCCESS;
                    break;
                default:
                    break;
            }
        }

        return new CommonResult<>(resultEnum, body);
    }
}
