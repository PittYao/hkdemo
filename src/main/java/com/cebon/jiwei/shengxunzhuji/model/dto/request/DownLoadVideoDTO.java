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
    @NotBlank(message = "审讯机ip不能为空")
    String ip;
    @NotBlank(message = "用户名不能为空")
    String userName;
    @NotBlank(message = "密码不能为空")
    String password;
    @NotNull(message = "通道号不能为空")
    Integer channelNo;
    @NotBlank(message = "视频开始时间不能为空")
    String startTime;
    @NotBlank(message = "视频结束时间不能为空")
    String endTime;
}
