package com.truste.filemonitor.filemon;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends Activity{
    RecursiveFileObserver mObserver;
    Button b;
    TextView txt1;
    int count = 0;
    File DIRECTORY_DCIM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt1 = (TextView) findViewById(R.id.textView);









    }





}
