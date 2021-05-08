# 纪委 对接海康审讯一体机 
## 本项目功能点
### 从审讯一体机中下载视频文件到指定文件夹下
### 下载远程文件到指定文件夹下
### 所有下载任务都会回调通知任务完成状态

- 在git仓库中没有上传vlc播放器文件夹，拉取项目后需要拷贝一个vlc的文件夹到lib目录下
  
    - 因为海康的视频用windows自带的视频播放器，播放不了     

- 项目mvn clean package 打jar包后，需要将项目中的hkwinlib和lib文件夹拷贝到jar包的同级目录再运行jar包
 层级结构

    - 层级结构:
        -   shengxunzhuji-0.0.1-SNAPSHOT.jar
        -   lib
        -   hkwinlib
        -   startup.bat

- resources 的 startup.bat是启动jar的脚本,双击执行

- 测试接口文件在 resource/demo.http
