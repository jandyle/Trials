package com.truste.filemonitor.filemon;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.os.FileObserver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


/**
 * Created by jandyle on 7/22/15.
 */
public class RecursiveFileObserver extends FileObserver {
    public String absolutePath;
    public String androidPath;
        ArrayList<SingleFileObserver> mObservers;
        ArrayList<String> allpaths;
        String cont;
       File logs;

        public RecursiveFileObserver(String path){
        super(path, FileObserver.ALL_EVENTS);
            absolutePath = path;
    }

        @Override
        public void onEvent ( int event, String path){
            if (path == null) {
                return;
            }

            //a new file or subdirectory was created under the monitored directory
            if ((FileObserver.CREATE & event)!=0) {
                getMessage(path, "is created");
                FileAccessLogStatic.accessPaths.add(path);
                RecursiveFileObserver fileOb = new RecursiveFileObserver(path);
                fileOb.startWatching();
            }

            if ((FileObserver.MODIFY & event)!=0) {
                getMessage(path,"is modified");
                FileAccessLogStatic.accessPaths.add(path);
            }
            if ((FileObserver.CLOSE_WRITE & event)!=0) {
                getMessage(path,"is written and closed");
                FileAccessLogStatic.accessPaths.add(path);
            }

            if ((FileObserver.DELETE & event)!=0) {
                getMessage(path,"is deleted");
                FileAccessLogStatic.accessPaths.add(path);
            }
            if ((FileObserver.DELETE_SELF & event)!=0) {
                getMessage(path,"is deleted");
                FileAccessLogStatic.accessPaths.add(path);
            }
            if ((FileObserver.MOVED_FROM & event)!=0) {
                getMessage(path,"is moved");
                FileAccessLogStatic.accessPaths.add(path);
            }
            //a file or subdirectory was moved to the monitored directory
            if ((FileObserver.MOVED_TO & event)!=0) {
                getMessage(path,"is moved");
                FileAccessLogStatic.accessPaths.add(path);
            }
            //the monitored file or directory was moved; monitoring continues
            if ((FileObserver.MOVE_SELF & event)!=0) {
                getMessage(path,"is moved");
                FileAccessLogStatic.accessPaths.add(path);
            }
            //Metadata (permissions, owner, timestamp) was changed explicitly
            if ((FileObserver.ATTRIB & event)!=0) {
                getMessage(path,"is changed");
                FileAccessLogStatic.accessPaths.add(path);
            }
        }


    public void getAllProcess(String path){
        ActivityManager manager = (ActivityManager) CurrentProcess.getContext().getSystemService(CurrentProcess.getContext().ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningProcesses = manager.getRunningAppProcesses();

        for(RunningAppProcessInfo a: runningProcesses) {
            if((a.processName.contains("com.android.")||a.processName.contains("com.google.")||a.processName.contains("system")||a.processName.contains("android.process.")) == false)
          //  FileAccessLogStatic.accessLogMsg += a.processName +":    "+path+"\n";
            FileAccessLogStatic.accessLogMsg += a.processName+"\n";
        }
    }

    public void getMessage(String path, String message){
        ActivityManager manager = (ActivityManager) CurrentProcess.getContext().getSystemService(CurrentProcess.getContext().ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningProcesses = manager.getRunningAppProcesses();

        for(RunningAppProcessInfo a: runningProcesses) {
            if((a.processName.contains("com.android.")||a.processName.contains("com.google.")||a.processName.contains("system")||a.processName.contains("android.process.")) == false) {
                FileAccessLogStatic.accessLogMsg += a.processName + ": " + path + " " + message + "\n";
                break;
            }
        }
    }

    @Override
        public void startWatching () {
        if (mObservers != null)
            return;

        mObservers = new ArrayList();
        Stack stack = new Stack();
        stack.push(absolutePath);

        while (!stack.isEmpty()) {
            String parent = String.valueOf(stack.pop());
          //  System.out.println(parent);
            if(parent.equals("/sdcard/TrusteFiles")==false)
            mObservers.add(new SingleFileObserver(parent));
            File path = new File(parent);
            path.setWritable(true);
            File[] files = path.listFiles();
            if (null == files)
                continue;
            for (File f : files) {
                if (f.isDirectory() && !f.getName().equals(".") && !f.getName()
                        .equals("..")) {
                    stack.push(f.getPath());
                }
                System.out.println(f.getPath() + " is watched");
            }
        }
        for (SingleFileObserver sfo : mObservers) {
            sfo.startWatching();
        }
    }


    class SingleFileObserver extends FileObserver {
        String mPath;

        public SingleFileObserver(String path) {
            this(path, ALL_EVENTS);
            mPath = path;
        }

        public SingleFileObserver(String path, int mask) {
            super(path, mask);
            mPath = path;
        }

        @Override
        public void onEvent(int event, String path) {
            String newPath = mPath + "/" + path;
              RecursiveFileObserver.this.onEvent(event, newPath);
        }
    }

}






