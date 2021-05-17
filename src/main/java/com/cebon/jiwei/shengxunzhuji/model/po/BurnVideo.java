package com.cebon.jiwei.shengxunzhuji.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author: bugProvider
 * @date: 2021/5/6 14:28
 * @description: 刻录文件
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("burn_video")
public class BurnVideo {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 刻录设置id
     */
    @TableField(value = "burn_setting_id")
    private Integer burnSettingId;
    /**
     * 审讯机ip
     */
    @TableField(value = "ip")
    private String ip;
    /**
     * 审讯机登录用户名
     */
    @TableField(value = "user_name")
    private String userName;
    /**
     * 审讯机登录密码
     */
    @TableField(value = "password")
    private String password;
    /**
     * 下载视频的审讯机通道号
     */
    @TableField(value = "channelNo")
    private Integer channelNo;
    /**
     * 下载视频的开始时间
     */
    @TableField(value = "start_time")
    private String startTime;
    /**
     * 下载视频的结束时间
     */
    @TableField(value = "end_time")
    private String endTime;

    /**
     * 状态  -1=任务异常 0=任务开始 1=任务完成
     */
    @TableField(value = "download_status")
    private Integer downloadStatus;

    /**
     * 下载视频失败的原因
     */
    @TableField(value = "download_error_info")
    private String downloadErrorInfo;

    /**
     * 回调通知状态  -1=回调失败 1=回调成功
     */
    @TableField(value = "call_back_status")
    private Integer callBackStatus;

    /**
     * 发起回调的请求时间
     */
    @TableField(value = "call_back_time")
    private LocalDateTime callBackTime;

    /**
     * 发起回调失败的原因
     */
    @TableField(value = "call_back_error_info")
    private String callBackErrorInfo;

}
