package com.cebon.jiwei.shengxunzhuji.model.enums;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum EncryptionType {
    NOT("不加密", 0),
    PASSWORD("密码加密", 1),
    OTHER("其他加密", 2);
    
    private String name;
    private Integer type;

}
