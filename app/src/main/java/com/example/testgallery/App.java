package com.example.testgallery;

import android.app.Application;
import android.content.Context;

/**
 * Created by avukelic on 25-Jul-18.
 */
// 앱 내로 DATA공유를 위한 Application
public class App extends Application {

    // 전역변수 context 선언
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
