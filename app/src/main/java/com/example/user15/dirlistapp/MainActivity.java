package com.example.user15.dirlistapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {

    public static final int REQUEST_CODE = 10;
    public static final String KEY_DIR = "KEY_DIR";
    private ListView fileList;
    private TextView noPermissionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = getIntent();
//        if (intent != null) {
//            File directory = (intent != null) ?
//                    new File(intent.getStringExtra(KEY_DIR)) :
//                    Environment.getRootDirectory();
//
//        }

        File directory = Environment.getRootDirectory();

        noPermissionText = (TextView) findViewById(R.id.no_permission_text);
        fileList = (ListView) findViewById(R.id.file_list);
        List<String> dataList = Arrays.asList(directory.list());
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                dataList
        );
        fileList.setAdapter(adapter);
        fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.putExtra(KEY_DIR, adapter.getItem(position));
                startActivity(intent);
            }
        });


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE
            );
            noPermissionText.setVisibility(View.VISIBLE);
            fileList.setVisibility(View.GONE);
        } else {
            noPermissionText.setVisibility(View.GONE);
            fileList.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                noPermissionText.setVisibility(View.GONE);
                fileList.setVisibility(View.VISIBLE);
            }
        }
    }
}
