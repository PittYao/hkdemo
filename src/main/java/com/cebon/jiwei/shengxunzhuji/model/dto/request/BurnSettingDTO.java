package com.cebon.jiwei.shengxunzhuji.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: bugProvider
 * @date: 2021/5/6 14:15
 * @description: 刻录任务设置
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BurnSettingDTO {
    @NotNull(message = "刻录任务数不能为空")
    private Integer taskNum;
    /**
     * 0=不加密 1=密码加密 2=其他加密
     */
    @NotNull(message = "加密方式不能为空")
    private Integer encryptionType;
    /**
     * 加密密码
     */
    private String password;
    @NotBlank(message = "刻录文件存放路径不能为空")
    private String odaSavePath;
    @NotBlank(message = "回调通知地址不能为空")
    private String callBackUrl;
    /**
     * 完成任务数
     */
    private Integer DoneTaskNum;
}
