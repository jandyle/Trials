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
   // int num_of_fos = 0;
  //  @Override
    public void onCreate(){
        if (!EnvironmentUtilsStatic.is_external_storage_available()) {
            Toast.makeText(FileModService.this, "SDCARD is not available!", Toast.LENGTH_SHORT).show();
            return;
        }
        File sdcard = new File("/sdcard/");
        if (sdcard == null) {
            return;
        } else {
            //fileOb_list.clear();
           // num_of_fos = 0;
            createFileObs(sdcard);
        }
        sdcard = new File("/externalSdCard/");
        if (sdcard == null) {
            return;
        } else {
            //fileOb_list.clear();
            // num_of_fos = 0;
            createFileObs(sdcard);
        }
    }

    private void createFileObs(File f) {
		/*if (num_of_fos > MAX_FO) {
			return;
		}*/
        if (!f.isDirectory()) {
            //MyFileObserver aFileOb = new MyFileObserver(f.getAbsolutePath());
            //fileOb_list.add(aFileOb);
        } else {
            fileOb = new RecursiveFileObserver(f.getAbsolutePath());
			/*MyFileObserver aFileOb = new MyFileObserver(f.getAbsolutePath());
			fileOb_list.add(aFileOb);
			num_of_fos++;
			try {
				for (File currentFile : f.listFiles()) {
					createFileObs(currentFile);
				}
			} catch (Exception e) {
				Log.e("Error", e.toString());
			}*/
        }
    }

    public void onStart(Intent intent, int startid) {
        fileOb.startWatching();
		/*for (int i = 0; i < fileOb_list.size(); ++i) {
			fileOb_list.get(i).startWatching();
		}*/
        Toast.makeText(this.getApplicationContext(), "start monitoring file modification", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        fileOb.stopWatching();
		/*for (int i = 0; i < fileOb_list.size(); ++i) {
			fileOb_list.get(i).stopWatching();
		}*/
        Toast.makeText(this.getApplicationContext(), "stop monitoring file modification", Toast.LENGTH_SHORT).show();
    }


    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
}
