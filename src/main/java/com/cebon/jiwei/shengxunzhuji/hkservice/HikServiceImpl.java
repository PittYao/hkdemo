package com.cebon.jiwei.shengxunzhuji.hkservice;

import com.cebon.jiwei.shengxunzhuji.model.dto.request.DownLoadVideoDTO;
import com.cebon.jiwei.shengxunzhuji.exception.BusinessException;
import com.sun.jna.NativeLong;
import com.sun.jna.ptr.IntByReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: yty
 * @date: 2020/12/16 11:03
 * @description:
 */
@Slf4j
@Service
public class HikServiceImpl implements IHkService {
    @Value(value = "${hikang.enable:false}")
    Boolean enableHikSdk;

    /**
     * HCNetSDK的全局实例
     */
    private HCNetSDK hCNetSDK;

    /**
     * 设备信息
     */
    private HCNetSDK.NET_DVR_DEVICEINFO_V30 deviceInfo;

    @Value("${download.tempPath}")
    String downloadPath;

    @PostConstruct
    void initSDK() {
        if (enableHikSdk) {
            hCNetSDK = HCNetSDK.INSTANCE;
            deviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
            hCNetSDK.NET_DVR_Init();
        }
    }

    @Override
    public NativeLong login30(String ip, String username, String password) {
        NativeLong loginUserId;
        short port = 8000;

        //调用登录的方法
        loginUserId = hCNetSDK.NET_DVR_Login_V30(ip, port, username, password, deviceInfo);

        //对于同步登录，接口返回-1表示登录失败
        if (loginUserId.longValue() == -1) {
            log.info("登录失败，错误码为" + hCNetSDK.NET_DVR_GetLastError());
            //接口返回其他值代表用户ID值。用户ID具有唯一性，后续对设备的操作都需要通过此ID实现
        } else {
            log.info("登录成功！登录id：" + loginUserId.longValue());
        }

        //返回用户ID值，或者失败返回-1
        return loginUserId;
    }

    private HCNetSDK.NET_DVR_TIME getDVRTime(LocalDateTime dateTime) {
        HCNetSDK.NET_DVR_TIME NDTStartTime = new HCNetSDK.NET_DVR_TIME();
        NDTStartTime.dwYear = dateTime.getYear();
        NDTStartTime.dwMonth = dateTime.getMonthValue();
        NDTStartTime.dwDay = dateTime.getDayOfMonth();
        NDTStartTime.dwHour = dateTime.getHour();
        NDTStartTime.dwMinute = dateTime.getMinute();
        NDTStartTime.dwSecond = dateTime.getSecond();
        return NDTStartTime;
    }

    @Override
    public NativeLong downLoad(DownLoadVideoDTO downLoadVideoDTO, LocalDateTime startTime, LocalDateTime endTime) {
        String ip = downLoadVideoDTO.getIp();
        String userName = downLoadVideoDTO.getUserName();
        String password = downLoadVideoDTO.getPassword();
        Integer channelNo = downLoadVideoDTO.getChannelNo();

        NativeLong uid = login30(ip, userName, password);
        if (uid.longValue() < 0) {
            throw new BusinessException(500, "设备登录失败");
        }

        HCNetSDK.NET_DVR_TIME NDTStartTime = getDVRTime(startTime);
        HCNetSDK.NET_DVR_TIME NDTEndTime = getDVRTime(endTime);
        HCNetSDK.NET_DVR_PLAYCOND playCond = new HCNetSDK.NET_DVR_PLAYCOND();
        playCond.struStartTime = NDTStartTime;
        playCond.struStopTime = NDTEndTime;
        playCond.dwChannel = channelNo;
        playCond.byDrawFrame = 0;
        playCond.byStreamType = 0;
        playCond.byStreamID = new byte[HCNetSDK.STREAM_ID_LEN];
        playCond.byCourseFile = 0;
        playCond.byRes = new byte[29];

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        String downLoadDir = downloadPath + "\\" + ip + "_ch" + channelNo + "_"
                + startTime.format(formatter) + "_" + endTime.format(formatter) + ".mp4";

        NativeLong downLoadHandle = hCNetSDK.NET_DVR_GetFileByTime_V40(uid, downLoadDir, playCond);
        if (downLoadHandle.intValue() >= 0) {
            hCNetSDK.NET_DVR_PlayBackControl(downLoadHandle, HCNetSDK.NET_DVR_PLAYSTART, 0, null);

            ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1,
                    new BasicThreadFactory.Builder().namingPattern("scheduled_download_video_hik-pool-%d").daemon(true).build());

            scheduledExecutorService.scheduleAtFixedRate(() -> {
                IntByReference nPos = new IntByReference(0);

                hCNetSDK.NET_DVR_PlayBackControl(downLoadHandle, HCNetSDK.NET_DVR_PLAYGETPOS, 0, nPos);
                log.info("海康回放返回结果:{}", nPos.getValue());

                if (nPos.getValue() < 0) {
                    hCNetSDK.NET_DVR_StopGetFile(downLoadHandle);
                    scheduledExecutorService.shutdown();
                    log.info("下载失败,具体原因:{}", hCNetSDK.NET_DVR_GetLastError());
                }
                if (nPos.getValue() > 100) {
                    hCNetSDK.NET_DVR_StopGetFile(downLoadHandle);
                    scheduledExecutorService.shutdown();
                    log.info("由于网络原因或DVR忙,下载异常终止!");
                }
                if (nPos.getValue() == 100) {
                    hCNetSDK.NET_DVR_StopGetFile(downLoadHandle);
                    scheduledExecutorService.shutdown();
                    log.info("按时间下载结束!");
                }
            }, 1, 5, TimeUnit.SECONDS);

            return downLoadHandle;
        } else {
            throw new BusinessException(500, "房间内无审讯一体机或缺少该通道号");
        }
    }
}
