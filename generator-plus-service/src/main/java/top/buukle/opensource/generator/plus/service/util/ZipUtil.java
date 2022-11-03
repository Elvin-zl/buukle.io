package top.buukle.opensource.generator.plus.service.util;


import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.springframework.util.StreamUtils.BUFFER_SIZE;

/**
 * Author: zh
 * <p>
 * Desc: 压缩包工具类
 * <p>
 * Date: Created in 2018/5/19 13:51
 */

@Slf4j
public class ZipUtil {

    static final int BUFFER = 8192;

    /**
     * 把文件集合打成zip压缩包
     *
     * @param srcFiles 压缩文件集合
     * @param zipFile  zip文件名
     * @throws RuntimeException 异常
     */
    public static void toZip(List<File> srcFiles, File zipFile) throws RuntimeException {
        long start = System.currentTimeMillis();
        if (zipFile == null) {
            log.error("压缩包文件名为空！");
            return;
        }
        if (!zipFile.getName().endsWith(".zip")) {
            log.error("压缩包文件名异常，zipFile={}", zipFile.getPath());
            return;
        }
        ZipOutputStream zos = null;
        try {
            FileOutputStream out = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(out);
            FileInputStream in = null;
            for (File srcFile : srcFiles) {
                byte[] buf = new byte[BUFFER_SIZE];
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int len;
                in = new FileInputStream(srcFile);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.setComment("我是注释");
                zos.closeEntry();
            }
            zos.close();
            in.close();
            out.close();
            long end = System.currentTimeMillis();
            log.debug("压缩完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            log.error("ZipUtil toZip exception, ", e);
            throw new RuntimeException("zipFile error from ZipUtils", e);
        }
    }

    public static void compress(String srcPathName, File zipFile) {
        File file = new File(srcPathName);
        if (!file.exists())
            throw new RuntimeException(srcPathName + "不存在！");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32());
            ZipOutputStream out = new ZipOutputStream(cos);
            String basedir = "";
            compress(file, out, basedir);
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void compress(File file, ZipOutputStream out, String basedir) {
        /* 判断是目录还是文件 */
        if (file.isDirectory()) {
            log.debug("压缩：" + basedir + file.getName());
            compressDirectory(file, out, basedir);
        } else {
            log.debug("压缩：" + basedir + file.getName());
            compressFile(file, out, basedir);
        }
    }

    /** 压缩一个目录 */
    private static void compressDirectory(File dir, ZipOutputStream out, String basedir) {
        if (!dir.exists())
            return;

        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            /* 递归 */
            compress(files[i], out, basedir + dir.getName() + "/");
        }
    }

    /** 压缩一个文件 */
    private static void compressFile(File file, ZipOutputStream out, String basedir) {
        if (!file.exists()) {
            return;
        }
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(basedir + file.getName());
            out.putNextEntry(entry);
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            bis.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}