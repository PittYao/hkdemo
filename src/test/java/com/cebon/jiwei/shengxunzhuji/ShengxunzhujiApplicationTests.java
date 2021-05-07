package com.cebon.jiwei.shengxunzhuji;

import com.cebon.jiwei.shengxunzhuji.config.DownloadConfig;
import com.cebon.jiwei.shengxunzhuji.util.CommonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShengxunzhujiApplicationTests {
    @Autowired
    private DownloadConfig downloadConfig;

    @Test
    void contextLoads() {
        String src = "E:\\IdeaWorkSpace\\盛邦\\省纪委\\shengxunzhuji\\lib\\vlc";
        String dest = "E:\\downTemp\\790e7fd8353e458f8c281e202eb4f442\\vlc";
        String cmd = "xcopy " + src + " " + dest + "/I /E /Y";

        String s = CommonUtil.executeLocalCmd(cmd, null);
    }




}
