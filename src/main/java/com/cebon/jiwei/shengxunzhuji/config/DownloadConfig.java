package com.cebon.jiwei.shengxunzhuji.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: bugProvider
 * @date: 2021/5/6 19:25
 * @description:
 */
@Data
@Component
@ConfigurationProperties(prefix = "download")
public class DownloadConfig {
    private String vlcPath;
}
