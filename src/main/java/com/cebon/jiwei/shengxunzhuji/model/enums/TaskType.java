package com.cebon.jiwei.shengxunzhuji.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum TaskType {
    ERROR("异常", -1),
    BEGIN("任务开始", 0),
    SUCCESS("任务完成", 1);

    private String name;
    private Integer type;

}
