package com.cebon.jiwei.shengxunzhuji.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cebon.jiwei.shengxunzhuji.model.dto.request.DownLoadVideoDTO;
import com.cebon.jiwei.shengxunzhuji.model.po.BurnVideo;

/**
 * @author: bugProvider
 * @date: 2021/5/6 14:19
 * @description: 刻录视频任务
 */
public interface IBurnVideoService extends IService<BurnVideo> {

    BurnVideo burnVideo(DownLoadVideoDTO downLoadVideoDTO) ;
}
