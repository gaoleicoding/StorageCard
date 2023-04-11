package com.storage.select;

import android.content.Context;
import android.os.Environment;

import androidx.core.content.ContextCompat;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 用来获取存储路径的类.
 *
 * @author Yan
 */
public class StorageManager {

    public static String currentPath;
    private String[] pathsTmp;
    private ArrayList<String> storagePaths;
    public static HashMap<String, Boolean> pathsAvailable;

    public static StorageManager instance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private final static StorageManager instance = new StorageManager();
    }

    public ArrayList<String> getStoragePaths() {
        return storagePaths;
    }

    public StorageManager() {

        storagePaths = new ArrayList<>();
        pathsAvailable = new HashMap<>();
        getVolumePaths();
    }

    /**
     * 获取可用的存储路径.
     */
    public void getVolumePaths() {
        boolean suc = false;
        try {
            if (android.os.Build.VERSION.SDK_INT >= 19) {
                pathsTmp = getExtraPath();
            } else {
                try {
                    android.os.storage.StorageManager sManager = (android.os.storage.StorageManager) AppApplication.mContext
                            .getSystemService(Context.STORAGE_SERVICE);
                    Method methodGetPaths = sManager.getClass().getMethod("getVolumePaths");
                    pathsTmp = (String[]) methodGetPaths.invoke(sManager);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }

            }
            suc = true;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!suc) { // 反射失败，使用系统默认的存储路径，4.4以前用返回外置存储卡，4.4之后会返回内置存储路径.
                if (SDCardUtil.checkSDExit()) {
                    pathsTmp = new String[1];
                    pathsTmp[0] = Environment.getExternalStorageDirectory()
                            .getAbsolutePath();
                } else {
                    pathsTmp = new String[1];
                    pathsTmp[0] = "";
                }
            }
            Collections.addAll(storagePaths, pathsTmp);
        }

    }

    public String[] getExtraPath() {
        String extra = AppApplication.mContext.getExternalFilesDir(null)
                .getAbsolutePath();
        List<String> paths = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= 19) {
            File[] files = ContextCompat.getExternalFilesDirs(
                    AppApplication.mContext, null);

            for (File file : files) {
                if (file != null) {
                    String tmpPath = file.getAbsolutePath();

                    if (!tmpPath.contains(extra)) {
                        paths.add(tmpPath);
                    } else {
                        String p = tmpPath.substring(0, tmpPath.indexOf("/Android/data"));
                        paths.add(p);
                    }

                }
            }
        }
        String[] result = new String[paths.size()];
        for (int i = 0; i < paths.size(); i++) {
            result[i] = paths.get(i);
        }
        return result;
    }

    /**
     * 获取可用空间最大的存储路径.
     */
    public String getMostAvailable() {
        String tmp = "";
        long size = 0;
        for (String path : storagePaths) {
            long tmpSize = SDCardUtil.getAvailableSize(path);
            if (tmpSize >= size) {
                size = tmpSize;
                tmp = path;
            }
        }
        return tmp;
    }

    private boolean isStorageValidate(String path) {
        boolean canUse = false;
        try {
            File dir = new File(path);
            File childDir = new File(path
                    + File.separator + ConstantUtils.PATH_INTERNAL);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!childDir.exists()) {
                canUse = childDir.mkdirs();
            } else {
                canUse = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return canUse;
    }


}
