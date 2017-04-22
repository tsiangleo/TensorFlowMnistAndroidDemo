package com.github.tsiangleo.tfdemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;


/**
 * Created by tsiang on 2016/11/26.
 */

public class MainActivity  extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";

    private Button imgChooseButton,startRecognitionButton;
    private ImageView imageView;
    private TextView statusTextView;
    private Bitmap bitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestReadExternalPermission();

        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imgView);
        imgChooseButton = (Button) findViewById(R.id.imgChooseButton);
        startRecognitionButton = (Button) findViewById(R.id.startRecognitionButton);
        startRecognitionButton.setVisibility(View.GONE);
        statusTextView = (TextView) findViewById(R.id.statusTextView);
        imgChooseButton.setOnClickListener(this);
        startRecognitionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == imgChooseButton){
            chooseFile();
            imageView.setVisibility(View.INVISIBLE);
        }else if(v == startRecognitionButton){
            recognition();
        }
    }

    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult( Intent.createChooser(intent, "选择文件"), 1);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "请先安装一个文件管理器.", Toast.LENGTH_SHORT).show();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                Log.i(TAG,"uri:"+uri);

                try {
                    String realPath = FileUtils.getPath(this,uri);
                    Log.i(TAG,"realPath:"+realPath);
                    statusTextView.setVisibility(View.VISIBLE);
                    if(realPath == null) {
                        statusTextView.setText("文件不存在，请重新选择！");
                        imgChooseButton.setText("重新选择文件");
                    }else if(!(realPath.endsWith(".jpg") || realPath.endsWith(".png") || realPath.endsWith(".bmp"))) {
                        statusTextView.setText("文件必须是jpg/png/bmp格式的，请重新选择！");
                        imgChooseButton.setText("重新选择文件");
                    }else{
                        bitmap = BitmapFactory.decodeFile(realPath,null);
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setImageBitmap(bitmap);
                        statusTextView.setText("当前选择文件:"+realPath);
                        startRecognitionButton.setVisibility(View.VISIBLE);
                        imgChooseButton.setVisibility(View.GONE);
                    }

                }catch (java.lang.SecurityException e){
                    statusTextView.setText("应用需要读取SD卡，请先授权读取SD卡！");
                }catch (Exception e){
                    Log.w(TAG,"onActivityResult:"+e);
                    statusTextView.setText("选择文件出错:"+e.getMessage());
                }
            }
        }
    }


    private void recognition() {
        try {
            RecognitionService recognitionService = new RecognitionService(MainActivity.this);

            float[] outSoftmax = recognitionService.recognize(bitmap);

            statusTextView.setText("识别结果："+recognitionService.argmax(outSoftmax));

            Toast.makeText(this,"outSoftmax:"+ Arrays.toString(outSoftmax),Toast.LENGTH_LONG).show();

            startRecognitionButton.setVisibility(View.GONE);
            imgChooseButton.setText("继续选择文件");
            imgChooseButton.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.e(TAG, "识别中发生异常:" + e);

            statusTextView.setText("识别中发生异常！"+e.getMessage());

            startRecognitionButton.setVisibility(View.GONE);
            imgChooseButton.setText("重新选择文件");
            imgChooseButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 读取SD卡需要用户授权。
     */
    @SuppressLint("NewApi")
    private void requestReadExternalPermission() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "READ permission IS NOT granted...");

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);

        } else {
            Log.d(TAG, "READ permission is granted...");
        }
    }
}
