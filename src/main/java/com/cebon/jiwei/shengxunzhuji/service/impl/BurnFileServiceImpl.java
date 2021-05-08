package com.cebon.jiwei.shengxunzhuji.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cebon.jiwei.shengxunzhuji.exception.BusinessException;
import com.cebon.jiwei.shengxunzhuji.mapper.BurnFileMapper;
import com.cebon.jiwei.shengxunzhuji.model.dto.request.BurnFileDTO;
import com.cebon.jiwei.shengxunzhuji.model.dto.response.TaskCallBackDTO;
import com.cebon.jiwei.shengxunzhuji.model.enums.TaskType;
import com.cebon.jiwei.shengxunzhuji.model.po.BurnFile;
import com.cebon.jiwei.shengxunzhuji.model.po.BurnSetting;
import com.cebon.jiwei.shengxunzhuji.service.IBurnFileService;
import com.cebon.jiwei.shengxunzhuji.service.IBurnSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author: bugProvider
 * @date: 2021/5/6 14:19
 * @description: 刻录文件任务
 */
@Slf4j
@Service
public class BurnFileServiceImpl extends ServiceImpl<BurnFileMapper, BurnFile> implements IBurnFileService {

    @Autowired
    private IBurnSettingService burnSettingService;

    @Override
    public BurnFile burnFile(BurnFileDTO burnFileDTO) {
        // 1.查询父任务是否存在
        Integer taskId = burnFileDTO.getTaskId();
        BurnSetting burnSetting = burnSettingService.getById(taskId);
        String fileUrl = burnFileDTO.getFileUrl();

        if (burnSetting == null) {
            throw new BusinessException(500, "父刻录任务不存在");
        }

        // 2.准备添加刻录文件入库
        BurnFile burnFile = BurnFile.builder()
                .createTime(LocalDateTime.now())
                .burnSettingId(taskId)
                .fileUrl(fileUrl)
                // 任务开始状态
                .downloadStatus(TaskType.BEGIN.getType())
                .build();

        // 2.下载远程文件到oda文件夹下
        String odaSavePath = burnSetting.getOdaSavePath();
        log.info("下载文件到oda文件夹:{}", odaSavePath);

        // 3.下载文件,TODO 如果文件较大，考虑另起线程下载
        long size;
        try {
            size = HttpUtil.downloadFile(fileUrl, FileUtil.file(odaSavePath));
            log.info("下载文件完成,文件大小:{} ", size);

            // 4.1.0 查询最新的刻录父任务
            burnSetting = burnSettingService.getById(burnSetting.getId());
            // 4.1.1添加刻录文件入库,任务完成状态
            burnFile.setDownloadStatus(TaskType.SUCCESS.getType());
            // 4.1.2修改父级刻录任务的子任务完成数量+1
            Integer doneTaskNum = burnSetting.getDoneTaskNum();
            burnSetting.setDoneTaskNum(++doneTaskNum);

        } catch (Exception e) {
            log.error("下载远程文件异常:", e);

            // 4.2.0 查询最新的刻录父任务
            burnSetting = burnSettingService.getById(burnSetting.getId());
            // 4.2.1添加刻录文件入库,任务异常状态,记录失败原因
            burnFile.setDownloadStatus(TaskType.ERROR.getType());
            burnFile.setDownloadErrorInfo(e.getMessage());
            // 4.2.2修改父级刻录任务的子任务完成数量+1
            Integer errorTaskNum = burnSetting.getErrorTaskNum();
            burnSetting.setErrorTaskNum(errorTaskNum == null ? 1 : ++errorTaskNum);
        }

        // 4.3 修改父任务
        burnSettingService.updateById(burnSetting);

        // 5.新增刻录文件子任务
        this.save(burnFile);

        // 6.回调通知任务完成状态
        String callBackUrl = burnSetting.getCallBackUrl();
        TaskCallBackDTO callBackDTO = TaskCallBackDTO.builder()
                .taskId(burnSetting.getId())
                .subTaskId(burnFile.getId())
                .status(burnFile.getDownloadStatus())
                .build();

        // 设置回调时间
        burnFile.setCallBackTime(LocalDateTime.now());

        try {
            log.info("下载文件成功，开始回调通知状态，回调地址:{}，回调参数:{}", callBackUrl, callBackDTO);
            String result = HttpRequest.post(callBackUrl)
                    .body(JSON.toJSONString(callBackDTO))
                    // 超时连接5秒
                    .timeout(5000)
                    .execute().body();
            log.info("回调通知状态的响应结果:{}", result);
            // 7.1 修改子任务回调状态为成功：1
            burnFile.setCallBackStatus(TaskType.SUCCESS.getType());
        } catch (Exception e) {
            log.error("下载文件回调失败，原因:", e);
            // 7.2 修改子任务回调状态为失败：-1
            burnFile.setCallBackStatus(TaskType.ERROR.getType());
            // 记录失败的原因
            burnFile.setCallBackErrorInfo(e.getMessage());
        } finally {
            this.updateById(burnFile);
        }

        return burnFile;
    }
}
