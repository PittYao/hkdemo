package com.cebon.jiwei.shengxunzhuji.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: bugProvider
 * @date: 2021/5/6 13:17
 * @description: 下载视频请求
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DownLoadVideoDTO {
    /** 刻录父任务id */
    @NotNull(message = "父任务id不能为空")
    private Integer taskId;
    /** 审讯机ip */
    @NotBlank(message = "审讯机ip不能为空")
    private String ip;
    /** 登录审讯机用户名 */
    @NotBlank(message = "登录审讯机用户名不能为空")
    private String userName;
    /** 登录审讯机密码 */
    @NotBlank(message = "登录审讯机密码不能为空")
    private String password;
    /** 审讯机通道号 */
    @NotNull(message = "通道号不能为空")
    private Integer channelNo;
    /** 视频开始时间 */
    @NotBlank(message = "视频开始时间不能为空")
    private String startTime;
    /** 视频结束时间 */
    @NotBlank(message = "视频结束时间不能为空")
    private String endTime;
}
