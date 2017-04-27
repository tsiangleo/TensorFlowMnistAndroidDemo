# TensorFlowMnistAndroidDemo

本项目将[https://github.com/tsiangleo/TensorFlowMnist](https://github.com/tsiangleo/TensorFlowMnist) 训练好后的网络模型移植到Android上，实现了一个小Demo。

## Demo运行效果
![Mnist在Android上的运行效果](http://upload-images.jianshu.io/upload_images/5736962-f3c633f154f0df34.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/300)

## 运行流程
这个Demo的运行流程非常简单：从手机上选择一张MNIST图片，得到识别结果。

原始的MNIST数据集是二进制格式，可以通过[```extract_mnist.py```](https://github.com/tsiangleo/TensorFlowMnist/blob/master/extract_mnist.py)脚本转为png格式的，然后将这些图片保存到手机上进行识别。