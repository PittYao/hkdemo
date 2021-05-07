package com.cebon.jiwei.shengxunzhuji.hkservice;

import com.cebon.jiwei.shengxunzhuji.model.dto.request.DownLoadVideoDTO;
import com.sun.jna.NativeLong;

import java.time.LocalDateTime;

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
     * @param ip        审讯机ip
     * @param userName  审讯机用户名
     * @param password  审讯机密码
     * @param channel   通道号
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 登录号，如果登录号小于0，则登录失败
     */
    NativeLong downLoad(DownLoadVideoDTO downLoadVideoDTO, LocalDateTime startTime, LocalDateTime endTime);
}
