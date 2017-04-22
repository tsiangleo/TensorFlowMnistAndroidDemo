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


/**
 * Created by tsiang on 2016/11/26.
 */

public class MainActivity  extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";

    private Button imgChooseButton,startRecognitionButton;
    private ImageView imageView;
    private TextView statusTextView;

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
        /**
         *
         //intent.setType(“image/*”);//选择图片
         //intent.setType(“audio/*”); //选择音频
         //intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
         //intent.setType(“video/*;image/*”);//同时选择视频和图片
         */
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult( Intent.createChooser(intent, "Select a File to Upload"), 1);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
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
                        Bitmap bitmap = BitmapFactory.decodeFile(realPath,null);
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setImageBitmap(bitmap);
                        statusTextView.setText("当前选择文件:"+realPath);
                        startRecognitionButton.setVisibility(View.VISIBLE);
                        imgChooseButton.setVisibility(View.GONE);
                    }

                }catch (java.lang.SecurityException e){
                    statusTextView.setText("没有授权读取SD卡，请先授权:"+e.getMessage());
                }catch (Exception e){
                    Log.w(TAG,"onActivityResult:"+e);
                    statusTextView.setText("选择文件出错:"+e.getMessage());
                }
            }
        }
    }


    private void recognition() {
        try {
//            RecognitionService recognitionService = new RecognitionService(MainActivity.this);
//            recognitionService.init();
//            recognitionService.recognize();
            statusTextView.setText("识别结果：7");

            startRecognitionButton.setVisibility(View.GONE);
            imgChooseButton.setText("继续选择文件");
            imgChooseButton.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.e(TAG, "RecognitionTask.doInBackground:" + e);

            statusTextView.setText("识别中发生异常！"+e.getMessage());

            startRecognitionButton.setVisibility(View.GONE);
            imgChooseButton.setText("重新选择文件");
            imgChooseButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 从SD卡上读取需要用户授权。
     * http://blog.csdn.net/likesidehu/article/details/52668879
     *
     */
    @SuppressLint("NewApi")
    private void requestReadExternalPermission() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "READ permission IS NOT granted...");

            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                // 0 是自己定义的请求coude
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
        } else {
            Log.d(TAG, "READ permission is granted...");
        }
    }

    /**
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(TAG, "requestCode=" + requestCode + "; --->" + permissions.toString()
                + "; grantResult=" + grantResults.toString());
        switch (requestCode) {
            case 0: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted
                    // request successfully, handle you transactions

                } else {

                    // permission denied
                    // request failed
                }

                return;
            }
            default:
                break;

        }
    }
}
