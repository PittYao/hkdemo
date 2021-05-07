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
 * @description: 刻录任务
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("burn_setting")
public class BurnSetting {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 本次任务总数
     */
    @TableField(value = "task_num")
    private Integer taskNum;

    /**
     * 加密方式  0=不加密 1=密码加密 2=其他加密
     */
    @TableField(value = "encryption_type")
    private Integer encryptionType;

    /**
     * 加密密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 下载文件存储路径
     */
    @TableField(value = "oda_save_path")
    private String odaSavePath;

    /**
     * 本次任务完成数
     */
    @TableField(value = "done_task_num")
    private Integer doneTaskNum;

    /**
     * 本次任务异常数
     */
    @TableField(value = "error_task_num")
    private Integer errorTaskNum;

    /**
     * 回到通知地址
     */
    @TableField(value = "call_back_url")
    private String callBackUrl;
}
