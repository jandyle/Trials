package com.truste.filemonitor.filemon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.apache.commons.io.FileUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.lang.reflect.Method;

//use asynctask background thread to detect file modification and then update the UI once there is 
//a new change detected
public class FileModificationMonitor extends Activity {
	private Button btn1;
	private Button btn2;
	private Button btn3;
	private Button btn4;
	private Button btn5;
	private TextView text1;
	//private boolean stop_capture;
	private static boolean started = false;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_modification_monitor);

        btn1 = (Button) findViewById(R.id.file_modification_monitor_btn_first);
        btn2 = (Button) findViewById(R.id.file_modification_monitor_btn_second);
        btn3 = (Button) findViewById(R.id.file_modification_monitor_btn_third);
        btn4 = (Button) findViewById(R.id.file_modification_monitor_btn_forth);
		btn5 = (Button)	findViewById(R.id.file_modification_monitor_btn_fifth);
        int screenWidth = this.getWindowManager().getDefaultDisplay().getWidth();
        btn1.setWidth(screenWidth/5);
        text1 = (TextView) findViewById(R.id.file_modification_monitor_log);

        if (!started) {
//			Runtime.getRuntime().exec("find /data/data -type d -exec chmod 755 {} +");
			//Runtime.getRuntime().exec("find /data/data -type f -exec chmod 755 {} +");
			try {

				Process process2 = null;
				DataOutputStream dataOutputStream = null;

				try {
					process2 = Runtime.getRuntime().exec("su");
					dataOutputStream = new DataOutputStream(process2.getOutputStream());
					dataOutputStream.writeBytes("chmod -R 777 /data/data \n");
					dataOutputStream.writeBytes("exit\n");
					dataOutputStream.flush();
					process2.waitFor();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (dataOutputStream != null) {
							dataOutputStream.close();
						}
						process2.destroy();
					} catch (Exception e) {
					}
				}


				startService(new Intent(FileModificationMonitor.this.getApplicationContext(), FileModService.class));
			} catch(Exception e){
				e.printStackTrace();
			}
			btn1.setEnabled(false);
			started = true;
        }
        btn1.setOnClickListener(new View.OnClickListener() {
			//@Override
			public void onClick(View v) {
				startService(new Intent(FileModificationMonitor.this.getApplicationContext(), FileModService.class));
				btn1.setEnabled(false);
				started = true;
			}
		});
        btn2.setWidth(screenWidth / 5);
        btn2.setOnClickListener(new View.OnClickListener() {
			//@Override
			public void onClick(View v) {
				//stop_capture = true;
				stopService(new Intent(FileModificationMonitor.this.getApplicationContext(), FileModService.class));
				btn1.setEnabled(true);
				started = false;
			}
		});
        btn3.setWidth(screenWidth / 5);
        btn3.setOnClickListener(new View.OnClickListener() {
			//@Override
			public void onClick(View v) {
				refreshLog();
			}
		});
        btn4.setWidth(screenWidth / 5);
        btn4.setOnClickListener(new View.OnClickListener() {
			//@Override
			public void onClick(View v) {
				clearLog();
			}
		});
		btn5.setWidth(screenWidth / 5);
		btn5.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				//copy files
				copyAllZefiles();
			}
		});
        refreshLog();
	}
	
	
	public void refreshLog() {
		text1.setText(FileAccessLogStatic.accessLogMsg);
	}
	
	public void clearLog() {
		FileAccessLogStatic.accessLogMsg = "";
		FileAccessLogStatic.accessPaths.clear();
		text1.setText("");
	}

	@Override
	public void onResume(){
		super.onResume();
		refreshLog();
	}

	public void copyAllZefiles(){
		String trustePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TrusteFiles/";
		File dir = new File(trustePath);
		try{
			if(dir.mkdir()) {
				System.out.println("Directory created");
			} else {
				System.out.println("Directory is not created");
			}
		}catch(Exception e){
			e.printStackTrace();
		}


		for(String s: FileAccessLogStatic.accessPaths){
			File newFile = new File(s);
			try {
			FileUtils.copyFileToDirectory(new File(s), new File(trustePath+newFile.getParent()));
				//System.out.println(s + trustePath);
			} catch (Exception e){
			e.printStackTrace();
			}
		}

	}

	public int chmod(File path, int mode) throws Exception {
		Class fileUtils = Class.forName("android.os.FileUtils");
		Method setPermissions =
				fileUtils.getMethod("setPermissions", String.class, int.class, int.class, int.class);
		return (Integer) setPermissions.invoke(null, path.getAbsolutePath(), mode, -1, -1);
	}

}
