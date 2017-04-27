package com.github.tsiangleo.tfdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.util.Arrays;


public class RecognitionService {

    public static final String TAG = "RecognitionService";

    public static final String MODEL_FILE = "file:///android_asset/mnist-tf1.0.1.pb"; //asserts目录下的pb文件名字
    public static final String INPUT_NODE = "input";       //输入节点的名称
    public static final String OUTPUT_NODE = "out_softmax";  //输出节点的名称
    public static final String KEEP_PROB_NODE = "keep_prob_placeholder"; // keep_prob节点的名称

    public static final int NUM_CLASSES = 10;   //输出节点的个数，即总的类别数。
    public static final int HEIGHT = 28;       //输入图片的像素高
    public static final int WIDTH = 28;        //输入图片的像素宽

    private TensorFlowInferenceInterface inferenceInterface;

    public RecognitionService(Context context){
        //初始化TensorFlowInferenceInterface对象。
        inferenceInterface = new TensorFlowInferenceInterface();

        //根据指定的MODEL_FILE创建一个本地的TensorFlow session
        final int status = inferenceInterface.initializeTensorFlow(context.getAssets(), MODEL_FILE);
        if (status != 0) {
            Log.e(TAG, "TF init status: " + status);
            throw new RuntimeException("TF init status (" + status + ") != 0");
        }
    }

    /**
     * 输入一张语图片，得到该图片属于各个类别的概率值。
     * @param bitmap
     * @return　该图片属于各个类别的概率。
     */
    public float[] recognize(Bitmap bitmap){
        //为输入节点准备数据
        float[] pixelArray = bitmapToFloatArray(bitmap);
        Log.i(TAG,"pixelArray:"+Arrays.toString(pixelArray));
        if(pixelArray.length != HEIGHT*WIDTH){
            throw new IllegalArgumentException("输入图片的像素矩阵的大小不对，传入的大小为:"+pixelArray.length +",需要的大小为："+(HEIGHT*WIDTH));
        }

        // 输入数据
        inferenceInterface.fillNodeFloat(INPUT_NODE, new int[]{1, HEIGHT, WIDTH}, pixelArray);
        inferenceInterface.fillNodeFloat(KEEP_PROB_NODE,new int[]{1},new float[]{1.0f});

        //进行模型的推理
        inferenceInterface.runInference(new String[]{OUTPUT_NODE});

        //获取输出节点的输出信息
        float[] outputs = new float[NUM_CLASSES];    //用于存储模型的输出数据
        inferenceInterface.readNodeFloat(OUTPUT_NODE, outputs); //获取输出数据

        return outputs;
    }

    public static int argmax(float[] prob){
        int result = 0;
        for(int i=1;i<prob.length;i++) {
            if (prob[result] < prob[i]) {
                result = i;
            }
        }
        return result;
    }

    /**
     * 将bitmap转为（按行优先）一个float数组。其中的每个像素点都归一化到0~1之间。
     * @param bitmap 灰度图，r,g,b分量都相等。
     * @return
     */
    public static float[] bitmapToFloatArray(Bitmap bitmap){
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        float[] result = new float[height*width];
        Log.i(TAG,"bitmap width:"+width+",height:"+height);
        Log.i(TAG,"bitmap.getConfig():"+bitmap.getConfig());

        int k = 0;

        //行优先
        for(int j = 0;j < height;j++){
            for (int i = 0;i < width;i++){
                int argb = bitmap.getPixel(i,j);

                int r = Color.red(argb);
                int g = Color.green(argb);
                int b = Color.blue(argb);
                int a = Color.alpha(argb);

                //由于是灰度图，所以r,g,b分量是相等的。
                assert(r==g && g==b);

//                Log.i(TAG,i+","+j+" : argb = "+argb+", a="+a+", r="+r+", g="+g+", b="+b);
                result[k++] = r / 255.0f;
            }
        }

        return result;
    }
}
