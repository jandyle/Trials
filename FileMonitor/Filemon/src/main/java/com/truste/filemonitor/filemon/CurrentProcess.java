package com.truste.filemonitor.filemon;

import android.app.Application;
import android.content.Context;

/**
 * Created by jandyle on 7/22/15.
 */
public class CurrentProcess extends Application {
    private static CurrentProcess instance;

    public static CurrentProcess getInstance(){
        return instance;
    }

    public static Context getContext(){
        return instance;
    }

    @Override
    public void onCreate(){
        instance=this;
        super.onCreate();
    }
}
