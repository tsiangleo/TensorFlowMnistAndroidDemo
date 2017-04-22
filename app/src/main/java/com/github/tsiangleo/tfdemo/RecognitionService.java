package com.github.tsiangleo.tfdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.util.Arrays;


/**
 * Created by tsiangleo on 17-2-17.
 */


public class RecognitionService {

    public static final String TAG = "RecognitionService";

    public static final String MODEL_FILE = "file:///android_asset/mnist_output.pb"; //模型存放路径

    public static final String INPUT_NODE = "input";       //模型中输入变量的名称
    public static final String OUTPUT_NODE = "out_softmax";  //模型中输出变量的名称

    public static final int NUM_CLASSES = 10;               //样本集的类别数量

    public static final int HEIGHT = 28;       //输入图片的像素高
    public static final int WIDTH = 28;        //输入图片的像素宽

    private TensorFlowInferenceInterface inferenceInterface = new TensorFlowInferenceInterface();   //接口定义


    private Context context;

    public RecognitionService(Context context){
        System.loadLibrary("tensorflow_inference");
        this.context = context;

        int retCode = inferenceInterface.initializeTensorFlow(context.getAssets(), MODEL_FILE);  //接口初始化

        if (retCode != 0){
            throw new RuntimeException("inferenceInterface.initializeTensorFlow运行失败:retCode is "+retCode);
        }
    }

    /**
     * 输入一张语谱图的像素点矩阵（行优先），每个点已经正规化了。
     * @param bitmap
     * @return　该语谱图属于各个标签的概率。
     */
    public float[] recognize(Bitmap bitmap){
        float[] normalizedPixelArray = bitmap2FloatArray(bitmap);

        Log.i(TAG,"normalizedPixelArray:"+Arrays.toString(normalizedPixelArray));

        if(normalizedPixelArray.length != HEIGHT*WIDTH){
            throw new IllegalArgumentException("输入图片的像素矩阵的大小不对，传入的大小为:"+normalizedPixelArray.length +",需要的大小为："+(HEIGHT*WIDTH));
        }

        inferenceInterface.fillNodeFloat(INPUT_NODE, new int[]{1, HEIGHT, WIDTH}, normalizedPixelArray);  //送入输入数据
        inferenceInterface.fillNodeFloat("keep_prob_placeholder",new int[]{1},new float[]{1.0f});


        int retCode = inferenceInterface.runInference(new String[]{OUTPUT_NODE});     //进行模型的推理

        if (retCode != 0){
            throw new RuntimeException("inferenceInterface.runInference运行失败:retCode is "+retCode);
        }

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
     * 将bitmap转为（按行优先）一个float数组。其中的每个像素点都正则化到0到1之间。
     * @param bitmap 灰度图，r,g,b分量都相等。
     * @return
     */
    public static float[] bitmap2FloatArray(Bitmap bitmap){
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
