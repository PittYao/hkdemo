package com.cebon.jiwei.shengxunzhuji.service.impl;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cebon.jiwei.shengxunzhuji.config.DownloadConfig;
import com.cebon.jiwei.shengxunzhuji.exception.BusinessException;
import com.cebon.jiwei.shengxunzhuji.mapper.BurnSettingMapper;
import com.cebon.jiwei.shengxunzhuji.model.dto.request.BurnSettingDTO;
import com.cebon.jiwei.shengxunzhuji.model.enums.EncryptionType;
import com.cebon.jiwei.shengxunzhuji.model.po.BurnSetting;
import com.cebon.jiwei.shengxunzhuji.service.IBurnSettingService;
import com.cebon.jiwei.shengxunzhuji.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.exception.ZipException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author: bugProvider
 * @date: 2021/5/6 14:19
 * @description: 刻录任务
 */
@Slf4j
@Service
public class BurnSettingServiceImpl extends ServiceImpl<BurnSettingMapper, BurnSetting> implements IBurnSettingService {
    @Autowired
    private IBurnSettingService burnService;
    @Autowired
    private DownloadConfig downloadConfig;

    @Value("${download.tempPath}")
    private String downloadTempPath;


    @Override
    public BurnSetting save(BurnSettingDTO burnSettingDTO) throws ZipException {
        Integer encryptionType = burnSettingDTO.getEncryptionType();
        String password = burnSettingDTO.getPassword();
        String odaSavePath = burnSettingDTO.getOdaSavePath();

        if (encryptionType.equals(EncryptionType.PASSWORD.getType()) && Strings.isBlank(password)) {
            // 密码加密，校验是否传入密码
            throw new BusinessException(500, "加密任务传入的密码不能为空");
        }

        // 1.在创建oda刻录文件夹
        FileUtil.mkdir(odaSavePath);

        // 2.将vlc播放器拷贝到oda刻录文件夹下
        log.info("拷贝vlc播放器到oda刻录文件夹中");
        CommonUtil.copyFile(downloadConfig.getVlcPath(), odaSavePath);

        // 3.存储刻录任务
        BurnSetting burnSetting = BurnSetting.builder()
                .createTime(LocalDateTime.now())
                .taskNum(burnSettingDTO.getTaskNum())
                .encryptionType(encryptionType)
                .password(password)
                .odaSavePath(burnSettingDTO.getOdaSavePath())
                .callBackUrl(burnSettingDTO.getCallBackUrl())
                .doneTaskNum(0)
                .build();

        this.save(burnSetting);

        log.info("存储刻录任务:{}", burnSetting.toString());

        return burnSetting;
    }

}
