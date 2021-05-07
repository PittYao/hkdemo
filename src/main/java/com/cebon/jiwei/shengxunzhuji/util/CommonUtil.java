package com.cebon.jiwei.shengxunzhuji.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * @author: bugProvider
 * @date: 2021/5/6 19:07
 * @description:
 */
public class CommonUtil {
    /**
     * 在指定路径下创建随机文件夹
     *
     * @param path
     * @return
     */
    public static String markRandomDir(String path) {
        // 1.随机生成文件夹名
        String uuid = IdUtil.fastSimpleUUID();
        String filePath = path.concat("\\").concat(uuid);

        // 2.创建文件夹
        FileUtil.mkdir(filePath);

        return filePath;
    }


    /**
     * 在指定文件夹下创建随机文件夹，并拷贝指定文件到随机文件夹下
     *
     * @param parentPath 指定父文件夹
     * @param srcPath    要拷贝的文件
     */
    public static String copyFileAndCreateRandomDir(String parentPath, String srcPath) {
        // 1.在parentPath文件夹下创建随机名称文件夹
        String dir = CommonUtil.markRandomDir(parentPath);
        // 2.获取源文件的绝对路径
        String distPath = System.getProperty("user.dir") + "\\" + srcPath;
        // 3.获取源文件的文件名
//        String fileName = FileUtil.getName(distPath);
        // 4.拷贝文件到指定文件夹中
        File copyFile = FileUtil.copy(distPath, dir, Boolean.TRUE);
        // 5.返回拷贝后的文件的绝对路径
//        return copyFile.getAbsolutePath() + "\\" + fileName;

        // 5.返回拷贝后的文件的父级路径
        return copyFile.getAbsolutePath();
    }

    /**
     * 并拷贝指定文件到指定文件夹下
     *
     * @param parentPath 指定父文件夹
     * @param srcPath    要拷贝的文件
     */
    public static String copyFile(String srcPath, String distPath) {
        // 1.获取源文件的绝对路径
        srcPath = System.getProperty("user.dir") + "\\" + srcPath;
        // 2.拷贝文件到指定文件夹中
        File copyFile = FileUtil.copy(srcPath, distPath, Boolean.TRUE);
        // 3.返回拷贝后的文件的父级路径
        return copyFile.getAbsolutePath();
    }

    /**
     * windows cmd命令并拷贝指定文件到指定文件夹下
     *
     * @param parentPath 指定父文件夹
     * @param srcPath    要拷贝的文件
     */
    public static void copyFileByCmd(String srcPath, String distPath) {
        // 1.获取源文件的绝对路径
        srcPath = System.getProperty("user.dir") + "\\" + srcPath;
        String cmd = "xcopy " + srcPath + " " + distPath + "/I /E /Y";
        CommonUtil.executeLocalCmd(cmd, null);
    }


    /**
     * zip路径下解压到本路径
     *
     * @param zipPath   zip路径
     * @param deleteZip 是否删除源zip文件
     * @return 解压后的zip路径
     */
    public static String extractZip(String zipPath, Boolean deleteZip) throws ZipException {
        File file = new File(zipPath);
        // 1.文件父级路径
        String extractPath = file.getParent();
        // 2.解压到指定目录
        ZipFile zipFile = new ZipFile(file);
        zipFile.extractAll(extractPath);
        // 3.删除源zip文件
        if (deleteZip) {
            FileUtil.del(file);
        }

        return extractPath;
    }

    /**
     * Windows执行本地命令行
     * 测试ok
     *
     * @param cmd
     * @param workpath 在此目录下执行
     * @return
     */
    public static String executeLocalCmd(String cmd, File workpath) {
        try {
            String[] cmdA = {"cmd.exe", "/c", cmd};
            Process process = null;
            if (workpath == null) {
                process = Runtime.getRuntime().exec(cmdA);
            } else {
                process = Runtime.getRuntime().exec(cmdA, null, workpath);
            }
            LineNumberReader br = new LineNumberReader(new InputStreamReader(process.getInputStream(), "GBK"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getClassPathResource(String filePath) {
        String path = CommonUtil.class.getResource(filePath).getPath();
        return path.substring(1);
    }
}
