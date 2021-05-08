package com.cebon.jiwei.shengxunzhuji.controller;

import com.cebon.jiwei.shengxunzhuji.model.dto.request.BurnFileDTO;
import com.cebon.jiwei.shengxunzhuji.model.dto.request.BurnSettingDTO;
import com.cebon.jiwei.shengxunzhuji.model.dto.request.DownLoadVideoDTO;
import com.cebon.jiwei.shengxunzhuji.model.dto.response.TaskCallBackDTO;
import com.cebon.jiwei.shengxunzhuji.model.po.BurnFile;
import com.cebon.jiwei.shengxunzhuji.model.po.BurnSetting;
import com.cebon.jiwei.shengxunzhuji.model.po.BurnVideo;
import com.cebon.jiwei.shengxunzhuji.service.IBurnFileService;
import com.cebon.jiwei.shengxunzhuji.service.IBurnSettingService;
import com.cebon.jiwei.shengxunzhuji.service.IBurnVideoService;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.exception.ZipException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author: bugProvider
 * @date: 2021/5/6 11:33
 * @description: 海康审讯机
 */
@Slf4j
@Validated
@RestController()
@RequestMapping("/burn")
public class BurnController {
    @Autowired
    private IBurnVideoService burnVideoService;

    @Autowired
    private IBurnSettingService burnSettingService;

    @Autowired
    private IBurnFileService burnFileService;

    /**
     * 申请下载父任务
     *
     * @param burnSettingDTO 刻录任务参数
     * @return DB刻录任务
     */
    @PostMapping("/setting")
    public BurnSetting burnTask(@Valid @RequestBody BurnSettingDTO burnSettingDTO) throws ZipException {
        return burnSettingService.burnTask(burnSettingDTO);
    }

    /**
     * 下载文件到刻录文件夹下
     *
     * @param burnFileDTO 刻录任务参数
     * @return DB刻录任务
     */
    @PostMapping("/file")
    public BurnFile burnFile(@Valid @RequestBody BurnFileDTO burnFileDTO) {
        return burnFileService.burnFile(burnFileDTO);
    }

    /**
     * 下载审讯一体机视频到刻录文件夹下
     *
     * @param downLoadVideoDTO
     * @return 下载视频任务
     */
    @PostMapping("/download")
    public BurnVideo downLoadVideo(@Valid @RequestBody DownLoadVideoDTO downLoadVideoDTO) {
        return burnVideoService.burnVideo(downLoadVideoDTO);
    }


    /**
     * 任务回调测试接口
     *
     * @param taskCallBackDTO 刻录任务参数
     */
    @PostMapping(value = "/callBackTest")
    public String callbackTest(@RequestBody TaskCallBackDTO taskCallBackDTO) {
        log.info("接收到回调信息:{}", taskCallBackDTO);
        return "ok";
    }

}
