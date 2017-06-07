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
import java.util.Arrays;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    public static final int REQUEST_CODE = 10;
    public static final String KEY_DIR = "KEY_DIR";

    private ListView fileList;
    private TextView noPermissionText;
    private ArrayAdapter<File> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String path = getIntent().getStringExtra(KEY_DIR);
        File directory = path != null ?
                new File(path) :
                Environment.getRootDirectory();

        initViews();
        checkPermission();

        File[] fileList = directory.listFiles();
        if (fileList != null) {
            updateFileList(fileList);
        }
    }

    private void initViews() {
        adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1);

        noPermissionText = (TextView) findViewById(R.id.no_permission_text);
        fileList = (ListView) findViewById(R.id.file_list);
        fileList.setAdapter(adapter);
        fileList.setOnItemClickListener(this);
    }

    private void updateFileList(File[] files) {
        adapter.clear();
        adapter.addAll(Arrays.asList(files));
        adapter.notifyDataSetChanged();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE
            );
            showContent(false);
        } else {
            showContent(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showContent(false);
            }
        }
    }

    private void showContent(boolean visible) {
        noPermissionText.setVisibility(!visible ? View.VISIBLE : View.GONE);
        fileList.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        File file = adapter.getItem(position);
        if (file != null && file.isDirectory()) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.putExtra(KEY_DIR, file.getAbsolutePath());
            startActivity(intent);
        }
    }
}
