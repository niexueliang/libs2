package com.nil.code.libs.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.content.ContextCompat;

import java.io.File;

/**
 * SD卡相关的辅助类
 */
public class SDCardUtil {
    private SDCardUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断SDCard是否可用
     *
     * @return true 已经挂载代表存在sd卡， false 挂载失败，代表不存在sd卡
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡的剩余容量 单位byte
     *
     * @return
     */
    public static long getSDCardAllSize() {
        if (isSDCardEnable()) {
            StatFs stat = new StatFs(getSDCardPath());
            // 获取空闲的数据块的数量
            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
            // 获取单个数据块的大小（byte）
            long freeBlocks = stat.getAvailableBlocks();
            return freeBlocks * availableBlocks;
        }
        return 0;
    }

    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     *
     * @param filePath
     * @return 容量字节 SDCard可用空间，内部存储可用空间
     */
    public static long getFreeBytes(String filePath) {
        // 如果是sd卡的下的路径，则获取sd卡可用容量
        if (filePath.startsWith(getSDCardPath())) {
            filePath = getSDCardPath();
        } else {// 如果是内部存储的路径，则获取内存存储的可用容量
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        return stat.getBlockSize() * availableBlocks;
    }

    /**
     * 获取系统存储路径
     *
     * @return
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }


    /**
     * 获取SD卡路径
     *
     * @return 系统默认sd卡路径，当挂在路径不存在时可能返回null
     */
    public static String getSDCardPath() {
        if (isSDCardEnable()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        }
        return null;
    }

    //获取sd卡缓存目录数据
    //添加android/data/包名/cache
    public static String getSdCacheDir(Context context) {
        File cacheFile = context.getExternalCacheDir();
        if (cacheFile != null && cacheFile.exists()) {
            return cacheFile.getAbsolutePath();
        } else {
            File[] files = ContextCompat.getExternalCacheDirs(context);
            if (null != files && files.length > 0) {
                for (File file : files) {
                    if (file != null && file.exists()) {
                        return file.getAbsolutePath();
                    }
                }
            }
        }
        String sdPath = getSDCardPath();
        return (sdPath == null) ? null : sdPath + "/Android/data/" + context.getPackageName() + "/cache";
    }


}
