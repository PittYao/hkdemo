package com.cebon.jiwei.shengxunzhuji.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cebon.jiwei.shengxunzhuji.model.dto.request.BurnSettingDTO;
import com.cebon.jiwei.shengxunzhuji.model.po.BurnSetting;
import net.lingala.zip4j.exception.ZipException;

/**
 * @author: bugProvider
 * @date: 2021/5/6 14:19
 * @description: 刻录任务
 */
public interface IBurnSettingService extends IService<BurnSetting> {

    BurnSetting burnTask(BurnSettingDTO burnSettingDTO) throws ZipException;

}
