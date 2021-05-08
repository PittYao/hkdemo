package com.cebon.jiwei.shengxunzhuji.service.hkservice;

import com.cebon.jiwei.shengxunzhuji.model.dto.request.DownLoadVideoDTO;
import com.cebon.jiwei.shengxunzhuji.model.po.BurnSetting;
import com.cebon.jiwei.shengxunzhuji.model.po.BurnVideo;
import com.sun.jna.NativeLong;

/**
 * @author: bugProvider
 * @date: 2021/5/6 10:50
 * @description: 海康接口
 */
public interface IHkService {
    /**
     * 设备登录的方法
     *
     * @param ip       IP
     * @param username 用户名
     * @param password 密码
     * @return 登录号，如果登录号小于0，则登录失败
     */
    NativeLong login30(String ip, String username, String password);

    /**
     * 审讯一体机下载视频
     *
     * @param downLoadVideoDTO
     * @param burnSetting
     */
    BurnVideo downLoad(DownLoadVideoDTO downLoadVideoDTO, BurnSetting burnSetting);
}
