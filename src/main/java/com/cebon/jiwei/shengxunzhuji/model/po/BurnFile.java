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
@TableName("burn_file")
public class BurnFile {
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
     * 下载文件地址
     */
    @TableField(value = "file_url")
    private String fileUrl;

    /**
     * 状态  -1=任务异常 0=任务开始 1=任务完成
     */
    @TableField(value = "download_status")
    private Integer downloadStatus;

    /**
     * 下载文件失败的原因
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
