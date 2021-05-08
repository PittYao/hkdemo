package com.cebon.jiwei.shengxunzhuji.service.hkservice;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.cebon.jiwei.shengxunzhuji.exception.BusinessException;
import com.cebon.jiwei.shengxunzhuji.model.dto.request.DownLoadVideoDTO;
import com.cebon.jiwei.shengxunzhuji.model.dto.response.TaskCallBackDTO;
import com.cebon.jiwei.shengxunzhuji.model.enums.TaskType;
import com.cebon.jiwei.shengxunzhuji.model.po.BurnSetting;
import com.cebon.jiwei.shengxunzhuji.model.po.BurnVideo;
import com.cebon.jiwei.shengxunzhuji.service.IBurnSettingService;
import com.cebon.jiwei.shengxunzhuji.service.IBurnVideoService;
import com.sun.jna.NativeLong;
import com.sun.jna.ptr.IntByReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private IBurnVideoService burnVideoService;

    @Autowired
    private IBurnSettingService burnSettingService;

    /**
     * HCNetSDK的全局实例
     */
    private HCNetSDK hCNetSDK;

    /**
     * 设备信息
     */
    private HCNetSDK.NET_DVR_DEVICEINFO_V30 deviceInfo;

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

    private HCNetSDK.NET_DVR_PLAYCOND getNet_dvr_playCond(Integer channelNo, HCNetSDK.NET_DVR_TIME NDTStartTime, HCNetSDK.NET_DVR_TIME NDTEndTime) {
        HCNetSDK.NET_DVR_PLAYCOND playCond = new HCNetSDK.NET_DVR_PLAYCOND();
        playCond.struStartTime = NDTStartTime;
        playCond.struStopTime = NDTEndTime;
        playCond.dwChannel = channelNo;
        playCond.byDrawFrame = 0;
        playCond.byStreamType = 0;
        playCond.byStreamID = new byte[HCNetSDK.STREAM_ID_LEN];
        playCond.byCourseFile = 0;
        playCond.byRes = new byte[29];
        return playCond;
    }

    /**
     * 构建下载视频的文件名称格式
     *
     * @param startTime   下载视频开始时间
     * @param endTime     下载视频结束时间
     * @param ip          审讯机ip
     * @param odaSavePath 下载文件存放路径
     * @param channelNo   审讯机通道号
     * @return 文件名称
     */
    private String downloadFileNameBuilder(LocalDateTime startTime, LocalDateTime endTime, String ip, String odaSavePath, Integer channelNo) {
        DateTimeFormatter formatter;
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        return odaSavePath + "\\" + ip + "_ch" + channelNo + "_"
                + startTime.format(formatter) + "_" + endTime.format(formatter) + ".mp4";
    }

    /**
     * 查询下载视频进度
     *
     * @param downLoadHandle 句柄
     * @param burnVideo
     * @param burnSetting
     */
    private void queryDownloadProcessAndCallback(NativeLong downLoadHandle, BurnVideo burnVideo, BurnSetting burnSetting) {
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("scheduled_download_video_hik-pool-%d").daemon(true).build());

        // 1.另起线程每5秒查询一次下载进度
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            IntByReference nPos = new IntByReference(0);

            hCNetSDK.NET_DVR_PlayBackControl(downLoadHandle, HCNetSDK.NET_DVR_PLAYGETPOS, 0, nPos);
            log.info("海康返回下载进度:{}%", nPos.getValue());

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
                log.info("下载视频成功结束!");
                // 下载视频成功，并回调通知任务完成状态
                successDownloadAndCallback(burnVideo, burnSetting);

            }
        }, 1, 5, TimeUnit.SECONDS);
    }

    /**
     * 下载视频成功，并回调通知任务完成状态
     *
     * @param burnVideo   视频子任务
     * @param burnSetting 刻录父任务
     */
    private void successDownloadAndCallback(BurnVideo burnVideo, BurnSetting burnSetting) {
        // 1.2 查询最新的刻录父任务
        BurnSetting newBurnSetting = burnSettingService.getById(burnSetting.getId());

        // 1.3 修改任务状态为完成状态
        burnVideo.setDownloadStatus(TaskType.SUCCESS.getType());

        // 1.4 修改父任务完成任务数+1
        Integer doneTaskNum = newBurnSetting.getDoneTaskNum();
        newBurnSetting.setDoneTaskNum(doneTaskNum == null ? 1 : ++doneTaskNum);
        burnSettingService.updateById(newBurnSetting);

        // 1.5 回调通知任务状态

        // 1.5.1 构建回调参数
        String callBackUrl = newBurnSetting.getCallBackUrl();
        TaskCallBackDTO callBackDTO = TaskCallBackDTO.builder()
                .taskId(newBurnSetting.getId())
                .subTaskId(burnVideo.getId())
                .status(burnVideo.getDownloadStatus())
                .build();

        // 设置回调时间
        burnVideo.setCallBackTime(LocalDateTime.now());

        // 1.5.2 开始回调
        try {
            log.info("下载视频成功，开始回调通知状态，回调地址:{}，回调参数:{}", callBackUrl, callBackDTO);
            String result = HttpRequest.post(callBackUrl)
                    .body(JSON.toJSONString(callBackDTO))
                    // 超时连接5秒
                    .timeout(5000)
                    .execute().body();
            log.info("回调视频通知状态的响应结果:{}", result);
            // 1.5.3 修改子任务回调状态为成功：1
            burnVideo.setCallBackStatus(TaskType.SUCCESS.getType());
        } catch (Exception e) {
            log.error("下载视频回调失败，原因:", e);
            // 1.5.4 修改子任务回调状态为失败：-1
            burnVideo.setCallBackStatus(TaskType.ERROR.getType());
            // 记录失败的原因
            burnVideo.setCallBackErrorInfo(e.getMessage());
        } finally {
            burnVideoService.updateById(burnVideo);
        }
    }

    @Override
    public BurnVideo downLoad(DownLoadVideoDTO downLoadVideoDTO, BurnSetting burnSetting) {

        // 1.将请求中的开始时间提前2秒，结束时间延迟2秒
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String startTimeStr = downLoadVideoDTO.getStartTime();
        String endTimeStr = downLoadVideoDTO.getEndTime();

        LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter).minusSeconds(2);
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter).plusSeconds(2);

        String ip = downLoadVideoDTO.getIp();
        String userName = downLoadVideoDTO.getUserName();
        String password = downLoadVideoDTO.getPassword();
        String odaSavePath = burnSetting.getOdaSavePath();
        Integer channelNo = downLoadVideoDTO.getChannelNo();

        // 2.登录审讯机
        NativeLong uid = login30(ip, userName, password);
        if (uid.longValue() < 0) {
            throw new BusinessException(500, "设备登录失败");
        }

        // 3.构建调用海康sdk的请求参数
        HCNetSDK.NET_DVR_TIME NDTStartTime = getDVRTime(startTime);
        HCNetSDK.NET_DVR_TIME NDTEndTime = getDVRTime(endTime);
        HCNetSDK.NET_DVR_PLAYCOND playCond = getNet_dvr_playCond(channelNo, NDTStartTime, NDTEndTime);

        // 4.构建下载视频的文件名称格式
        String downLoadDir = downloadFileNameBuilder(startTime, endTime, ip, odaSavePath, channelNo);

        // 5.查询这段时间文件是否存在
        NativeLong downLoadHandle = hCNetSDK.NET_DVR_GetFileByTime_V40(uid, downLoadDir, playCond);
        if (downLoadHandle.intValue() >= 0) {
            // 6.存储下载视频任务
            BurnVideo burnVideo = BurnVideo.builder()
                    .createTime(LocalDateTime.now())
                    .burnSettingId(burnSetting.getId())
                    .ip(ip)
                    .userName(userName)
                    .password(password)
                    .channelNo(channelNo)
                    .startTime(startTimeStr)
                    .endTime(endTimeStr)
                    .downloadStatus(TaskType.BEGIN.getType())
                    .build();

            burnVideoService.save(burnVideo);

            // 6.开始下载
            hCNetSDK.NET_DVR_PlayBackControl(downLoadHandle, HCNetSDK.NET_DVR_PLAYSTART, 0, null);

            // 7.查询下载视频进度，并完成任务状态回调
            queryDownloadProcessAndCallback(downLoadHandle, burnVideo, burnSetting);

            return burnVideo;
        } else {
            throw new BusinessException(500, "房间内无审讯一体机或缺少该通道号");
        }
    }


}
