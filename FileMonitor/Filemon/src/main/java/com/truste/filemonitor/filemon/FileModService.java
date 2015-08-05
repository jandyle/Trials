package com.truste.filemonitor.filemon;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import utils.EnvironmentUtilsStatic;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by jandyle on 7/22/15.
 */
public class FileModService extends Service{
    RecursiveFileObserver fileObSdCard;
    RecursiveFileObserver fileObExternal;
    RecursiveFileObserver fileObData;
    RecursiveFileObserver fileObMnt;
    //File sdcard;

    public void onCreate(){
        Process p;

        try {
            // Preform su to get root privledges
            p = Runtime.getRuntime().exec("su");

            // Attempt to write a file to a root-only
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            os.writeBytes("echo \"Do I have root?\" >/system/sd/temporary.txt\n");

            // Close the terminal
            os.writeBytes("exit\n");
            os.flush();
            try {
                p.waitFor();
                if (p.exitValue() != 255) {
                    // TODO Code to run on success
                    System.out.println("root");
                }
                else {
                    // TODO Code to run on unsuccessful
                    System.out.println("not root");
                }
            } catch (InterruptedException e) {
                // TODO Code to run in interrupted exception
                System.out.println("not root");
            }
        } catch (IOException e) {
            // TODO Code to run in input/output exception
            System.out.println("not root");
        }


        if (!EnvironmentUtilsStatic.is_external_storage_available()) {
            Toast.makeText(FileModService.this, "SDCARD is not available!", Toast.LENGTH_SHORT).show();
            return;
        }

            //fileObExternal = new RecursiveFileObserver(new File("/externalSdCard").getAbsolutePath());
            fileObData = new RecursiveFileObserver(new File("/data/data").getAbsolutePath());
            fileObMnt = new RecursiveFileObserver(new File("/mnt/").getAbsolutePath());

    }

    private void createFileObs(File f) {
        if (!f.isDirectory()) {
        } else {
           // fileOb = new RecursiveFileObserver(f.getAbsolutePath());
        }
    }

    public void onStart(Intent intent, int startid) {
        //fileObExternal.startWatching();
        fileObData.startWatching();
        fileObMnt.startWatching();
        Toast.makeText(this.getApplicationContext(), "start monitoring file modification", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        //fileObExternal.stopWatching();
        fileObMnt.stopWatching();
        fileObData.stopWatching();
        Toast.makeText(this.getApplicationContext(), "stop monitoring file modification", Toast.LENGTH_SHORT).show();
    }


    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
}
