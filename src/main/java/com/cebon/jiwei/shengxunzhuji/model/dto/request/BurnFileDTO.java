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
 * @description: 刻录文件任务
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BurnFileDTO {
    @NotNull(message = "父任务id不能为空")
    private Integer taskId;
    @NotBlank(message = "文件地址不能为空")
    private String fileUrl;
}
