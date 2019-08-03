用java实现，GUI使用swing. 
(注：在上回基础上结构有所修改，但此处仅写出与本次有关的部分)

output\
  对应的class文件 main函数在TinyImageApp.class中


tinyimage\

  TinyImageApp.java
    只有一个main函数


  TinyImage.java
    负责读取和显示图片。
    （上回的Histogram模块已经被整理到tinyimage\histogram\下）


  ImageController.java
    TinyImage的各个模块的接口。
    目前包括tinyimage\histogram\HistogramController.java和
            tinyimage\spatialfilter\SpatialFilterController.java


tinyimage\spatialfilter\

  SpatialFilter.java
    public int filterPixel(int x, int y)
      计算单个像素的卷积（3个channel分开计算）

    public BufferedImage filter()
      计算整个图像的卷积

    在读取图像边界以外的时候用边界上最近的点近似。


  SpatialFilterController.java
    TinyImage第二个功能模块。其中有计算Gaussian Kernel等的代码：
      private JButton getJGaussKernel();
    在这个函数中定义了该按钮的时间处理程序，其中有计算Gaussian Kernel的代码。
    其他Kernel的计算在其他相应的函数内。


  JKernelPanel.java
    自定义控件，用于显示Kernel等。
