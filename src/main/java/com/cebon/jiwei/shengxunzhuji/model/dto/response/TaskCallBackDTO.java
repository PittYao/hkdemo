package com.cebon.jiwei.shengxunzhuji.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: bugProvider
 * @date: 2021/5/7 19:04
 * @description: 任务完成状态回调
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskCallBackDTO {
    /** 父任务id */
    private Integer taskId;
    /** 子任务id */
    private String subTaskId;
    /** 任务完成状态  -1=任务异常 0=任务开始 1=任务完成 */
    private Integer status;
}
