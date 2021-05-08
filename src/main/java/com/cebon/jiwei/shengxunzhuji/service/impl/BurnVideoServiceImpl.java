package com.cebon.jiwei.shengxunzhuji.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cebon.jiwei.shengxunzhuji.exception.BusinessException;
import com.cebon.jiwei.shengxunzhuji.mapper.BurnVideoMapper;
import com.cebon.jiwei.shengxunzhuji.model.dto.request.DownLoadVideoDTO;
import com.cebon.jiwei.shengxunzhuji.model.po.BurnSetting;
import com.cebon.jiwei.shengxunzhuji.model.po.BurnVideo;
import com.cebon.jiwei.shengxunzhuji.service.IBurnSettingService;
import com.cebon.jiwei.shengxunzhuji.service.IBurnVideoService;
import com.cebon.jiwei.shengxunzhuji.service.hkservice.IHkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: bugProvider
 * @date: 2021/5/8 13:51
 * @description: 刻录视频
 */
@Slf4j
@Service
public class BurnVideoServiceImpl extends ServiceImpl<BurnVideoMapper, BurnVideo> implements IBurnVideoService {
    @Autowired
    private IBurnSettingService burnSettingService;
    @Autowired
    private IHkService hkService;

    @Override
    public BurnVideo burnVideo(DownLoadVideoDTO downLoadVideoDTO) {
        // 1.查询父任务是否存在
        Integer taskId = downLoadVideoDTO.getTaskId();
        BurnSetting burnSetting = burnSettingService.getById(taskId);

        if (burnSetting == null) {
            throw new BusinessException(500, "父刻录任务不存在");
        }

        // 2.下载视频
        return hkService.downLoad(downLoadVideoDTO, burnSetting);

    }
}
