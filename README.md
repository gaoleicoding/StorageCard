# StorageCard
Android 获取内置和外置存储卡的路径及总共、可用空间

5.0版本及以上可用ContextCompat.getExternalFilesDirs() 获取到内置和外置存储卡路径

if (android.os.Build.VERSION.SDK_INT >= 19) {
    File[] files = ContextCompat.getExternalFilesDirs(
            BaseApplication.mContext, null)
}