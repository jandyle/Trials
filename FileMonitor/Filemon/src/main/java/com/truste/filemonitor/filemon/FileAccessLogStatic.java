package com.truste.filemonitor.filemon;

import java.util.concurrent.CopyOnWriteArrayList;

public class FileAccessLogStatic {
	public static String accessLogMsg = "";
	public static CopyOnWriteArrayList<String> accessPaths = new CopyOnWriteArrayList<String>();

}
