package com.truste.filemonitor.filemon;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import utils.EnvironmentUtilsStatic;

import java.io.File;

/**
 * Created by jandyle on 7/22/15.
 */
public class FileModService extends Service{
    private RecursiveFileObserver fileOb;

    public void onCreate(){
        if (!EnvironmentUtilsStatic.is_external_storage_available()) {
            Toast.makeText(FileModService.this, "SDCARD is not available!", Toast.LENGTH_SHORT).show();
            return;
        }
//        File sdcard = new File("/sdcard/");
//        if (sdcard == null) {
//            return;
//        } else {
//            createFileObs(sdcard);
//            System.out.println("sdcard watched");
//        }
//        sdcard = new File("/externalSdCard/");
//        if (sdcard == null) {
//            return;
//        } else {
//            createFileObs(sdcard);
//            System.out.println("externalSdCard watched");
//        }
        File sdcard = new File("/data/");
        if (sdcard == null) {
            System.out.println("data not watched");
            return;
        } else {
            createFileObs(sdcard);
            System.out.println(sdcard.getPath() + "watched");
        }
//        sdcard = new File("/mnt/");
//        if (sdcard == null) {
//            return;
//        } else {
//            createFileObs(sdcard);
//            System.out.println("mnt watched");
//        }
    }

    private void createFileObs(File f) {
        if (!f.isDirectory()) {
        } else {
            fileOb = new RecursiveFileObserver(f.getAbsolutePath());
        }
    }

    public void onStart(Intent intent, int startid) {
        fileOb.startWatching();
        Toast.makeText(this.getApplicationContext(), "start monitoring file modification", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        fileOb.stopWatching();
        Toast.makeText(this.getApplicationContext(), "stop monitoring file modification", Toast.LENGTH_SHORT).show();
    }


    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
}
