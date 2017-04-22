package com.github.tsiangleo.tfdemo;

import android.content.Context;
import android.icu.text.RelativeDateTimeFormatter;
import android.util.Log;

//import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * Created by tsiangleo on 17-2-17.
 */


public class RecognitionService {
//
//    public static final String TAG = "RecognitionService";
//
//    public static final String LABEL_FILE = "speaker_recognition_label_strings.txt";
//    public static final String MODEL_FILE = "file:///android_asset/sid_one.pb"; //模型存放路径
//
//    public static final String INPUT_NODE = "input";       //模型中输入变量的名称
//    public static final String OUTPUT_NODE = "out_softmax";  //模型中输出变量的名称
//
//    public static final int NUM_CLASSES = 5;               //样本集的类别数量
//    public static final int BATCH_SIZE = 16;
//    public static final int HEIGHT = 128;       //输入图片的像素高
//    public static final int WIDTH = 100;        //输入图片的像素宽
//    public static final int TIME_STEP = 1;
//
////    private double[] inputs = new double[HEIGHT*WIDTH];    //用于存储的模型输入数据
//
//
//    private TensorFlowInferenceInterface inferenceInterface = new TensorFlowInferenceInterface();   //接口定义
//
//    public static final String[] NAMES = new String[NUM_CLASSES];    //标签对应的名字
//
//
//    private Context context;
//
//    public RecognitionService(Context context){
//
//        System.loadLibrary("tensorflow_inference");
//
//        this.context = context;
//
//    }
//
//    /**
//     * 从LABEL_FILE文件中读取名字
//     * @throws Exception
//     */
//    private void loadNames() throws Exception{
//        InputStream in = context.getAssets().open(LABEL_FILE); //new FileInputStream(LABEL_FILE);
//        BufferedReader br = new BufferedReader(new InputStreamReader(in));
//        String line;
//        int index = 0;
//        while ((line = br.readLine()) != null){
//            NAMES[index++] = line;
//        }
//    }
//
//
//    public void init() throws Exception{
//        int retCode = inferenceInterface.initializeTensorFlow(context.getAssets(), MODEL_FILE);  //接口初始化
//        if (retCode != 0){
//            throw new RuntimeException("inferenceInterface.initializeTensorFlow运行失败:retCode is "+retCode);
//        }
//        loadNames();
//    }
//
//    /**
//     * 输入一张语谱图的像素点矩阵（行优先），每个点已经正规化了。
//     * @param normalizedPixelArray
//     * @return　该语谱图属于各个标签的概率。
//     */
//    public float[] recognize_batch(float[] normalizedPixelArray){
//        if(normalizedPixelArray.length != HEIGHT*WIDTH){
//            throw new IllegalArgumentException("输入的语谱图像素矩阵的大小不对，传入的大小为:"+normalizedPixelArray.length +",需要的大小为："+(HEIGHT*WIDTH));
//        }
//
//        //填充为０
//        float[] inputs = new float[BATCH_SIZE*normalizedPixelArray.length];
//        for(int i=0;i<normalizedPixelArray.length;i++){
//            inputs[i] = normalizedPixelArray[i];
//        }
//
//        inferenceInterface.fillNodeFloat(INPUT_NODE, new int[]{BATCH_SIZE, TIME_STEP, HEIGHT*WIDTH}, inputs);  //送入输入数据
//        inferenceInterface.fillNodeFloat("keep_prob_placeholder",new int[]{1},new float[]{1.0f});
//
//
//        int retCode = inferenceInterface.runInference(new String[]{OUTPUT_NODE});     //进行模型的推理
//
//        if (retCode != 0){
//            throw new RuntimeException("inferenceInterface.runInference运行失败:retCode is "+retCode);
//        }
//
//        float[] outputs = new float[NUM_CLASSES*BATCH_SIZE];    //用于存储模型的输出数据
//        inferenceInterface.readNodeFloat(OUTPUT_NODE, outputs); //获取输出数据
//
//
//        //获取第一个
//        float[] output = new float[NUM_CLASSES];
//        float sum = 0.0f;
//        for(int i=0;i<NUM_CLASSES;i++){
//            output[i] = outputs[i];
//            sum += output[i];
//        }
//        Log.i(TAG, "probability sum of one spectrogram is:"+sum+",are:"+ Arrays.toString(output));
//        return output;
//    }
//
//
//    /**
//     * 输入一张语谱图的像素点矩阵（行优先），每个点已经正规化了。
//     * @param normalizedPixelArray
//     * @return　该语谱图属于各个标签的概率。
//     */
//    public float[] recognize(float[] normalizedPixelArray){
//        if(normalizedPixelArray.length != HEIGHT*WIDTH){
//            throw new IllegalArgumentException("输入的语谱图像素矩阵的大小不对，传入的大小为:"+normalizedPixelArray.length +",需要的大小为："+(HEIGHT*WIDTH));
//        }
//
//        inferenceInterface.fillNodeFloat(INPUT_NODE, new int[]{1, TIME_STEP, HEIGHT*WIDTH}, normalizedPixelArray);  //送入输入数据
//        inferenceInterface.fillNodeFloat("keep_prob_placeholder",new int[]{1},new float[]{1.0f});
//
//
//        int retCode = inferenceInterface.runInference(new String[]{OUTPUT_NODE});     //进行模型的推理
//
//        if (retCode != 0){
//            throw new RuntimeException("inferenceInterface.runInference运行失败:retCode is "+retCode);
//        }
//
//        float[] outputs = new float[NUM_CLASSES];    //用于存储模型的输出数据
//        inferenceInterface.readNodeFloat(OUTPUT_NODE, outputs); //获取输出数据
//
//
//        float sum = 0.0f;
//        for(int i=0;i<NUM_CLASSES;i++){
//            sum += outputs[i];
//        }
////        Log.i(TAG, "probability sum of one spectrogram is:"+sum+",are:"+ Arrays.toString(outputs));
//
//        return outputs;
//    }
//
//
//    public static int argmax(float[] prob){
//        int result = 0;
//        for(int i=1;i<prob.length;i++) {
//            if (prob[result] < prob[i]) {
//                result = i;
//            }
//        }
//        return result;
//    }
//
//    public static int argmax(int[] prob){
//        int result = 0;
//        for(int i=1;i<prob.length;i++) {
//            if (prob[result] < prob[i]) {
//                result = i;
//            }
//        }
//        return result;
//    }
}
