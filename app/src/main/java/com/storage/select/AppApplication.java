package com.storage.select;

import android.app.Application;
import android.content.Context;

public class AppApplication extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }
}
