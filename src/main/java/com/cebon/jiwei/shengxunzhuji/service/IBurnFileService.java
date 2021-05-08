package com.cebon.jiwei.shengxunzhuji.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cebon.jiwei.shengxunzhuji.model.dto.request.BurnFileDTO;
import com.cebon.jiwei.shengxunzhuji.model.po.BurnFile;

/**
 * @author: bugProvider
 * @date: 2021/5/6 14:19
 * @description: 刻录文件任务
 */
public interface IBurnFileService extends IService<BurnFile> {

    BurnFile burnFile(BurnFileDTO burnFileDTO) ;
}
