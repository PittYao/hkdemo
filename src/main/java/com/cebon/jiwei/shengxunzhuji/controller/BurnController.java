package com.cebon.jiwei.shengxunzhuji.controller;

import com.cebon.jiwei.shengxunzhuji.model.dto.request.BurnFileDTO;
import com.cebon.jiwei.shengxunzhuji.model.dto.request.BurnSettingDTO;
import com.cebon.jiwei.shengxunzhuji.model.dto.request.DownLoadVideoDTO;
import com.cebon.jiwei.shengxunzhuji.model.po.BurnSetting;
import com.cebon.jiwei.shengxunzhuji.service.IBurnFileService;
import com.cebon.jiwei.shengxunzhuji.service.IBurnSettingService;
import com.cebon.jiwei.shengxunzhuji.hkservice.IHkService;
import com.sun.jna.NativeLong;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.exception.ZipException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    IHkService hkService;

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
        return burnSettingService.save(burnSettingDTO);
    }

    /**
     * 下载文件
     *
     * @param burnSettingDTO 刻录任务参数
     * @return DB刻录任务
     */
    @PostMapping("/file")
    public void burnFile(@Valid @RequestBody BurnFileDTO burnFileDTO)  {
        burnFileService.burnFile(burnFileDTO);
    }

    /**
     * 审讯一体机视频下载
     *
     * @param downLoadVideoDTO
     * @return 登录号，如果登录号小于0，则登录失败
     */
    @PostMapping("/download")
    public NativeLong downLoadVideo(@Valid @RequestBody DownLoadVideoDTO downLoadVideoDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime sTime = LocalDateTime.parse(downLoadVideoDTO.getStartTime(), formatter).minusSeconds(2);
        LocalDateTime eTime = LocalDateTime.parse(downLoadVideoDTO.getEndTime(), formatter).plusSeconds(2);
        return hkService.downLoad(downLoadVideoDTO, sTime, eTime);
    }


}
